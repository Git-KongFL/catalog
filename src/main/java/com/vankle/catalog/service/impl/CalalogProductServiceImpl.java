package com.vankle.catalog.service.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.vankle.cart.dao.CartRuleEntityMapper;
import com.vankle.cart.entity.CartRuleEntity;
import com.vankle.catalog.dao.CatalogCategoryEntityMapper;
import com.vankle.catalog.dao.CatalogCategoryProductMapper;
import com.vankle.catalog.dao.CatalogProductAttributeValueMapper;
import com.vankle.catalog.dao.CatalogProductBundledLinkMapper;
import com.vankle.catalog.dao.CatalogProductCustomizedAttributeMapper;
import com.vankle.catalog.dao.CatalogProductCustomizedPriceEntityMapper;
import com.vankle.catalog.dao.CatalogProductEntityDiscountMapper;
import com.vankle.catalog.dao.CatalogProductEntityImageMapper;
import com.vankle.catalog.dao.CatalogProductEntityLanguageMapper;
import com.vankle.catalog.dao.CatalogProductEntityMapper;
import com.vankle.catalog.dao.CatalogProductEntityVideoMapper;
import com.vankle.catalog.dao.CatalogProductGroupSellEntityMapper;
import com.vankle.catalog.dao.CatalogProductGroupSellLinkProductMapper;
import com.vankle.catalog.dao.CatalogProductRecommendedMapper;
import com.vankle.catalog.dao.CatalogProductSkuMapper;
import com.vankle.catalog.dao.CatalogProductSpecMapper;
import com.vankle.catalog.dao.CatalogProductSpecValueMapper;
import com.vankle.catalog.entity.CatalogCategoryEntity;
import com.vankle.catalog.entity.CatalogCategoryProduct;
import com.vankle.catalog.entity.CatalogProductAttributeValue;
import com.vankle.catalog.entity.CatalogProductCustomizedPriceEntity;
import com.vankle.catalog.entity.CatalogProductEntity;
import com.vankle.catalog.entity.CatalogProductEntityDiscount;
import com.vankle.catalog.entity.CatalogProductEntityImage;
import com.vankle.catalog.entity.CatalogProductEntityLanguage;
import com.vankle.catalog.entity.CatalogProductEntityVideo;
import com.vankle.catalog.entity.CatalogProductGroupSell;
import com.vankle.catalog.entity.CatalogProductGroupSellLinkProduct;
import com.vankle.catalog.entity.CatalogProductRecommended;
import com.vankle.catalog.entity.CatalogProductSku;
import com.vankle.catalog.entity.CatalogProductSpec;
import com.vankle.catalog.entity.CatalogProductSpecValue;
import com.vankle.catalog.service.CalalogProductService;
import com.vankle.code.annotation.LanguageAnnotation;
import com.vankle.code.constants.RedisConstants;
import com.vankle.code.constants.VankleConstants;
import com.vankle.code.util.JsonDateValueProcessor;
import com.vankle.code.util.JsonUtils;
import com.vankle.code.util.VankleUtils;
import com.vankle.system.service.SystemCurrencyService;
import com.vankle.system.service.SystemService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Service(group = "calalogProductService", version = "1.0")
public class CalalogProductServiceImpl implements CalalogProductService {

	@Autowired
	CatalogProductEntityLanguageMapper catalogProductEntityLanguageMapper;
	@Autowired
	private CatalogProductEntityMapper catalogProductEntityMapper;
	@Autowired
	private CatalogProductGroupSellEntityMapper catalogProductGroupSellEntityMapper;
	@Autowired
	CatalogProductGroupSellLinkProductMapper catalogProductGroupSellLinkProductMapper;
	@Autowired
	CatalogProductEntityImageMapper catalogProductEntityImageMapper;
	@Autowired
	CatalogProductBundledLinkMapper catalogProductBundledLinkMapper;
	@Autowired
	CatalogProductEntityDiscountMapper catalogProductEntityDiscountMapper;
	@Autowired
	CatalogProductSpecMapper catalogProductSpecMapper;
	@Autowired
	CatalogProductSpecValueMapper catalogProductSpecValueMapper;
	@Autowired
	CatalogProductEntityVideoMapper catalogProductEntityVideoMapper;
	@Autowired
	CatalogProductAttributeValueMapper catalogProductAttributeValueMapper;
	@Autowired
	CatalogProductSkuMapper catalogProductSkuMapper;
	@Autowired
	CatalogProductRecommendedMapper catalogProductRecommendedMapper;
	@Autowired
	SystemCurrencyService systemCurrencyService;

	@Autowired
	CatalogProductCustomizedPriceEntityMapper catalogProductCustomizedPriceEntityMapper;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	CatalogCategoryProductMapper catalogCategoryProductMapper;
	@Autowired
	CatalogCategoryEntityMapper catalogCategoryEntityMapper;
	@Autowired
	CatalogProductCustomizedAttributeMapper catalogProductCustomizedAttributeMapper;

	@Autowired
	CartRuleEntityMapper cartRuleEntityMapper;
	@Reference(group = "systemService", version = "1.0", timeout = 60000)
	private SystemService systemService;

	private final static Logger logger = LoggerFactory.getLogger(CalalogProductServiceImpl.class);

	@Override
	public String getServerStatus() {
		return "sussess";
	}

