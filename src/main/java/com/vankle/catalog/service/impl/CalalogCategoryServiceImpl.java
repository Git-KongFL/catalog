package com.vankle.catalog.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.vankle.catalog.entity.CatalogCategoryEntity;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.vankle.catalog.dao.CatalogCategoryEntityMapper;
import com.vankle.catalog.dao.CatalogCategoryProductMapper;
import com.vankle.catalog.dao.CatalogProductSkuMapper;
import com.vankle.catalog.entity.CatalogCategoryEntity;
import com.vankle.catalog.entity.CatalogCategoryProduct;
import com.vankle.catalog.entity.CatalogProductSku;
import com.vankle.catalog.service.CalalogCategoryService;
import com.vankle.code.constants.RedisConstants;
import com.vankle.code.constants.VankleConstants;
import com.vankle.code.util.JsonDateValueProcessor;
import com.vankle.code.util.JsonUtils;
import com.vankle.code.util.PagerUtil;
import com.vankle.code.util.VankleUtils;
import com.vankle.system.dao.SystemCountryLanguageMapper;
import com.vankle.system.entity.SystemCountryLanguage;
import com.vankle.system.service.SystemCurrencyService;
import com.vankle.system.service.SystemService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONBuilder;

/**
 * 
 * 商品类目信息
 * 
 * @author denghaihui
 * @date 2017-06-26 09:47:03
 * 
 */

@Service(group = "calalogCategoryService", version = "1.0")
public class CalalogCategoryServiceImpl implements CalalogCategoryService {

	@Autowired
	CatalogCategoryEntityMapper catalogCategoryEntityMapper;

	@Autowired
	CatalogCategoryProductMapper catalogCategoryProductMapper;

	@Autowired
	SystemCountryLanguageMapper systemCountryLanguageMapper;

	@Autowired
	CatalogProductSkuMapper catalogProductSkuMapper;

	@Autowired
	SystemCurrencyService systemCurrencyService;

	@Reference(group = "systemService", version = "1.0", timeout = 60000)
	private SystemService systemService;

	private final static Logger logger = LoggerFactory.getLogger(CalalogProductServiceImpl.class);

	/**
	 * 店铺商品类目 paramJson
	 * 
	 * @param paramJson = {storeId:1}
	 * @return
	 */

