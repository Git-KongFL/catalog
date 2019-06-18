package com.vankle.catalog.service.impl;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.vankle.catalog.dao.CatalogCategoryEntityMapper;
import com.vankle.catalog.dao.CatalogProductEntityMapper;
import com.vankle.catalog.dao.CatalogProductReviewImageMapper;
import com.vankle.catalog.dao.CatalogProductReviewMapper;
import com.vankle.catalog.entity.CatalogCategoryProduct;
import com.vankle.catalog.entity.CatalogProductEntity;
import com.vankle.catalog.entity.CatalogProductReview;
import com.vankle.catalog.entity.CatalogProductReviewImage;
import com.vankle.catalog.service.CalalogProductReviewService;
import com.vankle.code.constants.RedisConstants;
import com.vankle.code.constants.VankleConstants;
import com.vankle.code.util.JsonDateValueProcessor;
import com.vankle.code.util.JsonUtils;
import com.vankle.code.util.PagerUtil;
import com.vankle.code.util.VankleUtils;
import com.vankle.sales.entity.SalesOrderEntity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONBuilder;

/**
 * 
 * 商品评论信息
 * @author denghaihui
 * @date 2017-06-26 09:47:03
 * 
 */
@Service(group="calalogProductReviewService", version="1.0")
public class CalalogProductReviewServiceImpl implements CalalogProductReviewService {

	 
	@Autowired
	CatalogProductReviewMapper catalogProductReviewMapper;
	@Autowired
    private CatalogProductEntityMapper catalogProductEntityMapper;
	@Autowired
	CatalogProductReviewImageMapper catalogProductReviewImageMapper ;
	
	
	
