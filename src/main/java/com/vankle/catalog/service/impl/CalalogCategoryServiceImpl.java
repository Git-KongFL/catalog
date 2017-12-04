package com.vankle.catalog.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.vankle.catalog.entity.CatalogCategoryEntity;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.vankle.catalog.dao.CatalogCategoryEntityMapper;
import com.vankle.catalog.dao.CatalogCategoryProductMapper;
import com.vankle.catalog.entity.CatalogCategoryEntity;
import com.vankle.catalog.entity.CatalogCategoryProduct;
import com.vankle.catalog.service.CalalogCategoryService;
import com.vankle.code.constants.RedisConstants;
import com.vankle.code.constants.VankleConstants;
import com.vankle.code.util.JsonDateValueProcessor;
import com.vankle.code.util.JsonUtils;
import com.vankle.code.util.PagerUtil;
import com.vankle.code.util.VankleUtils;
import com.vankle.system.service.SystemCurrencyService;
import com.vankle.system.service.SystemService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONBuilder;

/**
 * 
 * 商品类目信息
 * @author denghaihui
 * @date 2017-06-26 09:47:03
 * 
 */

@Service(group="calalogCategoryService", version="1.0")
public class CalalogCategoryServiceImpl implements CalalogCategoryService {
	
	@Autowired
	CatalogCategoryEntityMapper catalogCategoryEntityMapper;

	@Autowired
	CatalogCategoryProductMapper catalogCategoryProductMapper;
	

	@Autowired
	SystemCurrencyService systemCurrencyService;
	
	
	@Reference(group = "systemService", version = "1.0", timeout = 6000)
	private SystemService systemService;
	
	private final static Logger logger = LoggerFactory.getLogger(CalalogProductServiceImpl.class); 
	
	/**
	 * 店铺商品类目 paramJson
	 * @param paramJson = {storeId:1}
	 * @return
	 */

