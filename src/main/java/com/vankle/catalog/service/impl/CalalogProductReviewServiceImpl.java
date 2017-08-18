package com.vankle.catalog.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.vankle.catalog.dao.CatalogCategoryEntityMapper;
import com.vankle.catalog.dao.CatalogProductReviewMapper;
import com.vankle.catalog.entity.CatalogCategoryProduct;
import com.vankle.catalog.entity.CatalogProductReview;
import com.vankle.catalog.service.CalalogProductReviewService;
import com.vankle.code.constants.RedisConstants;
import com.vankle.code.constants.VankleConstants;
import com.vankle.code.util.JsonDateValueProcessor;
import com.vankle.code.util.JsonUtils;
import com.vankle.code.util.PagerUtil;
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
@Service(group="calalogProductReviewService", version="1.0")
public class CalalogProductReviewServiceImpl implements CalalogProductReviewService {

	 
	@Autowired
	CatalogProductReviewMapper catalogProductReviewMapper;
	
	
	@Override
	public String getCategoryProductReviewByParamJson(String paramJson) {
		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new  JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if(!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))){
			return resultObj.toString();
		}   
		int productId = paramObj.getInt("productId");
		int pageIndex = paramObj.getInt("pageIndex"); 
		int pageSize = 10;// VankleConstants.VANKLE_PAGE_SIZE;
		int offset = pageSize*(pageIndex-1);
		Map<String, Object> pMap = new HashedMap();
		pMap.put("offset", offset);
		pMap.put("limit", pageSize);
		pMap.put("productId", productId);
		if(paramObj.get("score")!=null&&(!"".equals(paramObj.get("score").toString()))){
			pMap.put("score", paramObj.get("score"));
		}
		//
		List<CatalogProductReview> catalogCategoryProducts  = 
				catalogProductReviewMapper.listPage(pMap);
		int total = catalogProductReviewMapper.rows(pMap);
		 
		JSONObject dataObj = new JSONObject();
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
		
		resultObj.put("data",dataObj);
		 
		return  resultObj.toString();
	}

 
 
	
}