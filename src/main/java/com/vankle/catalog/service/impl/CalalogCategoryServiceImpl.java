package com.vankle.catalog.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.vankle.catalog.dao.CatalogCategoryEntityMapper;
import com.vankle.catalog.dao.CatalogCategoryProductMapper;
import com.vankle.catalog.entity.CatalogCategoryEntity;
import com.vankle.catalog.entity.CatalogCategoryProduct;
import com.vankle.catalog.service.CalalogCategoryService;
import com.vankle.code.constants.RedisConstants;
import com.vankle.code.constants.VankleConstants;
import com.vankle.code.dao.RedisDao;
import com.vankle.code.util.JsonDateValueProcessor;
import com.vankle.code.util.JsonUtils;
import com.vankle.code.util.PagerUtil;
import com.vankle.code.util.VankleUtils;
import com.vankle.system.service.SystemCurrencyService;

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
	
	@Autowired
	RedisDao redisDao;
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
		int storeId = paramObj.getInt("storeId");
		
		String resultStr =  redisDao.getValue(RedisConstants.VANKLE_REDIS_CATALOG_CATEGORY+ storeId);
		logger.info(resultStr);
		if(resultStr!=null){
			return resultStr;
		}
		
		List<CatalogCategoryEntity>  catalogCategoryEntitys = catalogCategoryEntityMapper.findCatalogCategoryEntityList(storeId);
		JSONObject categoryObj = new JSONObject();
		categoryObj = this.getCategoryByCategoryList(categoryObj, catalogCategoryEntitys);
		resultObj.put("data", categoryObj.get("childs"));
		
		redisDao.setKey(RedisConstants.VANKLE_REDIS_CATALOG_CATEGORY+ storeId , resultObj.toString());
		 
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
	 * 商品分类列表paramJson
	 * @param paramJson =  "{categoryId:2,languageId:1,currencyId:1,pageIndex:1}";
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
		int languageId = paramObj.getInt("languageId");
		int pageIndex = paramObj.getInt("pageIndex");
		//int offset = paramObj.getInt("offset");
		int currencyId = paramObj.getInt("currencyId");
		int pageSize =  VankleConstants.VANKLE_PAGE_SIZE;
		int offset = pageSize*(pageIndex-1);
		
		String resultStr =  redisDao.getValue(RedisConstants.VANKLE_REDIS_CATALOG_CATEGORY_PRODUCT_LIST+ categoryId+languageId+pageSize+offset);
		
		logger.info(resultStr);
		if(resultStr!=null){
			return this.getCategoryProductByCurrencyId(resultStr, currencyId);
		} 
		
		int total = catalogCategoryProductMapper.findCatalogCategoryProductCount(categoryId);
		List<CatalogCategoryProduct> catalogCategoryProducts = catalogCategoryProductMapper.
				findCatalogCategoryProductList(categoryId,languageId,pageSize,offset);
		JSONObject dataObj = new JSONObject();

		PagerUtil pagerUtil = new PagerUtil(pageIndex,total,pageSize);
		dataObj.put("dataList", JSONArray.fromObject(  catalogCategoryProducts));
		dataObj.put("pageIndex", pageIndex);
		dataObj.put("pageSize", pageSize);
		dataObj.put("rowTotal", total);
		dataObj.put("endRowIndex", pagerUtil.pageCount);
		
		resultObj.put("data",dataObj);
		redisDao.setKey(RedisConstants.VANKLE_REDIS_CATALOG_CATEGORY_PRODUCT_LIST+ categoryId+languageId+pageIndex+offset, resultObj.toString());
		
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