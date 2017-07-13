package com.vankle.catalog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.vankle.catalog.dao.CatalogCategoryEntityMapper;
import com.vankle.catalog.entity.CatalogCategoryEntity;
import com.vankle.catalog.service.CalalogCategoryService;
import com.vankle.code.constants.VankleConstants;
import com.vankle.code.util.JsonUtils;
import com.vankle.code.util.VankleUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
	/**
	 * 店铺商品类目 paramJson
	 * @param paramJson = {storeId:1}
	 * @return
	 */
	
	public String getCatalogCategoryInfoByParamJson(String paramJson){
		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new  JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if(!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))){
			return resultObj.toString();
		} 
		int storeId = paramObj.getInt("storeId");
		List<CatalogCategoryEntity>  catalogCategoryEntitys = catalogCategoryEntityMapper.findCatalogCategoryEntityList(storeId);

		JSONObject categoryObj = new JSONObject();
		categoryObj = this.getCategoryByCategoryList(categoryObj, catalogCategoryEntitys);
		
		resultObj.put("catalogCategory", categoryObj);
		//resultObj.put("catalogCategoryEntitys", JSONArray.fromObject(catalogCategoryEntitys) );
		return resultObj.toString();
	}
	
	public JSONObject getCategoryByCategoryList(JSONObject obj,List<CatalogCategoryEntity> catalogCategoryEntitys){
		System.out.println(obj.toString());
		if(obj.get("id")==null){
			for(CatalogCategoryEntity categoryEntity:catalogCategoryEntitys){
				if(categoryEntity.getParentCategoryId()==null){
					obj = JSONObject.fromObject(categoryEntity); 
					this.getCategoryByCategoryList(obj, catalogCategoryEntitys);
				}
			}
		}else{
			for(CatalogCategoryEntity categoryEntity:catalogCategoryEntitys){
				if(categoryEntity.getParentCategoryId()!=null&&obj.getInt("id")==categoryEntity.getParentCategoryId()){
					JSONObject objChild = JSONObject.fromObject(categoryEntity); 
					obj.put(objChild.get("id").toString(), objChild);
					this.getCategoryByCategoryList(obj.getJSONObject(objChild.get("id").toString()), catalogCategoryEntitys);
				}
			}
		}

		return obj;
	}
	
}