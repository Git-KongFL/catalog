package com.vankle.catalog.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.vankle.catalog.dao.CatalogArticleMapper;
import com.vankle.catalog.entity.CatalogArticleEntity;
import com.vankle.catalog.service.CalalogArticleService;
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
 * 目录信息
 * 
 * @author denghaihui
 * @date 2017-06-26 09:47:03
 * 
 */
@Service(group = "calalogArticleService", version = "1.0")
public class CalalogArticleServiceImpl implements CalalogArticleService {

	@Autowired
	CatalogArticleMapper catalogArticleMapper;
	private final static Logger logger = LoggerFactory.getLogger(CalalogProductServiceImpl.class);

	@Override
	public String getCategoryArticleListByParamJson(String paramJson) {

		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if (!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))) {
			return resultObj.toString();
		}
		int pageIndex = 1;
		int pageSize = VankleConstants.VANKLE_PAGE_SIZE;
		if (paramObj.containsKey("pageSize")) {
			pageSize = paramObj.getInt("pageSize");
		}
		if (paramObj.containsKey("pageSize")) {
			pageIndex = paramObj.getInt("pageIndex");
		}
		int storeId = paramObj.getInt("storeId");
		int offset = pageSize * (pageIndex - 1);
		List<CatalogArticleEntity> catalogArticleEntitys = catalogArticleMapper.findCatalogArticleEntity(storeId,
				pageSize, offset);
		int total = catalogArticleMapper.findCatalogArticleEntityCount(storeId);
		CatalogArticleEntity catalogArticleEntity = catalogArticleMapper.findOneCatalogArticleEntity(storeId);

		JSONObject dataObj = new JSONObject();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());
		PagerUtil pagerUtil = new PagerUtil(pageIndex, total, pageSize);
		dataObj.put("dataList", JSONArray.fromObject(catalogArticleEntitys, jsonConfig));
		dataObj.put("prePageIndex", pagerUtil.previous());
		dataObj.put("curPageIndex", pageIndex);
		dataObj.put("nextPageIndex", pagerUtil.next());
		dataObj.put("pageSize", pageSize);
		dataObj.put("rowsCount", total);
		dataObj.put("pageCount", pagerUtil.pageCount);
		dataObj.put("catalogArticleEntity", JSONObject.fromObject(catalogArticleEntity, jsonConfig));
		resultObj.put("data", dataObj);
		return resultObj.toString();
	}

	@Override
	public String findOneCatalogArticleEntityByTitle(String paramJson) {
		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if (!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))) {
			return resultObj.toString();
		}

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());
		String title = paramObj.getString("title");
		int storeId = paramObj.getInt("storeId");
		CatalogArticleEntity catalogArticleEntity = catalogArticleMapper.findOneCatalogArticleEntityByTitle(storeId,
				title);
		resultObj.put("data", JSONObject.fromObject(catalogArticleEntity, jsonConfig));
		return resultObj.toString();
	}
}