	@Override
	public String addCategoryProductReview(String paramJson) {
		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if (!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))) {
			return resultObj.toString();
		} 	  
		int storeId = paramObj.getInt("storeId");
		//SalesOrderEntity salesOrderEntity = salesOrderEntityMapper.findSalesOrderEntity(orderId);
		
		if(paramObj.get("reviewArray")!=null){
			JSONArray reviewArray = paramObj.getJSONArray("reviewArray");
			for(int i =0 ; i< reviewArray.size(); i++){
				JSONObject  reviewObj = reviewArray.getJSONObject(i);
				int itemId  = reviewObj.getInt("itemId");

				CatalogProductEntity catalogProductEntity = catalogProductEntityMapper.findCatalogProductEntityByItemId(itemId, storeId);
				
				CatalogProductReview catalogProductReview = new CatalogProductReview();
				catalogProductReview.setCreateTime(new Date());
				catalogProductReview.setCustomerId(null);
				catalogProductReview.setCustomerName(reviewObj.getString("customerName"));
				catalogProductReview.setDescription(reviewObj.getString("description"));
				catalogProductReview.setDeletedStatus(1);
				catalogProductReview.setOrderCreateTime(new Date());
				catalogProductReview.setOrderItemId(null);
				catalogProductReview.setOrderId(null);
				//catalogProductReview.setProductId(reviewObj.getInt("productId"));
				catalogProductReview.setProductId(catalogProductEntity.getId());
				catalogProductReview.setScore(reviewObj.getInt("score"));
				catalogProductReview.setStoreId(storeId);
				catalogProductReview.setUserId(itemId+"");
				catalogProductReview.setStatus(0);
				catalogProductReviewMapper.addCatalogProductReview(catalogProductReview);
				if(reviewObj.get("imageArray")!=null){
					JSONArray imageArray = reviewObj.getJSONArray("imageArray");
					for(int j=0 ;j<imageArray.size();j++){
						JSONObject imageObj = imageArray.getJSONObject(j);
						CatalogProductReviewImage entity = new CatalogProductReviewImage();
						entity.setCreateTime(new Date());
						entity.setDeletedStatus(1);
						entity.setImageUrl(imageObj.getString("imageUrl"));
						entity.setPosition(j);
						entity.setProductReviewId(catalogProductReview.getId());
						catalogProductReviewImageMapper.addCatalogProductReviewImage(entity);
					}
				} 
			}
		}
		return resultObj.toString();
	}

	
	@Override
	public String getCategoryProductReviewByParamJson(String paramJson) {
		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new  JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if(!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))){
			return resultObj.toString();
		}   
		int productId = paramObj.getInt("productId");
		int storeId = paramObj.getInt("storeId");
		
		CatalogProductEntity catalogProductEntity = catalogProductEntityMapper.findCatalogProductEntityByItemId(productId, storeId);
		//判断商品是否存在
		if(catalogProductEntity==null){
		}else{
			productId = catalogProductEntity.getId();
		}

		int pageIndex = paramObj.getInt("pageIndex"); 
		int pageSize = 10;// VankleConstants.VANKLE_PAGE_SIZE;
		
		if(paramObj.get(pageSize)!=null){
			try{
				pageSize = paramObj.getInt("pageSize");
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		int offset = pageSize*(pageIndex-1);
		Map<String, Object> pMap = new HashedMap();
		pMap.put("offset", offset);
		pMap.put("limit", pageSize);
		pMap.put("productId", productId);
		
		//sortScore all 全部 12345 显示星星
		if(paramObj.get("sortScore")!=null&&(!"".equals(paramObj.get("sortScore").toString()))){
			if(!"all".equals(paramObj.getString("sortScore")))
				pMap.put("score", paramObj.get("sortScore"));
		}
		
		//sortType: all 全部 ，1文字， 2图片， 3 视频 
		if(paramObj.get("sortType")!=null&&(!"".equals(paramObj.get("sortType").toString()))){
			if(!"all".equals(paramObj.getString("sortType")))
				pMap.put("resType", paramObj.get("sortType"));
		}
		
	  
		//sortBy 1 recent 2 helpful, 暂时不做
//		if(paramObj.get("sortBy")!=null&&(!"".equals(paramObj.get("sortBy").toString()))){
//			pMap.put("sortBy", paramObj.get("sortBy"));
//		}
		
		//
		List<CatalogProductReview> catalogCategoryProducts = catalogProductReviewMapper.listPage(pMap);
		int total = catalogProductReviewMapper.rows(pMap);
		

		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor()); 
		JSONArray catalogCategoryProductsArray = JSONArray.fromObject(  catalogCategoryProducts,jsonConfig);
		for(int i=0;i<catalogCategoryProductsArray.size();i++){
			JSONObject catalogReviewObj = catalogCategoryProductsArray.getJSONObject(i);
			int resType = catalogReviewObj.getInt("resType");
			JSONArray imageArray = new JSONArray();
			if(resType>1){
				List<CatalogProductReviewImage> imageList = catalogProductReviewImageMapper.findCatalogProductReviewImageList(catalogReviewObj.getInt("id"));
				for(CatalogProductReviewImage productReviewImage:imageList){
					JSONObject imgObj = new JSONObject();
					imgObj.put("imgUrl", productReviewImage.getImageUrl());
					imageArray.add(imgObj);
				}
			}
			catalogReviewObj.put("imageArray", imageArray);
		}
		
		JSONObject dataObj = new JSONObject(); 
		PagerUtil pagerUtil = new PagerUtil(pageIndex,total,pageSize);
		dataObj.put("dataList", catalogCategoryProductsArray);
		dataObj.put("prePageIndex", pagerUtil.previous());
		dataObj.put("curPageIndex", pageIndex);
		dataObj.put("nextPageIndex", pagerUtil.next()); 
		dataObj.put("pageSize", pageSize);
		dataObj.put("rowsCount", total);
		dataObj.put("pageCount", pagerUtil.pageCount); 
		 
		Map<Integer,Double> reviewGroup = new HashedMap();
		reviewGroup.put(5, 0.0);
		reviewGroup.put(4, 0.0);
		reviewGroup.put(3, 0.0);
		reviewGroup.put(2, 0.0);
		reviewGroup.put(1, 0.0);
		
		Double scoreTotal = 0.0;
		
		 
		pMap.remove("score");
		pMap.remove("resType");
		List<Map> reviewGroupBy = catalogProductReviewMapper.listGroupByScore(pMap);
		for(Map map: reviewGroupBy){
			reviewGroup.put(Integer.parseInt(  map.get("score").toString()),Double.parseDouble(   map.get("total").toString()));
			scoreTotal += Integer.parseInt(  map.get("total").toString());
		}

		DecimalFormat df = new DecimalFormat("#.00");
		JSONObject positiveRatingObj = new JSONObject();
		
		double positiveRating =  reviewGroup.get(5)+reviewGroup.get(4) ;
		if(scoreTotal!=0&&scoreTotal!=positiveRating){
			positiveRatingObj.put("positiveRating", df.format( (positiveRating/scoreTotal)*100));
		}else{
			positiveRatingObj.put("positiveRating",100);
		}
		 
		JSONArray scoreArray = new JSONArray();
		for(int i=5;i>0;i--){
			JSONObject obj = new JSONObject();
			obj.put("score", i);
			if(reviewGroup.get(i)!=0)
				obj.put("percent", df.format( (reviewGroup.get(i)/scoreTotal)*100));
			else
				obj.put("percent", 0);
			obj.put("total", reviewGroup.get(i));
			scoreArray.add(obj);
		}
		positiveRatingObj.put("scoreArray", scoreArray);
		
		dataObj.put("positiveRatingObj", positiveRatingObj);
		resultObj.put("data",dataObj);
		return  resultObj.toString();
	}
 
}