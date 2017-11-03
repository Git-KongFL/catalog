package com.vankle.catalog.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.vankle.catalog.dao.CatalogCategoryEntityMapper;
import com.vankle.catalog.dao.CatalogProductReviewMapper;
import com.vankle.catalog.dao.CatalogVoteLogMapper;
import com.vankle.catalog.dao.CatalogVoteMapper;
import com.vankle.catalog.entity.CatalogCategoryProduct;
import com.vankle.catalog.entity.CatalogProductReview;
import com.vankle.catalog.entity.CatalogVote;
import com.vankle.catalog.entity.CatalogVoteLog;
import com.vankle.catalog.service.CalalogProductReviewService;
import com.vankle.catalog.service.CalalogVoteService;
import com.vankle.code.constants.RedisConstants;
import com.vankle.code.constants.VankleCalalogConstants;
import com.vankle.code.constants.VankleConstants;
import com.vankle.code.constants.VankleCustomerConstants;
import com.vankle.code.util.JsonDateValueProcessor;
import com.vankle.code.util.JsonUtils;
import com.vankle.code.util.PagerUtil;
import com.vankle.code.util.VankleUtils;
import com.vankle.system.service.SystemService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 
 * 投票信息
 * @author denghaihui
 * @date 2017-06-26 09:47:03
 * 
 */
@Service(group="calalogVoteService", version="1.0")
public class CalalogVoteServiceImpl implements CalalogVoteService {

	@Autowired
	CatalogVoteMapper catalogVoteMapper;
	
	@Autowired
	CatalogVoteLogMapper catalogVoteLogMapper;
	
	@Reference(group = "systemService", version = "1.0", timeout = 6000)
	private SystemService systemService;
	
	@Override
	public String getCategoryVoteListByParamJson(String paramJson) {
		
		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new  JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if(!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))){
			return resultObj.toString();
		}   
		int pageIndex = paramObj.getInt("pageIndex"); 
		int pageSize =  VankleConstants.VANKLE_PAGE_SIZE;
		int offset = pageSize*(pageIndex-1);
		int currencyId = paramObj.getInt("currencyId");

		List<CatalogVote> catalogCategoryProducts  =  catalogVoteMapper.findCatalogVote(pageSize,offset);
		int total =  catalogVoteMapper.findCatalogVoteCount();
		 
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
		String jsonCurrency =  systemService.getCurrencyEntity(currencyId);
		dataObj.put("currency", JSONObject.fromObject(jsonCurrency));
		resultObj.put("data",dataObj);
		 
		return  resultObj.toString();
	}

	@Override
	public String setCategoryVoteByParamJson(String paramJson) {

		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new  JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if(!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))){
			return resultObj.toString();
		}   
		int voteId = paramObj.getInt("voteId");
		int voteType = paramObj.getInt("voteType"); 
		String customerId = paramObj.getString("customerId");
		
		int countVote = catalogVoteLogMapper.findCatalogVoteLogCount(voteId, customerId);
		if(countVote>0){
			return JsonUtils.modifyJSONObject(resultObj, VankleCalalogConstants.VANKLE_CODE_FAIL_15001, VankleCalalogConstants.VANKLE_CODE_FAIL_15001_MESSAGE    ).toString();
		}
		CatalogVoteLog entity = new CatalogVoteLog();
		entity.setCreateTime(new Date());
		entity.setDeletedStatus(1);
		entity.setVoteId(voteId);
		entity.setCustomerId(customerId);
		entity.setVoteType(voteType);
		catalogVoteLogMapper.addCatalogVoteLog(entity);
		CatalogVote catalogVote  = catalogVoteMapper.findCatalogVoteById(voteId);
		if(voteType==0){
			catalogVote.setListVoteNum(catalogVote.getListVoteNum()+1);
			catalogVote.setTotalVoteNum(catalogVote.getTotalVoteNum()+1);
		}else{
			catalogVote.setPageVoteNum(catalogVote.getPageVoteNum()+1);
			catalogVote.setTotalVoteNum(catalogVote.getTotalVoteNum()+1);
		}
		catalogVoteMapper.updateCatalogVote(catalogVote);
		return resultObj.toString();
	}

	@Override
	public String getCategoryVoteInfoByParamJson(String paramJson) {

		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new  JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if(!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))){
			return resultObj.toString();
		}   
		int voteId = paramObj.getInt("voteId");
		CatalogVote catalogVote = catalogVoteMapper.findCatalogVoteById(voteId);
		resultObj.put("data", catalogVote);
		return resultObj.toString();
	}
	
}