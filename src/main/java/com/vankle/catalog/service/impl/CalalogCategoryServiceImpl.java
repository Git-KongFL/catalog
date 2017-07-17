package com.vankle.catalog.service.impl;

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
import com.vankle.code.util.VankleUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

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
	 * @param paramJson =  "{categoryId:1,languageId:1,currencyId:1,step:30,offset:0}";
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
		int step = paramObj.getInt("step");
		int offset = paramObj.getInt("offset");
		
		String resultStr =  redisDao.getValue(RedisConstants.VANKLE_REDIS_CATALOG_CATEGORY_PRODUCT_LIST+ categoryId+languageId+step+offset);
		
		logger.info(resultStr);
		if(resultStr!=null){
			return resultStr;
		} 
		
		int total = catalogCategoryProductMapper.findCatalogCategoryProductCount(categoryId);
		List<CatalogCategoryProduct> catalogCategoryProducts = catalogCategoryProductMapper.
				findCatalogCategoryProductList(categoryId,languageId,step,offset);
		JSONObject dataObj = new JSONObject();
		dataObj.put("total", total);
		dataObj.put("rows", JSONArray.fromObject(  catalogCategoryProducts));
		resultObj.put("data",dataObj);
		
		redisDao.setKey(RedisConstants.VANKLE_REDIS_CATALOG_CATEGORY_PRODUCT_LIST+ categoryId+languageId+step+offset, resultObj.toString());
		
		return resultObj.toString();
	}
	
}