	@SuppressWarnings("unused")
	public String getCatalogProductInfoByItemId(String paramJson) {

		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if (!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))) {
			return resultObj.toString();
		}
		try {
//			int productId = paramObj.getInt("productId");
			int productId = paramObj.optInt("productId", 0);
			if (productId == 0) {
				// 为了查看传入了哪些非数字类型的productId参数
				String productIdStr = paramObj.getString("productId");
				logger.info("-----------productId为:" + productIdStr);
			}
			int storeId = paramObj.getInt("storeId");
			CatalogProductEntity catalogProductEntity = catalogProductEntityMapper
					.findCatalogProductEntityByItemId(productId, storeId);
			// 判断商品是否存在
			if (catalogProductEntity == null) {
				JsonUtils.modifyJSONObject(resultObj, VankleConstants.VANKLE_CODE_FAIL_10002,
						VankleConstants.VANKLE_CODE_FAIL_10002_MESSAGE).toString();
				return resultObj.toString();
			} else {
				productId = catalogProductEntity.getId();
			}
			paramObj.put("productId", productId);
			paramObj.put("type", catalogProductEntity.getType());
			String resultStr = this.getCatalogProductInfoByParamJson(paramObj.toString());
			if (catalogProductEntity.getProductCustomizedId() != null) {
				try {
					paramObj.put("productId", catalogProductEntity.getProductCustomizedId());
					logger.info("itemId:" + paramObj.toString());
					String resultCustomizedStr = this.getCatalogProductInfoByParamJson(paramObj.toString());
					JSONObject obj = JSONObject.fromObject(resultStr);
					obj.put("productCustomizedEntity", JSONObject.fromObject(resultCustomizedStr));
					return obj.toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return resultStr;
		} catch (Exception e) {
			logger.error(paramJson);
			e.printStackTrace();
		}
		return null;

	}

	/*
	 * @pram {productId:1,languageId:1,currencyId:1}
	 */
	public String getCatalogProductInfoByParamJson(String paramJson) {
		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if (!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))) {
			return resultObj.toString();
		}
		int productId = paramObj.getInt("productId");
		int languageId = paramObj.getInt("languageId");
		int currencyId = paramObj.getInt("currencyId");
		String countryId = "us";
		try {
			if (paramObj.get("prefixion") != null) {
				countryId = paramObj.getString("prefixion");
				if (countryId.split("-").length == 2) {
					countryId = countryId.split("-")[1];
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			// TODO: handle exception
		}
		Object requestType = paramObj.get("requestType");
		resultObj.put("requestType", requestType);
		String resultStr = this.getProductLanguageInfo(resultObj, productId, languageId, currencyId, countryId);

		if (!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))) {
			return resultObj.toString();
		} else {

			return this.getCurrencyProduct(resultStr, currencyId);
		}
	}

	/**
	 * 商品信息汇率转换
	 * 
	 * @param resultStr
	 * @param currencyId
	 * @param storeId
	 * @return
	 */
	public String getCurrencyProduct(String resultStr, int currencyId) {
		if (currencyId == 1) {
			// 1是美元，无需转换
			return resultStr;
		} else {
			JSONObject resultObj = JSONObject.fromObject(resultStr);
			JSONObject productObj = resultObj.getJSONObject("data");

//			logger.info("---------productObj的详细信息为：" + productObj.toString());

			String jsonCurrency = systemService.getCurrencyEntity(currencyId);
			productObj.put("currency", JSONObject.fromObject(jsonCurrency));
			// 商品信息价格换算
			BigDecimal originalPrice = systemCurrencyService
					.getAmountByCurrencyId(new BigDecimal(productObj.getString("originalPrice")), currencyId);
			productObj.put("originalPrice", originalPrice);
			BigDecimal sellPrice = systemCurrencyService
					.getAmountByCurrencyId(new BigDecimal(productObj.getString("sellPrice")), currencyId);
			productObj.put("baseSellPrice", productObj.getString("sellPrice"));
			productObj.put("sellPrice", sellPrice);
			// 母商品折扣价格
			BigDecimal parentDiscountPrice = systemCurrencyService
					.getAmountByCurrencyId(new BigDecimal(productObj.getString("parentDiscountPrice")), currencyId);
			productObj.put("parentDiscountPrice", parentDiscountPrice);

			String customizedDesc = productObj.getString("customizedDesc");
			if (customizedDesc.indexOf(",") >= 0) {
				String[] priceArr = customizedDesc.split(",");
				String customizedDescCur = "";
				for (String price : priceArr) {
					try {
						BigDecimal priceCur = systemCurrencyService.getAmountByCurrencyId(new BigDecimal(price),
								currencyId);
						customizedDescCur += priceCur + ",";
					} catch (Exception e) {
						customizedDescCur += "0,";
						e.printStackTrace();
						// TODO: handle exception
					}
				}
				if (customizedDescCur.indexOf(",") >= 0) {
					customizedDescCur = customizedDescCur.substring(0, customizedDescCur.length() - 1);
				}
				productObj.put("customizedDesc_cur", customizedDescCur);
			}
			// 商品折扣信息价格换算
			JSONObject catalogProductEntityDiscount = productObj.getJSONObject("catalogProductEntityDiscount");
			catalogProductEntityDiscount.put("baseDiscountAmount",
					catalogProductEntityDiscount.getString("discountAmount"));
			BigDecimal discountAmount = systemCurrencyService.getAmountByCurrencyId(
					new BigDecimal(catalogProductEntityDiscount.getString("discountAmount")), currencyId);
			catalogProductEntityDiscount.put("discountAmount", discountAmount);
			try {
//				JSONArray setList = productObj.getJSONArray("setList");
				JSONArray setList = productObj.optJSONArray("setList");
				if (setList != null && !setList.isEmpty()) {
					if (setList.getJSONObject(0).containsKey("value")) {
						JSONArray valueList = setList.getJSONObject(0).getJSONArray("value");
						for (int i = 0; i < valueList.size(); i++) {
							JSONObject oneObj = valueList.getJSONObject(i);
							BigDecimal onePrice = systemCurrencyService
									.getAmountByCurrencyId(new BigDecimal(oneObj.getString("price")), currencyId);
							valueList.getJSONObject(i).put("price", onePrice);

							if (oneObj.containsKey("value")) {
								JSONArray valueOneList = oneObj.getJSONArray("value");
								for (int n = 0; n < valueOneList.size(); n++) {
									JSONArray twoList = oneObj.getJSONArray("value").getJSONObject(n)
											.getJSONArray("value");
									for (int m = 0; m < twoList.size(); m++) {
										JSONObject towObj = twoList.getJSONObject(m);
										BigDecimal towPrice = systemCurrencyService.getAmountByCurrencyId(
												new BigDecimal(towObj.getString("price")), currencyId);
										towObj.put("price", towPrice);

									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return resultObj.toString();
		}
	}

	public String getProductLanguageInfo(JSONObject resultObj, int productId, int languageId, int currencyId,
			String countryId) {

		CatalogProductEntity catalogProductEntity = catalogProductEntityMapper.findCatalogProductEntity(productId);
		// 判断商品是否存在
		if (catalogProductEntity == null) {
			logger.error("productId" + productId);
			JsonUtils.modifyJSONObject(resultObj, VankleConstants.VANKLE_CODE_FAIL_10002,
					VankleConstants.VANKLE_CODE_FAIL_10002_MESSAGE).toString();
			return resultObj.toString();
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());
		JSONObject jsonProduct = JSONObject.fromObject(catalogProductEntity, jsonConfig);
		jsonProduct.put("requestType", resultObj.get("requestType"));

		String jsonCurrency = systemService.getCurrencyEntity(currencyId);
		jsonProduct.put("currency", JSONObject.fromObject(jsonCurrency));

		// 添加折扣信息
		this.addCatalogProductDiscount(jsonProduct, jsonConfig);
		// 添加商品分类
		this.addCatalogProductCategory(jsonProduct, jsonConfig, languageId);
		// 添加商品规格
		this.addCatalogProductSpec(jsonProduct, jsonConfig, countryId);
		// 添加商品图片
		this.addCatalogProductImage(jsonProduct, jsonConfig);
		// 添加商品视频
		this.addCatalogProductVideo(jsonProduct, jsonConfig);
		// 添加捆绑销售资料
		this.addCatalogProductGroupSell(jsonProduct, jsonConfig, languageId, currencyId, countryId);
		// 添加商品推荐商品
		this.addCatalogProductRecommended(jsonProduct, jsonConfig, languageId, currencyId, countryId);

		if (catalogProductEntity.getCustomizedType() != null && catalogProductEntity.getCustomizedType() == 5) {
			if (catalogProductEntity.getStoreId() == 10) {
				this.addCatalogProductCustomizeAttributeValueGP(catalogProductEntity, jsonProduct, jsonConfig,
						languageId);
			}
		}
		if (catalogProductEntity.getType() == 5) {
			this.addCatalogProductCustomizeAttributeValue(catalogProductEntity, jsonProduct, jsonConfig, languageId);
		}

		// 添加商品自定义属性
		// this.addCatalogProductAttributeValue(jsonProduct, jsonConfig);
		jsonProduct.put("id", jsonProduct.getInt("itemId"));
		if (languageId == 1) {

			String name = jsonProduct.getString("name").replaceAll("[\\t\\n\\r]", "");
			jsonProduct.put("name", name);
			resultObj.put("data", jsonProduct);
			return resultObj.toString();
		} else {
			this.getLanguageByJosnProduct(jsonProduct, productId, languageId);
			resultObj.put("data", jsonProduct);
			return resultObj.toString();
		}

	}

	/**
	 * 英语商品信息 翻译对应的语言
	 * 
	 * @param resultObj productId languageId
	 */
	public void getLanguageByJosnProduct(JSONObject productObj, int productId, int languageId) {

		CatalogProductEntityLanguage catalogProductEntityLanguage = catalogProductEntityLanguageMapper
				.findCatalogProductEntityLanguage(productId, languageId);
		JSONObject obj = JSONObject.fromObject(catalogProductEntityLanguage);
		obj.remove("id");
		obj.remove("productId");
		obj.remove("spu");
		obj.remove("storeId");
		obj.remove("languageId");
		obj.remove("customizedType");
		obj.remove("customizedNumber");
		obj.remove("customizedNumberDataDesc");
		obj.remove("customizedNumberFontDesc");
		obj.remove("customizedInfo");
		obj.remove("customizedDesc");

		String name = obj.getString("name").replaceAll("[\\t\\n\\r]", "");
		obj.put("name", name);
		Iterator iterator = obj.keys();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = obj.getString(key);
			productObj.put(key, value);
		}

	}

	/**
	 * 添加商
	 * 
	 * @param jsonProduct
	 */
	public void addCatalogProductCategory(JSONObject jsonProduct, JsonConfig config, int languageId) {

		List<CatalogCategoryProduct> catalogProductList = catalogCategoryProductMapper
				.findCatalogCategoryProductListByProduct(jsonProduct.getInt("id"));

		jsonProduct.put("categoryIds", "");
		String categoryIds = "";
		JSONArray cartRuleEntitys = new JSONArray();
		for (CatalogCategoryProduct catalogCategoryProduct : catalogProductList) {
			List<CartRuleEntity> cartRuleEntityList = cartRuleEntityMapper.findCartRuleEntityByStoreIdAndCategoryId(
					jsonProduct.getInt("storeId"), catalogCategoryProduct.getCategoryId());
			for (CartRuleEntity cartRuleEntity : cartRuleEntityList) {
				cartRuleEntitys.add(cartRuleEntity);
			}
//			if(cartRuleEntity != null) {
//				cartRuleEntitys.add(cartRuleEntity);
//			}
			categoryIds += catalogCategoryProduct.getCategoryId() + ",";
		}
		jsonProduct.put("cartRuleEntitys", cartRuleEntitys);
		jsonProduct.put("categoryIds", categoryIds);
		jsonProduct.put("category", "");
		String category = "";
		for (CatalogCategoryProduct catalogCategoryProduct : catalogProductList) {
			CatalogCategoryEntity categoryEntity = catalogCategoryEntityMapper
					.findCatalogCategoryEntityById(catalogCategoryProduct.getCategoryId(), languageId);
			if (categoryEntity != null) {
				if (categoryEntity.getLevel() == 2) {
					category = categoryEntity.getName();
				} else if (categoryEntity.getLevel() == 3) {
					CatalogCategoryEntity categoryEntity2 = catalogCategoryEntityMapper
							.findCatalogCategoryEntityById(categoryEntity.getParentCategoryId(), languageId);
					if (categoryEntity2 != null)
						category = categoryEntity2.getName() + "/" + categoryEntity.getName();
					else
						category = categoryEntity.getName();
				}

				jsonProduct.put("category", category);

			}
			if (!"".equals(category)) {
				break;
			}
		}
	}

	/**
	 * 添加商品折扣
	 * 
	 * @param jsonProduct
	 */
	public void addCatalogProductDiscount(JSONObject jsonProduct, JsonConfig config) {
		CatalogProductEntityDiscount catalogProductEntityDiscount = catalogProductEntityDiscountMapper
				.findCatalogProductEntityDiscount(jsonProduct.getInt("id"));
		jsonProduct.put("catalogProductEntityDiscount", JSONObject.fromObject(catalogProductEntityDiscount, config));
	}

	/**
	 * 添加商品SKU
	 * 
	 * @param jsonProduct
	 * @param config
	 */
	public void addCatalogProductSku(JSONObject jsonProduct, JsonConfig config, String countryId) {
		List<CatalogProductSku> catalogProductSkus = catalogProductSkuMapper
				.findCatalogProductSkuList(jsonProduct.getInt("id"), countryId);
		JSONArray jsonCatalogProductSkus = new JSONArray();
		for (CatalogProductSku catalogProductSku : catalogProductSkus) {
			if (catalogProductSku.getStatus() == 1)
				jsonCatalogProductSkus.add(catalogProductSku);
		}
		// JSONArray jsonCatalogProductSkus = JSONArray.fromObject(catalogProductSkus);
		jsonProduct.put("catalogProductSkus", jsonCatalogProductSkus);
	}

	/**
	 * 添加商品自定义属性
	 * 
	 * @param jsonProduct
	 */
	public void addCatalogProductCustomizeAttributeValueGP(CatalogProductEntity productEntity, JSONObject jsonProduct,
			JsonConfig config, int languageId) {

		List<CatalogProductAttributeValue> catalogProductAttributeValues = catalogProductAttributeValueMapper
				.findCatalogProductAttributeValue(jsonProduct.getInt("id"));
		JSONArray jsonCatalogProductAttributeValues = JSONArray.fromObject(catalogProductAttributeValues);
		jsonProduct.put("catalogProductAttributeValues", jsonCatalogProductAttributeValues);
		JSONArray set_list = new JSONArray();

		List<Map<String, Object>> catalogProductCustomizedAttributeList = catalogProductCustomizedAttributeMapper
				.findCatalogProductcustomizedAttributListByProductId(productEntity.getId());
		for (Map<String, Object> customizedAttributeMap : catalogProductCustomizedAttributeList) {
			JSONObject obj = new JSONObject();
			obj.put("key", customizedAttributeMap.get("short_name"));
			obj.put("name", customizedAttributeMap.get("name"));
			obj.put("nameFr", customizedAttributeMap.get("name_fr"));
			obj.put("sort", customizedAttributeMap.get("sort_number"));
			JSONArray valueArr = JSONArray.fromObject(customizedAttributeMap.get("json_service_string"));
			obj.put("value", valueArr);
			set_list.add(obj);
		}
		jsonProduct.put("setList", set_list);
	}

	/**
	 * 添加商品自定义属性
	 * 
	 * @param jsonProduct
	 */
	public void addCatalogProductCustomizeAttributeValue(CatalogProductEntity productEntity, JSONObject jsonProduct,
			JsonConfig config, int languageId) {

		int productId = productEntity.getId();
//		logger.error(":productEntity.getType()" + productEntity.getType());
		if (productEntity.getType() == 5) {
			String[] spuStrings = productEntity.getSpu().split("_");
			try {
				productId = jdbcTemplate.queryForObject("select id from  catalog_product_entity where store_id = "
						+ productEntity.getStoreId() + " and spu = '" + spuStrings[0] + "'", Integer.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		List<CatalogProductAttributeValue> catalogProductAttributeValues = catalogProductAttributeValueMapper
				.findCatalogProductAttributeValue(jsonProduct.getInt("id"));
		JSONArray jsonCatalogProductAttributeValues = JSONArray.fromObject(catalogProductAttributeValues);
		jsonProduct.put("catalogProductAttributeValues", jsonCatalogProductAttributeValues);
		JSONArray setList = new JSONArray();

		List<Map<String, Object>> catalogProductCustomizedAttributeList = catalogProductCustomizedAttributeMapper
				.findCatalogProductcustomizedAttributListByProductId(productId);

		for (Map<String, Object> customizedAttributeMap : catalogProductCustomizedAttributeList) {
			JSONObject obj = new JSONObject();
			obj.put("key", customizedAttributeMap.get("short_name"));
			obj.put("name", customizedAttributeMap.get("name"));
			obj.put("nameFr", customizedAttributeMap.get("name_fr"));
			obj.put("sort", customizedAttributeMap.get("sort_number"));
			JSONArray valueArr = JSONArray.fromObject(customizedAttributeMap.get("json_service_string"));
//			logger.info(valueArr.toString());
			JSONArray filterArr = new JSONArray();
			for (int i = 0; i < valueArr.size(); i++) {
				JSONObject oneObj = valueArr.getJSONObject(i);
				if (this.customizedAttributeExit(productEntity.getSpu2(), oneObj)) {
					filterArr.add(oneObj);
				}
			}
			obj.put("value", filterArr);
			setList.add(obj);
		}

		// 如果虚拟商品调整了价格，则使用调整后的价格
		this.updateCustomizedPrice(setList, productEntity);

		jsonProduct.put("setList", setList);
		String spu = productEntity.getSpu().split("_")[0];

		List<Map<String, Object>> customizedList = jdbcTemplate
				.queryForList("select item_id,spu2 from " + " catalog_product_entity where store_id = "
						+ productEntity.getStoreId() + " and status = 1 and spu like  '" + spu + "_%'");
		jsonProduct.put("setListKey", customizedList);

	}

	public void getCustomizedNewPriceObj(JSONObject newPriceObj, String jsonStr, String attriblueteShortName) {
		try {
			if (jsonStr != null && !("".equals(jsonStr))) {
				JSONObject jsonObject = JSONObject.fromObject(jsonStr);
				newPriceObj.put(attriblueteShortName + "-" + jsonObject.getString("key"),
						jsonObject.getString("newPrice"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateCustomizedPrice(JSONArray setList, CatalogProductEntity productEntity) {

		String spu = productEntity.getSpu().split("_")[0];
		List<CatalogProductCustomizedPriceEntity> catalogProductCustomizedPriceList = catalogProductCustomizedPriceEntityMapper
				.findCatalogProductCustomizedPriceEntityList(spu);
		JSONObject newPriceObj = new JSONObject();
		for (CatalogProductCustomizedPriceEntity catalogProductCustomizedPriceEntity : catalogProductCustomizedPriceList) {
			this.getCustomizedNewPriceObj(newPriceObj, catalogProductCustomizedPriceEntity.getZss(), "ZSS");
			this.getCustomizedNewPriceObj(newPriceObj, catalogProductCustomizedPriceEntity.getZscw(), "ZSCW");
			this.getCustomizedNewPriceObj(newPriceObj, catalogProductCustomizedPriceEntity.getMmc(), "MMC");
			this.getCustomizedNewPriceObj(newPriceObj, catalogProductCustomizedPriceEntity.getMmct(), "MMCT");
			this.getCustomizedNewPriceObj(newPriceObj, catalogProductCustomizedPriceEntity.getOscs(), "OSCS");
			this.getCustomizedNewPriceObj(newPriceObj, catalogProductCustomizedPriceEntity.getMsmc(), "MSMC");
			this.getCustomizedNewPriceObj(newPriceObj, catalogProductCustomizedPriceEntity.getSsmc(), "SSMC");
		}

		if (setList.getJSONObject(0).containsKey("value")) {
			JSONArray valueList = setList.getJSONObject(0).getJSONArray("value");
			String attriblueteShortName = setList.getJSONObject(0).getString("key");
			for (int i = 0; i < valueList.size(); i++) {
				JSONObject oneObj = valueList.getJSONObject(i);
				BigDecimal onePrice = new BigDecimal(oneObj.getString("price"));
				String valueName = oneObj.getString("key");
				String newPriceKey = attriblueteShortName + "-" + valueName;
				if (newPriceObj.containsKey(newPriceKey)) {
					onePrice = new BigDecimal(newPriceObj.getString(newPriceKey));
				}
				valueList.getJSONObject(i).put("price", onePrice);
				if (oneObj.containsKey("value")) {

					JSONArray valueOneList = oneObj.getJSONArray("value");
					for (int n = 0; n < valueOneList.size(); n++) {
						JSONArray twoList = oneObj.getJSONArray("value").getJSONObject(n).getJSONArray("value");
						String attriblueteShortNameTwo = oneObj.getJSONArray("value").getJSONObject(n).getString("key");
						for (int m = 0; m < twoList.size(); m++) {
							JSONObject towObj = twoList.getJSONObject(m);
							BigDecimal towPrice = new BigDecimal(towObj.getString("price"));
							String valueNameTwo = towObj.getString("key");
							String newPriceKeyTwo = attriblueteShortNameTwo + "-" + valueNameTwo;
							if (newPriceObj.containsKey(newPriceKeyTwo)) {
								towPrice = new BigDecimal(newPriceObj.getString(newPriceKeyTwo));
							}
							towObj.put("price", towPrice);

						}
					}
				}
			}
		}
	}

	public boolean customizedAttributeExit(String spu2, JSONObject obj) {
//		logger.info(spu2);
//		logger.info(obj.toString());
		Boolean b = false;
		String spu = spu2;
		String[] spuStrings = spu.split("\\|\\|");// TEST0013||MMC-bps_MSMC-whitestone_SDST-moissanitewhite
		String arr = "";

		if (spuStrings.length == 2) {
			arr = spuStrings[1];// MMC-bps_MSMC-whitestone_SDST-moissanitewhite
		} else {
			arr = spuStrings[0];// TEST0013
		}

		String[] attArr = arr.split("_");
		if (attArr.length == 1) {
			// 只有一个大属性:TEST0013||MMC-bps
			b = true;
		}

		if (attArr.length == 2) {
			// 存在两个大属性:TEST0013||MMC-bps_MSMC-whitestone
			try {
				JSONObject twoArrObj = obj.getJSONArray("value").getJSONObject(0);
				String towKey = twoArrObj.getString("key");
				JSONArray twoObjValueArr = twoArrObj.getJSONArray("value");
				for (int i = 0; i < twoObjValueArr.size(); i++) {
					JSONObject twoObjValue = twoObjValueArr.getJSONObject(i);
					String towKeyValue = twoObjValue.getString("key");
					if (attArr[1].equals(towKey + "-" + towKeyValue)) {
						// attArr[1]:MSMC-whitestone
						b = true;
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}

		// 无需判断第一个大属性，因为第一个大属性是必选的，肯定存在

		if (attArr.length == 3) {
			// 存在三个大属性：TEST0013||MMC-bps_MSMC-whitestone_SDST-moissanitewhite
			try {
				// 第二个大属性
				JSONObject twoArrObj = obj.getJSONArray("value").getJSONObject(0);
				String towKey = twoArrObj.getString("key");
				Boolean b_twoKey = false;
				JSONArray twoObjValueArr = twoArrObj.getJSONArray("value");
				for (int i = 0; i < twoObjValueArr.size(); i++) {
					JSONObject twoObjValue = twoObjValueArr.getJSONObject(i);
					String towKeyValue = twoObjValue.getString("key");
					if (attArr[1].equals(towKey + "-" + towKeyValue)) {
						b_twoKey = true;
						break;
					}
				}

				// 第三个大属性
				JSONObject threeArrObj = obj.getJSONArray("value").getJSONObject(1);
				String threeKey = threeArrObj.getString("key");
				Boolean b_threeKey = false;
				JSONArray threeObjValueArr = threeArrObj.getJSONArray("value");
				for (int i = 0; i < threeObjValueArr.size(); i++) {
					JSONObject threeObjValue = threeObjValueArr.getJSONObject(i);
					String threeKeyValue = threeObjValue.getString("key");
					if (attArr[2].equals(threeKey + "-" + threeKeyValue)) {
						b_threeKey = true;
						break;
					}
				}

				if (b_twoKey && b_threeKey) {
					b = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}

		return b;
	}

	/**
	 * 添加商品自定义属性
	 * 
	 * @param jsonProduct
	 */
	public void addCatalogProductAttributeValue(JSONObject jsonProduct, JsonConfig config) {

		List<CatalogProductAttributeValue> catalogProductAttributeValues = catalogProductAttributeValueMapper
				.findCatalogProductAttributeValue(jsonProduct.getInt("id"));
		JSONArray jsonCatalogProductAttributeValues = JSONArray.fromObject(catalogProductAttributeValues);
		jsonProduct.put("catalogProductAttributeValues", jsonCatalogProductAttributeValues);
	}

	/**
	 * 添加商品视频
	 * 
	 * @param jsonProduct
	 */
	public void addCatalogProductVideo(JSONObject jsonProduct, JsonConfig config) {
		List<CatalogProductEntityVideo> catalogProductEntityVideos = catalogProductEntityVideoMapper
				.findCatalogProductEntityVideoList(jsonProduct.getInt("id"));
		JSONArray jsonCatalogProductEntityVideos = JSONArray.fromObject(catalogProductEntityVideos);
		jsonProduct.put("catalogProductEntityVideos", jsonCatalogProductEntityVideos);
	}

	/**
	 * 添加商品图片
	 * 
	 * @param jsonProduct
	 */
	public void addCatalogProductImage(JSONObject jsonProduct, JsonConfig config) {
		List<CatalogProductEntityImage> minCatalogProductEntityImages = catalogProductEntityImageMapper
				.findCatalogProductEntityImageList(jsonProduct.getInt("id"));
		JSONArray jsonCatalogProductEntityImages = JSONArray.fromObject(minCatalogProductEntityImages);
		jsonProduct.put("catalogProductEntityImages", jsonCatalogProductEntityImages);
	}

	/**
	 * 添加商品规格
	 * 
	 * @param jsonProduct
	 */
	public void addCatalogProductSpec(JSONObject jsonProduct, JsonConfig config, String countryId) {

		List<CatalogProductSku> catalogProductSkus = catalogProductSkuMapper
				.findCatalogProductSkuList(jsonProduct.getInt("id"), countryId);

		List<CatalogProductSpec> catalogProductSpecList = catalogProductSpecMapper
				.findCatalogProductSpecList(jsonProduct.getInt("id"));
		JSONArray jsonArrSpec = new JSONArray();
		for (CatalogProductSpec catalogProductSpec : catalogProductSpecList) {
			List<CatalogProductSpecValue> catalogProductSpecValues = catalogProductSpecValueMapper
					.findCatalogProductSpecValueList(catalogProductSpec.getId(), countryId);
			JSONObject jsonCatalogProductSpec = JSONObject.fromObject(catalogProductSpec); // 商品规格
			JSONArray jsonArrCatalogProductSpecValue = JSONArray.fromObject(catalogProductSpecValues);// 商品规格值
			JSONArray jsonArrCatalogProductSpecValueNew = new JSONArray();
			for (int i = 0; i < jsonArrCatalogProductSpecValue.size(); i++) {
				JSONObject specVaue = jsonArrCatalogProductSpecValue.getJSONObject(i);
				specVaue.put("description", "");
				for (CatalogProductSku catalogProductSku : catalogProductSkus) {
					if (catalogProductSku.getSkuName().equals(specVaue.getString("name"))) {
						if (catalogProductSku.getDescription() != null) {
							specVaue.put("description", catalogProductSku.getDescription());
						} else {
							specVaue.put("description", "");
						}
						specVaue.put("status", catalogProductSku.getStatus());
						break;
					}
				}
				if (specVaue.get("status") != null && "1".equals(specVaue.getString("status"))) {
					jsonArrCatalogProductSpecValueNew.add(specVaue);
				}

			}
			jsonCatalogProductSpec.put("catalogProductSpecValueList", jsonArrCatalogProductSpecValueNew);// 商品规格 添加商品规格值
			// jsonCatalogProductSpec.put("catalogProductSpecValueList",
			// jsonArrCatalogProductSpecValue);//商品规格 添加商品规格值
			jsonArrSpec.add(jsonCatalogProductSpec);// 商品规格组
		}
		jsonProduct.put("catalogProductSpecList", jsonArrSpec);

		List<CatalogProductSku> catalogProductSkuList = new ArrayList<CatalogProductSku>();

		for (CatalogProductSku catalogProductSku : catalogProductSkus) {
			if (catalogProductSku.getStatus() == 1) {
				catalogProductSkuList.add(catalogProductSku);
			}
		}

		jsonProduct.put("catalogProductSkuList", catalogProductSkuList);

	}

	/**
	 * 添加捆绑销售资料
	 * 
	 * @param JSONObject
	 */
	public void addCatalogProductGroupSell(JSONObject jsonProduct, JsonConfig config, int languageId, int currencyId,
			String countryId) {
		// logger.info("addCatalogProductGroupSell:"+jsonProduct.toString());
		if (jsonProduct.getInt("type") == 1) {
			return;
		}

		if (jsonProduct.get("requestType") != null) {
			return;
		}

		jsonProduct.put("catalogProductGroupSell", new JSONObject());
		// 判断是捆绑销售groupSellId
		if (jsonProduct.get("groupSellId") != null) {
			int groupSellId = jsonProduct.getInt("groupSellId");
			CatalogProductGroupSell cGroupSellEntity = catalogProductGroupSellEntityMapper
					.findCatalogProductGroupSellEntity(groupSellId);
			if (cGroupSellEntity != null) {
				JSONObject jsonCatalogProductGroupSell = JSONObject.fromObject(cGroupSellEntity);
				List<CatalogProductGroupSellLinkProduct> groupLinkProductList = catalogProductGroupSellLinkProductMapper
						.findCatalogProductGroupSellLinkProductList(groupSellId);
				JSONArray jsonArr = new JSONArray();
				for (CatalogProductGroupSellLinkProduct cSellLinkProduct : groupLinkProductList) {
					CatalogProductEntity catalogProductEntity = catalogProductEntityMapper
							.findCatalogProductEntity(cSellLinkProduct.getProductId());
					if (catalogProductEntity != null) {
						if (catalogProductEntity.getType() == 1 || catalogProductEntity.getType() == 3) {
							JSONObject paramObj = new JSONObject();
							paramObj.put("productId", cSellLinkProduct.getProductId());
							paramObj.put("languageId", languageId);
							paramObj.put("currencyId", currencyId);
							paramObj.put("prefixion", countryId);
							paramObj.put("requestType", 1);
							String resout = this.getCatalogProductInfoByParamJson(paramObj.toString());
							JSONObject resoutJson = JSONObject.fromObject(resout);
							jsonArr.add(resoutJson.get("data"));
						}
					}
				}
				jsonCatalogProductGroupSell.put("list", jsonArr);
				jsonProduct.put("catalogProductGroupSell", jsonCatalogProductGroupSell);
			}
		}
	}

	/**
	 * 添加推荐商品
	 * 
	 * @param JSONObject
	 */
	public void addCatalogProductRecommended(JSONObject jsonProduct, JsonConfig config, int languageId, int currencyId,
			String countryId) {
		List<CatalogProductRecommended> catalogProductRecommendeds = catalogProductRecommendedMapper
				.findCatalogProductRecommendedList(jsonProduct.getInt("id"), languageId);
		JSONArray recommendArray = new JSONArray();
		for (CatalogProductRecommended catalogProductRecommended : catalogProductRecommendeds) {

			BigDecimal sellPrice = systemCurrencyService.getAmountByCurrencyId(catalogProductRecommended.getSellPrice(),
					currencyId);
			BigDecimal discountAmount = systemCurrencyService
					.getAmountByCurrencyId(catalogProductRecommended.getDiscountAmount(), currencyId);
			JSONObject obj = JSONObject.fromObject(catalogProductRecommended);
			obj.put("spu", catalogProductRecommended.getSpu());
			obj.put("recommendType", "manual");
			obj.put("customizedType", catalogProductRecommended.getCustomizedType());
			if (catalogProductRecommended.getSpu().split("\\|\\|").length == 2) {
				obj.put("spu_customized", catalogProductRecommended.getSpu().split("\\|\\|")[1]);
			} else {
				obj.put("spu_customized", catalogProductRecommended.getSpu().split("\\|\\|")[0]);
			}

			obj.put("item_id", catalogProductRecommended.getId());
			obj.put("remoteUrl", catalogProductRecommended.getSmallImage());
			obj.put("listProductTitle", catalogProductRecommended.getListProductTitle());
			obj.put("sellPrice", sellPrice);
			obj.put("discountAmount", discountAmount);
			recommendArray.add(obj);
		}

		jsonProduct.put("catalogProductRecommendeds", recommendArray);
	}

	public static void main(String[] args) {
		String jsonStr = "   {\n" + "    \"nick\": \"Round Cut\",\n" + "    \"nickFr\": \"Rond\",\n"
				+ "    \"price\": 0.00,\n"
				+ "    \"imageUrl\": \"https://media.vancaro.com/customized/images/round-20210518.jpg?x-oss-process=image/format,webp\",\n"
				+ "    \"sortNumber\": 1,\n" + "    \"name\": \"Round\",\n" + "    \"nameFr\": \"Rond\",\n"
				+ "    \"value\": [\n" + "      {\n" + "        \"name\": \"CARAT WEIGHT\",\n"
				+ "        \"nameFr\": \"CARATAGE\",\n" + "        \"sort\": 2,\n" + "        \"value\": [\n"
				+ "          {\n" + "            \"nick\": \"1 CT\",\n" + "            \"nickFr\": \"1 CT\",\n"
				+ "            \"price\": 30.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 2,\n" + "            \"name\": \"1 Carat\",\n"
				+ "            \"nameFr\": \"1 Carat\",\n" + "            \"key\": \"1ct\"\n" + "          },\n"
				+ "          {\n" + "            \"nick\": \"1.2 CT\",\n" + "            \"nickFr\": \"1.2 CT\",\n"
				+ "            \"price\": 50.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1.2carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 3,\n" + "            \"name\": \"1.2 Carat\",\n"
				+ "            \"nameFr\": \"1.2 Carat\",\n" + "            \"key\": \"12ct\"\n" + "          },\n"
				+ "          {\n" + "            \"nick\": \"1.5 CT\",\n" + "            \"nickFr\": \"1.5 CT\",\n"
				+ "            \"price\": 60.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1.5carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 4,\n" + "            \"name\": \"1.5 Carat\",\n"
				+ "            \"nameFr\": \"1.5 Carat\",\n" + "            \"key\": \"15ct\"\n" + "          }\n"
				+ "        ],\n" + "        \"key\": \"ZSCW\"\n" + "      },\n" + "      {\n"
				+ "        \"name\": \"METAL\",\n" + "        \"nameFr\": \"MÉTAL\",\n" + "        \"sort\": 3,\n"
				+ "        \"value\": [\n" + "          {\n" + "            \"nick\": \"Silver\",\n"
				+ "            \"nickFr\": \"Argent\",\n" + "            \"price\": 40.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/925silver-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 1,\n" + "            \"name\": \"925 Sterling Silver\",\n"
				+ "            \"nameFr\": \"Argent Sterling 925\",\n" + "            \"key\": \"925silver\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose\",\n" + "            \"price\": 65.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/rgps-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 2,\n" + "            \"name\": \"Rose Gold Plated Silver\",\n"
				+ "            \"nameFr\": \"Argent Plaqué Or Rose\",\n" + "            \"key\": \"rgps\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune\",\n" + "            \"price\": 65.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/ygps-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 3,\n" + "            \"name\": \"Yellow Gold Plated Silver\",\n"
				+ "            \"nameFr\": \"Argent Plaqué Or Jaune\",\n" + "            \"key\": \"ygps\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K White Gold\",\n"
				+ "            \"nickFr\": \"Or Blanc 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14k-white-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 5,\n" + "            \"name\": \"14K White Gold\",\n"
				+ "            \"nameFr\": \"Or Blanc 14 Carats\",\n" + "            \"key\": \"14kwhitegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14k-rose-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 6,\n" + "            \"name\": \"14K Rose Gold\",\n"
				+ "            \"nameFr\": \"Or Rose 14 Carats\",\n" + "            \"key\": \"14krosegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14kgold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 7,\n" + "            \"name\": \"14K Yellow Gold\",\n"
				+ "            \"nameFr\": \"Or Jaune 14 Carats\",\n" + "            \"key\": \"14kyellowgold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K White Gold\",\n"
				+ "            \"nickFr\": \"Or Blanc 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18k-white-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 8,\n" + "            \"name\": \"18K White Gold\",\n"
				+ "            \"nameFr\": \"Or Blanc 18 Carats\",\n" + "            \"key\": \"18kwhitegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18k-rose-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 9,\n" + "            \"name\": \"18K Rose Gold\",\n"
				+ "            \"nameFr\": \"Or Rose 18 Carats\",\n" + "            \"key\": \"18krosegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18kgold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 10,\n" + "            \"name\": \"18K Yellow Gold\",\n"
				+ "            \"nameFr\": \"Or Jaune 18 Carats\",\n" + "            \"key\": \"18kyellowgold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Platinum\",\n"
				+ "            \"nickFr\": \"Platine\",\n" + "            \"price\": 1100.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/platinum-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 11,\n" + "            \"name\": \"Platinum\",\n"
				+ "            \"nameFr\": \"Platine\",\n" + "            \"key\": \"platinum\"\n" + "          }\n"
				+ "        ],\n" + "        \"key\": \"MMC\"\n" + "      }\n" + "    ],\n" + "    \"key\": \"round\"\n"
				+ "  },\n" + "  {\n" + "    \"nick\": \"Princess Cut\",\n" + "    \"nickFr\": \"Princesse\",\n"
				+ "    \"price\": 0.00,\n"
				+ "    \"imageUrl\": \"https://media.vancaro.com/customized/images/princess-20210518.jpg?x-oss-process=image/format,webp\",\n"
				+ "    \"sortNumber\": 2,\n" + "    \"name\": \"Princess\",\n" + "    \"nameFr\": \"Princesse\",\n"
				+ "    \"value\": [\n" + "      {\n" + "        \"name\": \"CARAT WEIGHT\",\n"
				+ "        \"nameFr\": \"CARATAGE\",\n" + "        \"sort\": 2,\n" + "        \"value\": [\n"
				+ "          {\n" + "            \"nick\": \"1 CT\",\n" + "            \"nickFr\": \"1 CT\",\n"
				+ "            \"price\": 30.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 2,\n" + "            \"name\": \"1 Carat\",\n"
				+ "            \"nameFr\": \"1 Carat\",\n" + "            \"key\": \"1ct\"\n" + "          },\n"
				+ "          {\n" + "            \"nick\": \"1.2 CT\",\n" + "            \"nickFr\": \"1.2 CT\",\n"
				+ "            \"price\": 50.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1.2carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 3,\n" + "            \"name\": \"1.2 Carat\",\n"
				+ "            \"nameFr\": \"1.2 Carat\",\n" + "            \"key\": \"12ct\"\n" + "          },\n"
				+ "          {\n" + "            \"nick\": \"1.5 CT\",\n" + "            \"nickFr\": \"1.5 CT\",\n"
				+ "            \"price\": 60.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1.5carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 4,\n" + "            \"name\": \"1.5 Carat\",\n"
				+ "            \"nameFr\": \"1.5 Carat\",\n" + "            \"key\": \"15ct\"\n" + "          }\n"
				+ "        ],\n" + "        \"key\": \"ZSCW\"\n" + "      },\n" + "      {\n"
				+ "        \"name\": \"METAL\",\n" + "        \"nameFr\": \"MÉTAL\",\n" + "        \"sort\": 3,\n"
				+ "        \"value\": [\n" + "          {\n" + "            \"nick\": \"Silver\",\n"
				+ "            \"nickFr\": \"Argent\",\n" + "            \"price\": 40.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/925silver-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 1,\n" + "            \"name\": \"925 Sterling Silver\",\n"
				+ "            \"nameFr\": \"Argent Sterling 925\",\n" + "            \"key\": \"925silver\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose\",\n" + "            \"price\": 65.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/rgps-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 2,\n" + "            \"name\": \"Rose Gold Plated Silver\",\n"
				+ "            \"nameFr\": \"Argent Plaqué Or Rose\",\n" + "            \"key\": \"rgps\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune\",\n" + "            \"price\": 65.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/ygps-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 3,\n" + "            \"name\": \"Yellow Gold Plated Silver\",\n"
				+ "            \"nameFr\": \"Argent Plaqué Or Jaune\",\n" + "            \"key\": \"ygps\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K White Gold\",\n"
				+ "            \"nickFr\": \"Or Blanc 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14k-white-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 5,\n" + "            \"name\": \"14K White Gold\",\n"
				+ "            \"nameFr\": \"Or Blanc 14 Carats\",\n" + "            \"key\": \"14kwhitegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14k-rose-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 6,\n" + "            \"name\": \"14K Rose Gold\",\n"
				+ "            \"nameFr\": \"Or Rose 14 Carats\",\n" + "            \"key\": \"14krosegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14kgold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 7,\n" + "            \"name\": \"14K Yellow Gold\",\n"
				+ "            \"nameFr\": \"Or Jaune 14 Carats\",\n" + "            \"key\": \"14kyellowgold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K White Gold\",\n"
				+ "            \"nickFr\": \"Or Blanc 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18k-white-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 8,\n" + "            \"name\": \"18K White Gold\",\n"
				+ "            \"nameFr\": \"Or Blanc 18 Carats\",\n" + "            \"key\": \"18kwhitegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18k-rose-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 9,\n" + "            \"name\": \"18K Rose Gold\",\n"
				+ "            \"nameFr\": \"Or Rose 18 Carats\",\n" + "            \"key\": \"18krosegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18kgold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 10,\n" + "            \"name\": \"18K Yellow Gold\",\n"
				+ "            \"nameFr\": \"Or Jaune 18 Carats\",\n" + "            \"key\": \"18kyellowgold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Platinum\",\n"
				+ "            \"nickFr\": \"Platine\",\n" + "            \"price\": 1100.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/platinum-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 11,\n" + "            \"name\": \"Platinum\",\n"
				+ "            \"nameFr\": \"Platine\",\n" + "            \"key\": \"platinum\"\n" + "          }\n"
				+ "        ],\n" + "        \"key\": \"MMC\"\n" + "      }\n" + "    ],\n"
				+ "    \"key\": \"princess\"\n" + "  },\n" + "  {\n" + "    \"nick\": \"Heart Cut\",\n"
				+ "    \"nickFr\": \"Coeur\",\n" + "    \"price\": 0.00,\n"
				+ "    \"imageUrl\": \"https://media.vancaro.com/customized/images/heart-20210518.jpg?x-oss-process=image/format,webp\",\n"
				+ "    \"sortNumber\": 3,\n" + "    \"name\": \"Heart\",\n" + "    \"nameFr\": \"Coeur\",\n"
				+ "    \"value\": [\n" + "      {\n" + "        \"name\": \"CARAT WEIGHT\",\n"
				+ "        \"nameFr\": \"CARATAGE\",\n" + "        \"sort\": 2,\n" + "        \"value\": [\n"
				+ "          {\n" + "            \"nick\": \"1 CT\",\n" + "            \"nickFr\": \"1 CT\",\n"
				+ "            \"price\": 30.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 2,\n" + "            \"name\": \"1 Carat\",\n"
				+ "            \"nameFr\": \"1 Carat\",\n" + "            \"key\": \"1ct\"\n" + "          },\n"
				+ "          {\n" + "            \"nick\": \"1.2 CT\",\n" + "            \"nickFr\": \"1.2 CT\",\n"
				+ "            \"price\": 50.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1.2carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 3,\n" + "            \"name\": \"1.2 Carat\",\n"
				+ "            \"nameFr\": \"1.2 Carat\",\n" + "            \"key\": \"12ct\"\n" + "          },\n"
				+ "          {\n" + "            \"nick\": \"1.5 CT\",\n" + "            \"nickFr\": \"1.5 CT\",\n"
				+ "            \"price\": 60.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1.5carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 4,\n" + "            \"name\": \"1.5 Carat\",\n"
				+ "            \"nameFr\": \"1.5 Carat\",\n" + "            \"key\": \"15ct\"\n" + "          }\n"
				+ "        ],\n" + "        \"key\": \"ZSCW\"\n" + "      },\n" + "      {\n"
				+ "        \"name\": \"METAL\",\n" + "        \"nameFr\": \"MÉTAL\",\n" + "        \"sort\": 3,\n"
				+ "        \"value\": [\n" + "          {\n" + "            \"nick\": \"Silver\",\n"
				+ "            \"nickFr\": \"Argent\",\n" + "            \"price\": 40.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/925silver-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 1,\n" + "            \"name\": \"925 Sterling Silver\",\n"
				+ "            \"nameFr\": \"Argent Sterling 925\",\n" + "            \"key\": \"925silver\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose\",\n" + "            \"price\": 65.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/rgps-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 2,\n" + "            \"name\": \"Rose Gold Plated Silver\",\n"
				+ "            \"nameFr\": \"Argent Plaqué Or Rose\",\n" + "            \"key\": \"rgps\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune\",\n" + "            \"price\": 65.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/ygps-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 3,\n" + "            \"name\": \"Yellow Gold Plated Silver\",\n"
				+ "            \"nameFr\": \"Argent Plaqué Or Jaune\",\n" + "            \"key\": \"ygps\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K White Gold\",\n"
				+ "            \"nickFr\": \"Or Blanc 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14k-white-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 5,\n" + "            \"name\": \"14K White Gold\",\n"
				+ "            \"nameFr\": \"Or Blanc 14 Carats\",\n" + "            \"key\": \"14kwhitegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14k-rose-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 6,\n" + "            \"name\": \"14K Rose Gold\",\n"
				+ "            \"nameFr\": \"Or Rose 14 Carats\",\n" + "            \"key\": \"14krosegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14kgold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 7,\n" + "            \"name\": \"14K Yellow Gold\",\n"
				+ "            \"nameFr\": \"Or Jaune 14 Carats\",\n" + "            \"key\": \"14kyellowgold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K White Gold\",\n"
				+ "            \"nickFr\": \"Or Blanc 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18k-white-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 8,\n" + "            \"name\": \"18K White Gold\",\n"
				+ "            \"nameFr\": \"Or Blanc 18 Carats\",\n" + "            \"key\": \"18kwhitegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18k-rose-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 9,\n" + "            \"name\": \"18K Rose Gold\",\n"
				+ "            \"nameFr\": \"Or Rose 18 Carats\",\n" + "            \"key\": \"18krosegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18kgold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 10,\n" + "            \"name\": \"18K Yellow Gold\",\n"
				+ "            \"nameFr\": \"Or Jaune 18 Carats\",\n" + "            \"key\": \"18kyellowgold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Platinum\",\n"
				+ "            \"nickFr\": \"Platine\",\n" + "            \"price\": 1100.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/platinum-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 11,\n" + "            \"name\": \"Platinum\",\n"
				+ "            \"nameFr\": \"Platine\",\n" + "            \"key\": \"platinum\"\n" + "          }\n"
				+ "        ],\n" + "        \"key\": \"MMC\"\n" + "      }\n" + "    ],\n" + "    \"key\": \"heart\"\n"
				+ "  },\n" + "  {\n" + "    \"nick\": \"Cushion Cut\",\n" + "    \"nickFr\": \"Coussin\",\n"
				+ "    \"price\": 0.00,\n"
				+ "    \"imageUrl\": \"https://media.vancaro.com/customized/images/cushion-20210518.jpg?x-oss-process=image/format,webp\",\n"
				+ "    \"sortNumber\": 4,\n" + "    \"name\": \"Cushion\",\n" + "    \"nameFr\": \"Coussin\",\n"
				+ "    \"value\": [\n" + "      {\n" + "        \"name\": \"CARAT WEIGHT\",\n"
				+ "        \"nameFr\": \"CARATAGE\",\n" + "        \"sort\": 2,\n" + "        \"value\": [\n"
				+ "          {\n" + "            \"nick\": \"1 CT\",\n" + "            \"nickFr\": \"1 CT\",\n"
				+ "            \"price\": 30.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 2,\n" + "            \"name\": \"1 Carat\",\n"
				+ "            \"nameFr\": \"1 Carat\",\n" + "            \"key\": \"1ct\"\n" + "          },\n"
				+ "          {\n" + "            \"nick\": \"1.5 CT\",\n" + "            \"nickFr\": \"1.5 CT\",\n"
				+ "            \"price\": 60.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1.5carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 4,\n" + "            \"name\": \"1.5 Carat\",\n"
				+ "            \"nameFr\": \"1.5 Carat\",\n" + "            \"key\": \"15ct\"\n" + "          }\n"
				+ "        ],\n" + "        \"key\": \"ZSCW\"\n" + "      },\n" + "      {\n"
				+ "        \"name\": \"METAL\",\n" + "        \"nameFr\": \"MÉTAL\",\n" + "        \"sort\": 3,\n"
				+ "        \"value\": [\n" + "          {\n" + "            \"nick\": \"Silver\",\n"
				+ "            \"nickFr\": \"Argent\",\n" + "            \"price\": 40.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/925silver-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 1,\n" + "            \"name\": \"925 Sterling Silver\",\n"
				+ "            \"nameFr\": \"Argent Sterling 925\",\n" + "            \"key\": \"925silver\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose\",\n" + "            \"price\": 65.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/rgps-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 2,\n" + "            \"name\": \"Rose Gold Plated Silver\",\n"
				+ "            \"nameFr\": \"Argent Plaqué Or Rose\",\n" + "            \"key\": \"rgps\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune\",\n" + "            \"price\": 65.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/ygps-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 3,\n" + "            \"name\": \"Yellow Gold Plated Silver\",\n"
				+ "            \"nameFr\": \"Argent Plaqué Or Jaune\",\n" + "            \"key\": \"ygps\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K White Gold\",\n"
				+ "            \"nickFr\": \"Or Blanc 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14k-white-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 5,\n" + "            \"name\": \"14K White Gold\",\n"
				+ "            \"nameFr\": \"Or Blanc 14 Carats\",\n" + "            \"key\": \"14kwhitegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14k-rose-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 6,\n" + "            \"name\": \"14K Rose Gold\",\n"
				+ "            \"nameFr\": \"Or Rose 14 Carats\",\n" + "            \"key\": \"14krosegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14kgold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 7,\n" + "            \"name\": \"14K Yellow Gold\",\n"
				+ "            \"nameFr\": \"Or Jaune 14 Carats\",\n" + "            \"key\": \"14kyellowgold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K White Gold\",\n"
				+ "            \"nickFr\": \"Or Blanc 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18k-white-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 8,\n" + "            \"name\": \"18K White Gold\",\n"
				+ "            \"nameFr\": \"Or Blanc 18 Carats\",\n" + "            \"key\": \"18kwhitegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18k-rose-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 9,\n" + "            \"name\": \"18K Rose Gold\",\n"
				+ "            \"nameFr\": \"Or Rose 18 Carats\",\n" + "            \"key\": \"18krosegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18kgold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 10,\n" + "            \"name\": \"18K Yellow Gold\",\n"
				+ "            \"nameFr\": \"Or Jaune 18 Carats\",\n" + "            \"key\": \"18kyellowgold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Platinum\",\n"
				+ "            \"nickFr\": \"Platine\",\n" + "            \"price\": 1100.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/platinum-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 11,\n" + "            \"name\": \"Platinum\",\n"
				+ "            \"nameFr\": \"Platine\",\n" + "            \"key\": \"platinum\"\n" + "          }\n"
				+ "        ],\n" + "        \"key\": \"MMC\"\n" + "      }\n" + "    ],\n"
				+ "    \"key\": \"cushion\"\n" + "  },\n" + "  {\n" + "    \"nick\": \"Oval Cut\",\n"
				+ "    \"nickFr\": \"Ovale\",\n" + "    \"price\": 0.00,\n"
				+ "    \"imageUrl\": \"https://media.vancaro.com/customized/images/oval-20210518.jpg?x-oss-process=image/format,webp\",\n"
				+ "    \"sortNumber\": 5,\n" + "    \"name\": \"Oval\",\n" + "    \"nameFr\": \"Ovale\",\n"
				+ "    \"value\": [\n" + "      {\n" + "        \"name\": \"CARAT WEIGHT\",\n"
				+ "        \"nameFr\": \"CARATAGE\",\n" + "        \"sort\": 2,\n" + "        \"value\": [\n"
				+ "          {\n" + "            \"nick\": \"1 CT\",\n" + "            \"nickFr\": \"1 CT\",\n"
				+ "            \"price\": 30.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 2,\n" + "            \"name\": \"1 Carat\",\n"
				+ "            \"nameFr\": \"1 Carat\",\n" + "            \"key\": \"1ct\"\n" + "          },\n"
				+ "          {\n" + "            \"nick\": \"1.5 CT\",\n" + "            \"nickFr\": \"1.5 CT\",\n"
				+ "            \"price\": 60.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1.5carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 4,\n" + "            \"name\": \"1.5 Carat\",\n"
				+ "            \"nameFr\": \"1.5 Carat\",\n" + "            \"key\": \"15ct\"\n" + "          },\n"
				+ "          {\n" + "            \"nick\": \"2 CT\",\n" + "            \"nickFr\": \"2 CT\",\n"
				+ "            \"price\": 90.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/2carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 5,\n" + "            \"name\": \"2 Carat\",\n"
				+ "            \"nameFr\": \"2 Carat\",\n" + "            \"key\": \"2ct\"\n" + "          }\n"
				+ "        ],\n" + "        \"key\": \"ZSCW\"\n" + "      },\n" + "      {\n"
				+ "        \"name\": \"METAL\",\n" + "        \"nameFr\": \"MÉTAL\",\n" + "        \"sort\": 3,\n"
				+ "        \"value\": [\n" + "          {\n" + "            \"nick\": \"Silver\",\n"
				+ "            \"nickFr\": \"Argent\",\n" + "            \"price\": 40.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/925silver-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 1,\n" + "            \"name\": \"925 Sterling Silver\",\n"
				+ "            \"nameFr\": \"Argent Sterling 925\",\n" + "            \"key\": \"925silver\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose\",\n" + "            \"price\": 65.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/rgps-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 2,\n" + "            \"name\": \"Rose Gold Plated Silver\",\n"
				+ "            \"nameFr\": \"Argent Plaqué Or Rose\",\n" + "            \"key\": \"rgps\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune\",\n" + "            \"price\": 65.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/ygps-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 3,\n" + "            \"name\": \"Yellow Gold Plated Silver\",\n"
				+ "            \"nameFr\": \"Argent Plaqué Or Jaune\",\n" + "            \"key\": \"ygps\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K White Gold\",\n"
				+ "            \"nickFr\": \"Or Blanc 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14k-white-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 5,\n" + "            \"name\": \"14K White Gold\",\n"
				+ "            \"nameFr\": \"Or Blanc 14 Carats\",\n" + "            \"key\": \"14kwhitegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14k-rose-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 6,\n" + "            \"name\": \"14K Rose Gold\",\n"
				+ "            \"nameFr\": \"Or Rose 14 Carats\",\n" + "            \"key\": \"14krosegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14kgold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 7,\n" + "            \"name\": \"14K Yellow Gold\",\n"
				+ "            \"nameFr\": \"Or Jaune 14 Carats\",\n" + "            \"key\": \"14kyellowgold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K White Gold\",\n"
				+ "            \"nickFr\": \"Or Blanc 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18k-white-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 8,\n" + "            \"name\": \"18K White Gold\",\n"
				+ "            \"nameFr\": \"Or Blanc 18 Carats\",\n" + "            \"key\": \"18kwhitegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18k-rose-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 9,\n" + "            \"name\": \"18K Rose Gold\",\n"
				+ "            \"nameFr\": \"Or Rose 18 Carats\",\n" + "            \"key\": \"18krosegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18kgold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 10,\n" + "            \"name\": \"18K Yellow Gold\",\n"
				+ "            \"nameFr\": \"Or Jaune 18 Carats\",\n" + "            \"key\": \"18kyellowgold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Platinum\",\n"
				+ "            \"nickFr\": \"Platine\",\n" + "            \"price\": 1100.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/platinum-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 11,\n" + "            \"name\": \"Platinum\",\n"
				+ "            \"nameFr\": \"Platine\",\n" + "            \"key\": \"platinum\"\n" + "          }\n"
				+ "        ],\n" + "        \"key\": \"MMC\"\n" + "      }\n" + "    ],\n" + "    \"key\": \"oval\"\n"
				+ "  },\n" + "  {\n" + "    \"nick\": \"Pear Cut\",\n" + "    \"nickFr\": \"Poire\",\n"
				+ "    \"price\": 0.00,\n"
				+ "    \"imageUrl\": \"https://media.vancaro.com/customized/images/pear-20210518.jpg?x-oss-process=image/format,webp\",\n"
				+ "    \"sortNumber\": 6,\n" + "    \"name\": \"Pear\",\n" + "    \"nameFr\": \"Poire\",\n"
				+ "    \"value\": [\n" + "      {\n" + "        \"name\": \"CARAT WEIGHT\",\n"
				+ "        \"nameFr\": \"CARATAGE\",\n" + "        \"sort\": 2,\n" + "        \"value\": [\n"
				+ "          {\n" + "            \"nick\": \"1 CT\",\n" + "            \"nickFr\": \"1 CT\",\n"
				+ "            \"price\": 30.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 2,\n" + "            \"name\": \"1 Carat\",\n"
				+ "            \"nameFr\": \"1 Carat\",\n" + "            \"key\": \"1ct\"\n" + "          },\n"
				+ "          {\n" + "            \"nick\": \"1.2 CT\",\n" + "            \"nickFr\": \"1.2 CT\",\n"
				+ "            \"price\": 50.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1.2carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 3,\n" + "            \"name\": \"1.2 Carat\",\n"
				+ "            \"nameFr\": \"1.2 Carat\",\n" + "            \"key\": \"12ct\"\n" + "          },\n"
				+ "          {\n" + "            \"nick\": \"1.5 CT\",\n" + "            \"nickFr\": \"1.5 CT\",\n"
				+ "            \"price\": 60.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1.5carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 4,\n" + "            \"name\": \"1.5 Carat\",\n"
				+ "            \"nameFr\": \"1.5 Carat\",\n" + "            \"key\": \"15ct\"\n" + "          }\n"
				+ "        ],\n" + "        \"key\": \"ZSCW\"\n" + "      },\n" + "      {\n"
				+ "        \"name\": \"METAL\",\n" + "        \"nameFr\": \"MÉTAL\",\n" + "        \"sort\": 3,\n"
				+ "        \"value\": [\n" + "          {\n" + "            \"nick\": \"Silver\",\n"
				+ "            \"nickFr\": \"Argent\",\n" + "            \"price\": 40.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/925silver-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 1,\n" + "            \"name\": \"925 Sterling Silver\",\n"
				+ "            \"nameFr\": \"Argent Sterling 925\",\n" + "            \"key\": \"925silver\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose\",\n" + "            \"price\": 65.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/rgps-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 2,\n" + "            \"name\": \"Rose Gold Plated Silver\",\n"
				+ "            \"nameFr\": \"Argent Plaqué Or Rose\",\n" + "            \"key\": \"rgps\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune\",\n" + "            \"price\": 65.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/ygps-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 3,\n" + "            \"name\": \"Yellow Gold Plated Silver\",\n"
				+ "            \"nameFr\": \"Argent Plaqué Or Jaune\",\n" + "            \"key\": \"ygps\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K White Gold\",\n"
				+ "            \"nickFr\": \"Or Blanc 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14k-white-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 5,\n" + "            \"name\": \"14K White Gold\",\n"
				+ "            \"nameFr\": \"Or Blanc 14 Carats\",\n" + "            \"key\": \"14kwhitegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14k-rose-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 6,\n" + "            \"name\": \"14K Rose Gold\",\n"
				+ "            \"nameFr\": \"Or Rose 14 Carats\",\n" + "            \"key\": \"14krosegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14kgold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 7,\n" + "            \"name\": \"14K Yellow Gold\",\n"
				+ "            \"nameFr\": \"Or Jaune 14 Carats\",\n" + "            \"key\": \"14kyellowgold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K White Gold\",\n"
				+ "            \"nickFr\": \"Or Blanc 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18k-white-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 8,\n" + "            \"name\": \"18K White Gold\",\n"
				+ "            \"nameFr\": \"Or Blanc 18 Carats\",\n" + "            \"key\": \"18kwhitegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18k-rose-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 9,\n" + "            \"name\": \"18K Rose Gold\",\n"
				+ "            \"nameFr\": \"Or Rose 18 Carats\",\n" + "            \"key\": \"18krosegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18kgold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 10,\n" + "            \"name\": \"18K Yellow Gold\",\n"
				+ "            \"nameFr\": \"Or Jaune 18 Carats\",\n" + "            \"key\": \"18kyellowgold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Platinum\",\n"
				+ "            \"nickFr\": \"Platine\",\n" + "            \"price\": 1100.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/platinum-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 11,\n" + "            \"name\": \"Platinum\",\n"
				+ "            \"nameFr\": \"Platine\",\n" + "            \"key\": \"platinum\"\n" + "          }\n"
				+ "        ],\n" + "        \"key\": \"MMC\"\n" + "      }\n" + "    ],\n" + "    \"key\": \"pear\"\n"
				+ "  },\n" + "  {\n" + "    \"nick\": \"Asscher Cut\",\n" + "    \"nickFr\": \"Asscher\",\n"
				+ "    \"price\": 0.00,\n"
				+ "    \"imageUrl\": \"https://media.vancaro.com/customized/images/asscher-20210518.jpg?x-oss-process=image/format,webp\",\n"
				+ "    \"sortNumber\": 7,\n" + "    \"name\": \"Asscher\",\n" + "    \"nameFr\": \"Asscher\",\n"
				+ "    \"value\": [\n" + "      {\n" + "        \"name\": \"CARAT WEIGHT\",\n"
				+ "        \"nameFr\": \"CARATAGE\",\n" + "        \"sort\": 2,\n" + "        \"value\": [\n"
				+ "          {\n" + "            \"nick\": \"1 CT\",\n" + "            \"nickFr\": \"1 CT\",\n"
				+ "            \"price\": 30.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 2,\n" + "            \"name\": \"1 Carat\",\n"
				+ "            \"nameFr\": \"1 Carat\",\n" + "            \"key\": \"1ct\"\n" + "          },\n"
				+ "          {\n" + "            \"nick\": \"1.5 CT\",\n" + "            \"nickFr\": \"1.5 CT\",\n"
				+ "            \"price\": 60.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/1.5carat.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 4,\n" + "            \"name\": \"1.5 Carat\",\n"
				+ "            \"nameFr\": \"1.5 Carat\",\n" + "            \"key\": \"15ct\"\n" + "          }\n"
				+ "        ],\n" + "        \"key\": \"ZSCW\"\n" + "      },\n" + "      {\n"
				+ "        \"name\": \"METAL\",\n" + "        \"nameFr\": \"MÉTAL\",\n" + "        \"sort\": 3,\n"
				+ "        \"value\": [\n" + "          {\n" + "            \"nick\": \"Silver\",\n"
				+ "            \"nickFr\": \"Argent\",\n" + "            \"price\": 40.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/925silver-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 1,\n" + "            \"name\": \"925 Sterling Silver\",\n"
				+ "            \"nameFr\": \"Argent Sterling 925\",\n" + "            \"key\": \"925silver\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose\",\n" + "            \"price\": 65.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/rgps-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 2,\n" + "            \"name\": \"Rose Gold Plated Silver\",\n"
				+ "            \"nameFr\": \"Argent Plaqué Or Rose\",\n" + "            \"key\": \"rgps\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune\",\n" + "            \"price\": 65.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/ygps-20220307.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 3,\n" + "            \"name\": \"Yellow Gold Plated Silver\",\n"
				+ "            \"nameFr\": \"Argent Plaqué Or Jaune\",\n" + "            \"key\": \"ygps\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K White Gold\",\n"
				+ "            \"nickFr\": \"Or Blanc 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14k-white-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 5,\n" + "            \"name\": \"14K White Gold\",\n"
				+ "            \"nameFr\": \"Or Blanc 14 Carats\",\n" + "            \"key\": \"14kwhitegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14k-rose-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 6,\n" + "            \"name\": \"14K Rose Gold\",\n"
				+ "            \"nameFr\": \"Or Rose 14 Carats\",\n" + "            \"key\": \"14krosegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"14K Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune 14 Carats\",\n" + "            \"price\": 650.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/14kgold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 7,\n" + "            \"name\": \"14K Yellow Gold\",\n"
				+ "            \"nameFr\": \"Or Jaune 14 Carats\",\n" + "            \"key\": \"14kyellowgold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K White Gold\",\n"
				+ "            \"nickFr\": \"Or Blanc 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18k-white-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 8,\n" + "            \"name\": \"18K White Gold\",\n"
				+ "            \"nameFr\": \"Or Blanc 18 Carats\",\n" + "            \"key\": \"18kwhitegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K Rose Gold\",\n"
				+ "            \"nickFr\": \"Or Rose 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18k-rose-gold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 9,\n" + "            \"name\": \"18K Rose Gold\",\n"
				+ "            \"nameFr\": \"Or Rose 18 Carats\",\n" + "            \"key\": \"18krosegold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"18K Yellow Gold\",\n"
				+ "            \"nickFr\": \"Or Jaune 18 Carats\",\n" + "            \"price\": 800.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/18kgold-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 10,\n" + "            \"name\": \"18K Yellow Gold\",\n"
				+ "            \"nameFr\": \"Or Jaune 18 Carats\",\n" + "            \"key\": \"18kyellowgold\"\n"
				+ "          },\n" + "          {\n" + "            \"nick\": \"Platinum\",\n"
				+ "            \"nickFr\": \"Platine\",\n" + "            \"price\": 1100.00,\n"
				+ "            \"imageUrl\": \"https://media.vancaro.com/customized/images/platinum-20210511.jpg?x-oss-process=image/format,webp\",\n"
				+ "            \"sortNumber\": 11,\n" + "            \"name\": \"Platinum\",\n"
				+ "            \"nameFr\": \"Platine\",\n" + "            \"key\": \"platinum\"\n" + "          }\n"
				+ "        ],\n" + "        \"key\": \"MMC\"\n" + "      }\n" + "    ],\n"
				+ "    \"key\": \"asscher\"\n" + "  }\n" + "";

		JSONObject obj = JSONObject.fromObject(jsonStr);
		Boolean b = false;

		String spu = "CUS0017||ZSS-asscher_ZSCW-15ct_MMC-925silver";
		String[] spuStrings = spu.split("\\|\\|");
		String arr = "";

		if (spuStrings.length == 2) {
			arr = spuStrings[1];
		} else {
			arr = spuStrings[0];
		}

		String[] attArr = arr.split("_");
		if (attArr.length == 1) {
			b = true;
		}

		if (attArr.length == 2) {
			JSONObject twoArrObj = obj.getJSONArray("value").getJSONObject(0);
			String towKey = twoArrObj.getString("key");

			JSONObject threeObj = twoArrObj.getJSONArray("value").getJSONObject(0);
		}

		if (attArr.length == 3) {
			JSONObject twoArrObj = obj.getJSONArray("value").getJSONObject(0);
			String towKey = twoArrObj.getString("key");
			Boolean b_twoKey = false;
			JSONArray twoObjValueArr = twoArrObj.getJSONArray("value");
			for (int i = 0; i < twoObjValueArr.size(); i++) {
				JSONObject twoObjValue = twoObjValueArr.getJSONObject(i);
				String towKeyValue = twoObjValue.getString("key");
				if (attArr[1].equals(towKey + "-" + towKeyValue)) {
					b_twoKey = true;
					break;
				}
			}

			JSONObject threeArrObj = obj.getJSONArray("value").getJSONObject(1);
			String threeKey = threeArrObj.getString("key");
			Boolean b_threeKey = false;
			JSONArray threeObjValueArr = threeArrObj.getJSONArray("value");
			for (int i = 0; i < threeObjValueArr.size(); i++) {
				JSONObject threeObjValue = threeObjValueArr.getJSONObject(i);
				String threeKeyValue = threeObjValue.getString("key");
				if (attArr[2].equals(threeKey + "-" + threeKeyValue)) {
					b_threeKey = true;
					break;
				}
			}

			if (b_twoKey && b_threeKey) {
				b = true;
			}
		}

		System.out.println(b);
		System.out.println(arr);
	}

}