	@Override
	public String getCatalogCategoryInfoByParamJson(String paramJson) {
		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if (!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))) {
			return resultObj.toString();
		}
		paramObj.put("data", new JSONArray());
		int storeId = paramObj.getInt("storeId");
		int languageId = paramObj.getInt("languageId");

		String iso2 = "US";
		try {
			String prefixion = paramObj.getString("prefixion");// 指 https://www.vancaro.com/en-us/blog 中的 en-us
			if (prefixion.length() < 5) {
//				logger.info("---------------prefixion为：" + prefixion);
				iso2 = "US";
			} else {
				// prefixion长度小于5时会报错
				iso2 = prefixion.substring(3, 5).toUpperCase();
			}
			List<SystemCountryLanguage> iso2List = systemCountryLanguageMapper.findSystemCountryList();
			boolean isoBool = true;
			for (SystemCountryLanguage country : iso2List) {
				if (iso2.equalsIgnoreCase(country.getIso2())) {
					isoBool = false;
					break;
				}
			}
			if (isoBool) {
				// 没有在数据库中找到对应的iso2，默认使用US
				iso2 = "US";
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		// 根据店铺、语言、国家找到所有的目录
		List<CatalogCategoryEntity> catalogCategoryEntitys = catalogCategoryEntityMapper
				.findCatalogCategoryEntityByIso2List(storeId, languageId, iso2.toLowerCase());
//		List<CatalogCategoryEntity>  catalogCategoryEntitysNew = new ArrayList<CatalogCategoryEntity>();
//		for(CatalogCategoryEntity catalogCategoryEntity:catalogCategoryEntitys) {
//			if(catalogCategoryEntity.getIso2()!=null&&catalogCategoryEntity.getIso2().indexOf(iso2)>=0) {
//				catalogCategoryEntitysNew.add(catalogCategoryEntity);
//			}
//		}

		JSONObject categoryObj = new JSONObject();
		categoryObj = this.getCategoryByCategoryList(categoryObj, catalogCategoryEntitys);
//		System.out.println("categoryObj:"+JSONArray.fromObject(catalogCategoryEntitys));
//		System.out.println("----------------------------------------------------------------------------------");
//		System.out.println("categoryObj:"+categoryObj.toString());
		if (categoryObj.get("childs") == null)
			resultObj.put("data", new JSONArray());
		else
			resultObj.put("data", categoryObj.get("childs"));
		CatalogCategoryEntity catalogCategoryEntity = catalogCategoryEntityMapper
				.findCatalogCategoryEntityByIdRoot(storeId, languageId);
		resultObj.put("catalogCategoryEntity", catalogCategoryEntity);
		return resultObj.toString();
	}

	// 递归生成类目列表
	public JSONObject getCategoryByCategoryList(JSONObject obj, List<CatalogCategoryEntity> catalogCategoryEntitys) {

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());
		if (obj.get("id") == null) {
			for (CatalogCategoryEntity categoryEntity : catalogCategoryEntitys) {
				if (categoryEntity.getParentCategoryId() == null) {
					obj = JSONObject.fromObject(categoryEntity, jsonConfig);
					JSONArray childs = new JSONArray();
					obj.put("childs", childs);
					this.getCategoryByCategoryList(obj, catalogCategoryEntitys);
				}
			}
		} else {
			for (CatalogCategoryEntity categoryEntity : catalogCategoryEntitys) {
				if (categoryEntity.getParentCategoryId() != null
						&& obj.getInt("id") == categoryEntity.getParentCategoryId()) {
					JSONObject objChild = JSONObject.fromObject(categoryEntity, jsonConfig);
					JSONArray childsObj = new JSONArray();
					objChild.put("childs", childsObj);
					JSONArray childs = obj.getJSONArray("childs");
					childs.add(objChild);
					obj.put("childs", childs);
					this.getCategoryByCategoryList(obj.getJSONArray("childs").getJSONObject(childs.size() - 1),
							catalogCategoryEntitys);
				}
			}
		}
		return obj;
	}

	/**
	 * 商品分类列表paramJson field :name/price/new
	 * 
	 * @param paramJson =
	 *                  "{storeId:1,categoryId:2,languageId:1,currencyId:1,pageIndex:1,orderBy:{order:'name',dir:'desc'}}";
	 * @return
	 */
	@Override
	public String getCategoryProductInfoByParamJson(String paramJson) {

		logger.info("paramJson:" + paramJson);
		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if (!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))) {
			return resultObj.toString();
		}
		int categoryId = paramObj.getInt("categoryId");
		int storeId = paramObj.getInt("storeId");
		int languageId = paramObj.getInt("languageId");
		int pageIndex = paramObj.getInt("pageIndex");
		int currencyId = paramObj.getInt("currencyId");
		int pageSize = VankleConstants.VANKLE_PAGE_SIZE;
		int offset = pageSize * (pageIndex - 1);

		CatalogCategoryEntity catalogCategoryEntity = catalogCategoryEntityMapper
				.findCatalogCategoryEntityById(categoryId, languageId);
		if (catalogCategoryEntity == null) {
			return resultObj.toString();
		}
		if (catalogCategoryEntity.getShowType() == 2) {
			PagerUtil pagerUtil = new PagerUtil(pageIndex, 0, pageSize);
			JSONObject dataObj = new JSONObject();
			JSONArray dataList = new JSONArray();
			dataObj.put("showType", catalogCategoryEntity.getShowType());
			dataObj.put("dataList", dataList);
			dataObj.put("prePageIndex", pagerUtil.previous());
			dataObj.put("curPageIndex", pageIndex);
			dataObj.put("nextPageIndex", pagerUtil.next());
			dataObj.put("pageSize", pageSize);
			dataObj.put("rowsCount", 0);
			dataObj.put("pageCount", pagerUtil.pageCount);

			dataObj.put("catalogCategoryEntity", catalogCategoryEntity);

			resultObj.put("data", dataObj);
			return this.getCategoryProductByCurrencyId(resultObj.toString(), currencyId);
		}

		List<CatalogCategoryProduct> adpProductList = new ArrayList<CatalogCategoryProduct>();
		Map<String, String> spuMap = new HashMap<String, String>();
		String adp = "";
		if (paramObj.get("adp") != null && !paramObj.getString("adp").equals("null")) {
			adp = paramObj.get("adp") + "";
			String[] spus = adp.split(",");
			for (String spu : spus) {
				spuMap.put(spu, spu);
				List<CatalogCategoryProduct> spuProductList = catalogCategoryProductMapper
						.findCatalogCategoryProductListBySpu(spu, storeId, languageId);
				for (CatalogCategoryProduct catalogCategoryProduct : spuProductList) {
					adpProductList.add(catalogCategoryProduct);
				}
			}
		}

		String countryId = "us";
		if (paramObj.get("prefixion") != null) {
			try {
				countryId = paramObj.getString("prefixion");
				if (countryId.split("-").length == 2) {
					countryId = countryId.split("-")[1];
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}

		String coutryIdsort = " ";
		if ("us".equals(countryId)) {
			coutryIdsort = " m.us  desc,";
		} else if ("fr".equals(countryId)) {
			coutryIdsort = " m.fr  desc,";
		} else if ("ca".equals(countryId)) {
			coutryIdsort = " m.ca  desc,";
		} else if ("gb".equals(countryId)) {
			coutryIdsort = " m.gb  desc,";
		} else if ("de".equals(countryId)) {
			coutryIdsort = " m.de  desc,";
		}

//		if(storeId!=1) {
//			coutryIdsort = "";
//		}
		String orderBy = "";
		// 2023-08-02 KK网站的默认排序改成按新品排倒序
		if (storeId == 4) {
			if (StringUtils.isNotBlank(coutryIdsort)) {
				orderBy = "order by m.createTime DESC, " + coutryIdsort;
			} else {
				orderBy = "order by m.createTime DESC ";
			}
		} else {
			orderBy = "order by " + coutryIdsort + " m.score  desc ";
		}

		if (paramObj.get("orderBy") != null) {
			JSONObject orderObject = paramObj.getJSONObject("orderBy");
			if ("Recommend".equalsIgnoreCase(orderObject.getString("order"))) {
				// if("desc".equalsIgnoreCase(orderObject.getString("dir"))){
				// orderBy = " order by " + coutryIdsort + " m.score  desc ";
//				}else{
//					orderBy = " order by  m.name  asc ";
//				}
				// 2023-08-02 KK网站的默认排序改成按新品排倒序
				if (storeId == 4) {
					orderBy = "order by m.createTime DESC, " + coutryIdsort + " m.score  desc ";
				} else {
					orderBy = " order by " + coutryIdsort + " m.score  desc ";
				}

			} else if ("price".equalsIgnoreCase(orderObject.getString("order"))) {
				if ("desc".equalsIgnoreCase(orderObject.getString("dir"))) {
					orderBy = " order by  m.discountAmount  desc ";
				} else {
					orderBy = " order by  m.discountAmount   asc ";
				}
			} else if ("new".equalsIgnoreCase(orderObject.getString("order"))) {
				if ("desc".equalsIgnoreCase(orderObject.getString("dir"))) {
					orderBy = " order by  m.sortDate  desc ";
				} else {
					orderBy = " order by  m.sortDate  asc ";
				}
			} else if ("name".equalsIgnoreCase(orderObject.getString("order"))) {
				if ("desc".equalsIgnoreCase(orderObject.getString("dir"))) {
					orderBy = " order by  m.name  desc ";
				} else {
					orderBy = " order by  m.name  asc ";
				}
			}

		}

		JSONObject dataObj = new JSONObject();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());

		// categoryId
		// CatalogCategoryEntity catalogCategoryEntity =
		// catalogCategoryEntityMapper.findCatalogCategoryEntityById(categoryId,
		// languageId);
		CatalogCategoryEntity catalogCategoryEntityBar = catalogCategoryEntityMapper
				.findCatalogCategoryEntityByLevel(storeId, languageId);
		dataObj.put("catalogCategoryEntity", catalogCategoryEntity);
		dataObj.put("sortBar", JSONArray.fromObject(catalogCategoryEntityBar.getOrderBy()));

//		String jsonCurrency = systemService.getCurrencyEntity(currencyId);
//		dataObj.put("currency", JSONObject.fromObject(jsonCurrency));

		int total = catalogCategoryProductMapper.findCatalogCategoryProductCount(storeId, categoryId);
		List<CatalogCategoryProduct> catalogCategoryProducts = catalogCategoryProductMapper
				.findCatalogCategoryProductList(storeId, categoryId, languageId, countryId, pageSize, offset, orderBy);

		for (CatalogCategoryProduct catalogCategoryProduct : catalogCategoryProducts) {
			if (spuMap.get(catalogCategoryProduct.getSpu() + "") == null) {

				adpProductList.add(catalogCategoryProduct);
			}
		}

		PagerUtil pagerUtil = new PagerUtil(pageIndex, total, pageSize);

		dataObj.put("showType", catalogCategoryEntity.getShowType());
		dataObj.put("dataList", JSONArray.fromObject(adpProductList, jsonConfig));
		dataObj.put("prePageIndex", pagerUtil.previous());
		dataObj.put("curPageIndex", pageIndex);
		dataObj.put("nextPageIndex", pagerUtil.next());
		dataObj.put("pageSize", pageSize);
		dataObj.put("rowsCount", total);
		dataObj.put("pageCount", pagerUtil.pageCount);

		resultObj.put("data", dataObj);
		logger.info("resultObj:" + resultObj.toString());
		return this.getCategoryProductByCurrencyId(resultObj.toString(), currencyId);
	}

	public String getCategoryProductByCurrencyId(String paramJson, int currencyId) {

		JSONObject resoutObj = JSONObject.fromObject(paramJson);
		JSONArray arrayObj = resoutObj.getJSONObject("data").getJSONArray("dataList");
		if (arrayObj == null || arrayObj.size() == 0) {
			return paramJson;
		}
		for (int i = 0; i < arrayObj.size(); i++) {
			JSONObject obj = arrayObj.getJSONObject(i);
			BigDecimal discountAmount = systemCurrencyService
					.getAmountByCurrencyId(new BigDecimal(obj.getString("discountAmount")), currencyId);

			obj.put("baseDiscountAmount", obj.getString("discountAmount"));
			obj.put("discountAmount", discountAmount);
			BigDecimal sellPrice = systemCurrencyService
					.getAmountByCurrencyId(new BigDecimal(obj.getString("sellPrice")), currencyId);
			obj.put("baseSellPrice", obj.getString("sellPrice"));
			obj.put("sellPrice", sellPrice);
		}
		return resoutObj.toString();
	}

	@Override
	public String getCategoryPositionProductInfoByParamJson(String paramJson) {

		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if (!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))) {
			return resultObj.toString();
		}
		int positionType = paramObj.getInt("positionType");
		int languageId = paramObj.getInt("languageId");
		int currencyId = paramObj.getInt("currencyId");
		int storeId = paramObj.getInt("storeId");

		List<CatalogCategoryProduct> adpProductList = new ArrayList<CatalogCategoryProduct>();

		String countryId = "";
		if (paramObj.get("prefixion") != null) {
			try {
				countryId = paramObj.getString("prefixion");
				if (countryId.split("-").length == 2) {
					countryId = countryId.split("-")[1];
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}

		JSONObject dataObj = new JSONObject();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());

		String jsonCurrency = systemService.getCurrencyEntity(currencyId);
		dataObj.put("currency", JSONObject.fromObject(jsonCurrency));

		List<CatalogCategoryProduct> catalogCategoryProducts = catalogCategoryProductMapper
				.findCatalogPositionProductList(languageId, positionType, storeId);

		JSONArray array = new JSONArray();
		for (CatalogCategoryProduct catalogCategoryProduct : catalogCategoryProducts) {
			JSONObject jsonObject = JSONObject.fromObject(catalogCategoryProduct, jsonConfig);
			List<CatalogProductSku> catalogProductSkuList = catalogProductSkuMapper
					.findCatalogProductSkuList(catalogCategoryProduct.getId(), "US");
			jsonObject.put("skuList", catalogProductSkuList);
			array.add(jsonObject);
		}
		dataObj.put("dataList", array);
		// dataObj.put("dataList", JSONArray.fromObject(
		// catalogCategoryProducts,jsonConfig));
		resultObj.put("data", dataObj);
		return this.getCategoryProductByCurrencyId(resultObj.toString(), currencyId);

	}

	@Override
	public String getCategorySiteMap(String paramJson) {

		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if (!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))) {
			return resultObj.toString();
		}
		int languageId = paramObj.getInt("languageId");
		int storeId = paramObj.getInt("storeId");
		List<CatalogCategoryEntity> catalogCategoryEntityList = catalogCategoryEntityMapper
				.findCatalogCategorySetMap(storeId, languageId);

		JSONObject dataObj = new JSONObject();
		dataObj.put("dataList", catalogCategoryEntityList);
		resultObj.put("data", dataObj);
		return resultObj.toString();
	}

}