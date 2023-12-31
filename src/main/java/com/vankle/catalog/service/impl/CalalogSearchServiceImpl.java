package com.vankle.catalog.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.vankle.catalog.dao.CatalogCategoryEntityMapper;
import com.vankle.catalog.dao.CatalogCategoryProductMapper;
import com.vankle.catalog.entity.CatalogCategoryEntity;
import com.vankle.catalog.entity.CatalogCategoryProduct;
import com.vankle.catalog.service.CalalogCategoryService;
import com.vankle.catalog.service.CalalogSearchService;
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

@Service(group="calalogSearchService", version="1.0")
public class CalalogSearchServiceImpl implements CalalogSearchService {
	
	@Autowired
	CatalogCategoryEntityMapper catalogCategoryEntityMapper;
	@Autowired
	CatalogCategoryProductMapper catalogCategoryProductMapper;
	

	@Autowired
	SystemCurrencyService systemCurrencyService;
	
	
	@Reference(group = "systemService", version = "1.0", timeout = 60000)
	private SystemService systemService;
	private final static Logger logger = LoggerFactory.getLogger(CalalogProductServiceImpl.class);
	
	/**
	 * 商品分类列表paramJson
	 * @param paramJson =  {q:'keyword',storeId:1,languageId:1,currencyId:1,pageIndex:1,orderBy:{order:'name',dir:'desc'}} ;
	 * @return
	 */
	@Override
	public String getCategorySearchByParamJson(String paramJson) {
		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new  JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if(!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))){
			return resultObj.toString();
		} 
		String  q = paramObj.getString("q").trim().replace("'", "\\\'");
		String  spu = paramObj.getString("q").trim().replace("'", "\\\'");;
//      String[] qArr = q.split(" ");
//		String  searchKeyword = "";
//		for(String keyword:qArr) {
//			if(!"".equals(keyword.trim())) {
//				searchKeyword +=  keyword;
//			}
//		}
//		q = searchKeyword; 
		
		int storeId = paramObj.getInt("storeId");
		int languageId = paramObj.getInt("languageId");
		int pageIndex = paramObj.getInt("pageIndex");
		int currencyId = paramObj.getInt("currencyId");
		int pageSize =  VankleConstants.VANKLE_PAGE_SIZE;
		int offset = pageSize*(pageIndex-1);
		 
		  
		String orderBy = "order by  m.score  desc ";
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
		}
				
		String countryId = "";
		if(paramObj.get("prefixion")!=null){
			try{
				countryId = paramObj.getString("prefixion");
				if(countryId.split("-").length==2){
					countryId = countryId.split("-")[1];
				}
			}catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		int total = 0;
		List<CatalogCategoryProduct> catalogCategoryProducts = new ArrayList<CatalogCategoryProduct>();
		if(languageId==1) {
			total = catalogCategoryProductMapper.findCatalogSearchProductCount(storeId,languageId,q,spu);
			catalogCategoryProducts = catalogCategoryProductMapper.
					findCatalogSearchProductList(storeId,q,spu,languageId,countryId,pageSize,offset,orderBy);
		}else {
			total = catalogCategoryProductMapper.findCatalogSearchProductByLanguageIdCount(storeId,languageId,q,spu);
			catalogCategoryProducts = catalogCategoryProductMapper.
					findCatalogSearchProductByLanguageIdList(storeId,q,spu,languageId,countryId,pageSize,offset,orderBy);
		}
		
		
		
		
		
		JSONObject dataObj = new JSONObject(); 
		//categoryId
		CatalogCategoryEntity catalogCategoryEntityBar = catalogCategoryEntityMapper.findCatalogCategoryEntityByLevel(storeId, languageId);
		//dataObj.put("catalogCategoryEntity", catalogCategoryEntityBar);
		dataObj.put("sortBar", JSONArray.fromObject(catalogCategoryEntityBar.getOrderBy()) );
		
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());  
		PagerUtil pagerUtil = new PagerUtil(pageIndex,total,pageSize);
		dataObj.put("dataList", JSONArray.fromObject(  catalogCategoryProducts,jsonConfig));
		dataObj.put("prePageIndex", pagerUtil.previous());
		dataObj.put("curPageIndex", pageIndex);
		dataObj.put("nextPageIndex", pagerUtil.next()); 
		dataObj.put("pageSize", pageSize);
		dataObj.put("rowsCount", total);
		dataObj.put("pageCount", pagerUtil.pageCount); 
		String jsonCurrency =  systemService.getCurrencyEntity(currencyId);
		dataObj.put("currency", JSONObject.fromObject(jsonCurrency));
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

			obj.put("baseDiscountAmount", obj.getString("discountAmount")); 
			obj.put("discountAmount", discountAmount);
			BigDecimal sellPrice =  systemCurrencyService.getAmountByCurrencyId(new BigDecimal(obj.getString("sellPrice")), currencyId);
			obj.put("baseSellPrice", obj.getString("sellPrice")); 
			obj.put("sellPrice", sellPrice);
		}
		return resoutObj.toString();
	}
	
	
}