	@Override
	public String getCatalogCategoryInfoByParamJson(String paramJson){
		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new  JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if(!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))){
			return resultObj.toString();
		} 
		paramObj.put("data", new JSONArray());
		int storeId = paramObj.getInt("storeId");
		int languageId = paramObj.getInt("languageId");
		
		List<CatalogCategoryEntity>  catalogCategoryEntitys = catalogCategoryEntityMapper.findCatalogCategoryEntityList(storeId,languageId);
		JSONObject categoryObj = new JSONObject();
		categoryObj = this.getCategoryByCategoryList(categoryObj, catalogCategoryEntitys);
		if(categoryObj.get("childs")==null)
			resultObj.put("data", new JSONArray());
		else
			resultObj.put("data",categoryObj.get("childs"));
		CatalogCategoryEntity catalogCategoryEntity = catalogCategoryEntityMapper.findCatalogCategoryEntityByIdRoot(storeId,languageId);
		resultObj.put("catalogCategoryEntity", catalogCategoryEntity);
		return resultObj.toString();
	}
	
	
	//递归生成类目列表
	public JSONObject getCategoryByCategoryList(JSONObject obj,List<CatalogCategoryEntity> catalogCategoryEntitys){
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());  
		if(obj.get("id")==null){
			for(CatalogCategoryEntity categoryEntity:catalogCategoryEntitys){
				if(categoryEntity.getParentCategoryId()==null){
					obj = JSONObject.fromObject(categoryEntity,jsonConfig); 
					JSONArray childs = new JSONArray();
					obj.put("childs", childs);
					this.getCategoryByCategoryList(obj, catalogCategoryEntitys);
				}
			}
		}else{
			for(CatalogCategoryEntity categoryEntity:catalogCategoryEntitys){
				if(categoryEntity.getParentCategoryId()!=null&&obj.getInt("id")==categoryEntity.getParentCategoryId()){
					JSONObject objChild = JSONObject.fromObject(categoryEntity,jsonConfig); 
					JSONArray childsObj = new JSONArray();
					objChild.put("childs", childsObj);
					JSONArray childs = obj.getJSONArray("childs");
					childs.add(objChild);
					obj.put("childs", childs);
					this.getCategoryByCategoryList(obj.getJSONArray("childs").getJSONObject(childs.size()-1), catalogCategoryEntitys);
				}
			}
		}
		return obj;
	}

	
	/**
	 * 商品分类列表paramJson  field :name/price/new
	 * @param paramJson =  "{storeId:1,categoryId:2,languageId:1,currencyId:1,pageIndex:1,orderBy:{order:'name',dir:'desc'}}";
	 * @return
	 */
	@Override
	public String getCategoryProductInfoByParamJson(String paramJson) {
		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new  JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if(!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))){
			return resultObj.toString();
		} 
		int categoryId = paramObj.getInt("categoryId");
		int storeId = paramObj.getInt("storeId");
		int languageId = paramObj.getInt("languageId");
		int pageIndex = paramObj.getInt("pageIndex");
		int currencyId = paramObj.getInt("currencyId");
		int pageSize =  VankleConstants.VANKLE_PAGE_SIZE;
		int offset = pageSize*(pageIndex-1);
		
		List<CatalogCategoryProduct> adpProductList = new ArrayList<CatalogCategoryProduct>();
		Map<String,String> spuMap = new HashMap<String,String>();
		String adp = "";
		if(paramObj.get("adp")!=null){
			adp = paramObj.get("adp")+"";
			String[] spus = adp.split(",");
			for(String spu:spus){
				spuMap.put(spu, spu);
				List<CatalogCategoryProduct> spuProductList = catalogCategoryProductMapper.
						findCatalogCategoryProductListBySpu(spu, storeId, languageId);
				for(CatalogCategoryProduct catalogCategoryProduct:spuProductList){
					adpProductList.add(catalogCategoryProduct);
				}
			}
		}
		
		String countryId = "";
		if(paramObj.get("countryId")!=null){
			try{
				countryId = paramObj.getString("prefixion");
				if(countryId.split("-").length==2){
					countryId = countryId.split("-")[1];
				}
			}catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		
		String orderBy = "";
		if(paramObj.get("orderBy")!=null){
			JSONObject orderObject = paramObj.getJSONObject("orderBy");
			if("Recommend".equalsIgnoreCase(orderObject.getString("order"))){
				//if("desc".equalsIgnoreCase(orderObject.getString("dir"))){
					orderBy = " order by  m.score  desc ";
//				}else{
//					orderBy = " order by  m.name  asc ";
//				}
			}else if("price".equalsIgnoreCase(orderObject.getString("order"))){
				if("desc".equalsIgnoreCase(orderObject.getString("dir"))){
					orderBy = " order by  m.discountAmount  desc ";
				}else{
					orderBy = " order by  m.discountAmount   asc ";
				}
			}else if("new".equalsIgnoreCase(orderObject.getString("order"))){
				if("desc".equalsIgnoreCase(orderObject.getString("dir"))){
					orderBy = " order by  m.sortDate  desc ";
				}else{
					orderBy = " order by  m.sortDate  asc ";
				}
			}
			
//			if("name".equalsIgnoreCase(orderObject.getString("order"))){
//				if("desc".equalsIgnoreCase(orderObject.getString("dir"))){
//					orderBy = " order by  m.name  desc ";
//				}else{
//					orderBy = " order by  m.name  asc ";
//				}
//			}else if("price".equalsIgnoreCase(orderObject.getString("order"))){
//				if("desc".equalsIgnoreCase(orderObject.getString("dir"))){
//					orderBy = " order by  m.discountAmount  desc ";
//				}else{
//					orderBy = " order by  m.discountAmount   asc ";
//				}
//			}else if("new".equalsIgnoreCase(orderObject.getString("order"))){
//				if("desc".equalsIgnoreCase(orderObject.getString("dir"))){
//					orderBy = " order by  m.createTime  desc ";
//				}else{
//					orderBy = " order by  m.createTime  asc ";
//				}
//			}
		}
		
		
		JSONObject dataObj = new JSONObject();
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());  
		
		 
		//categoryId
		CatalogCategoryEntity catalogCategoryEntity = catalogCategoryEntityMapper.findCatalogCategoryEntityById(categoryId, languageId);
		CatalogCategoryEntity catalogCategoryEntityBar = catalogCategoryEntityMapper.findCatalogCategoryEntityByLevel(storeId, languageId);
		dataObj.put("catalogCategoryEntity", catalogCategoryEntity);
		dataObj.put("sortBar", JSONArray.fromObject(catalogCategoryEntityBar.getOrderBy()) );
	
		String jsonCurrency =  systemService.getCurrencyEntity(currencyId);
		dataObj.put("currency", JSONObject.fromObject(jsonCurrency));
		
				
		int total = catalogCategoryProductMapper.findCatalogCategoryProductCount(storeId,categoryId);
		List<CatalogCategoryProduct> catalogCategoryProducts = catalogCategoryProductMapper.
				findCatalogCategoryProductList(storeId,categoryId,languageId,countryId,pageSize,offset,orderBy);
		
		for(CatalogCategoryProduct catalogCategoryProduct:catalogCategoryProducts){
			if(spuMap.get(catalogCategoryProduct.getSpu()+"")==null){
				adpProductList.add(catalogCategoryProduct);
			}
		}
		
		PagerUtil pagerUtil = new PagerUtil(pageIndex,total,pageSize);
		
		dataObj.put("dataList", JSONArray.fromObject(  adpProductList,jsonConfig));
		dataObj.put("prePageIndex", pagerUtil.previous());
		dataObj.put("curPageIndex", pageIndex);
		dataObj.put("nextPageIndex", pagerUtil.next()); 
		dataObj.put("pageSize", pageSize);
		dataObj.put("rowsCount", total);
		dataObj.put("pageCount", pagerUtil.pageCount); 

		
		resultObj.put("data",dataObj);
		return  this.getCategoryProductByCurrencyId(resultObj.toString(), currencyId) ;
	}
	
	public String getCategoryProductByCurrencyId(String paramJson,int currencyId){
		 
		JSONObject resoutObj = JSONObject.fromObject(paramJson);
		JSONArray  arrayObj =  resoutObj.getJSONObject("data").getJSONArray("dataList");
		if(arrayObj==null||arrayObj.size()==0){
			return paramJson ;
		}
		for(int i=0;i<arrayObj.size();i++){
			JSONObject obj = arrayObj.getJSONObject(i);
			BigDecimal discountAmount =  systemCurrencyService.getAmountByCurrencyId(new BigDecimal(obj.getString("discountAmount")), currencyId);
			obj.put("discountAmount", discountAmount);
			BigDecimal sellPrice =  systemCurrencyService.getAmountByCurrencyId(new BigDecimal(obj.getString("sellPrice")), currencyId);
			obj.put("sellPrice", sellPrice);
		}
		return resoutObj.toString();
	}
	
	
	
}