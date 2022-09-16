package com.vankle.catalog.service.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.vankle.cart.dao.CartRuleEntityMapper;
import com.vankle.cart.entity.CartRuleEntity;
import com.vankle.catalog.dao.CatalogCategoryEntityMapper;
import com.vankle.catalog.dao.CatalogCategoryProductMapper;
import com.vankle.catalog.dao.CatalogProductAttributeValueMapper;
import com.vankle.catalog.dao.CatalogProductBundledLinkMapper;
import com.vankle.catalog.dao.CatalogProductCustomizedAttributeMapper;
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


@Service(group="calalogProductService", version="1.0")
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
	
	public String getCatalogProductInfoByItemId(String paramJson) {

		logger.error("itemId:"+paramJson);
		JSONObject resultObj = JsonUtils.createJSONObject();
		JSONObject paramObj = new  JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if(!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))){
			return resultObj.toString();
		} 
		try {  
			int productId = paramObj.getInt("productId");
			int storeId = paramObj.getInt("storeId"); 
		 
			CatalogProductEntity catalogProductEntity = catalogProductEntityMapper.findCatalogProductEntityByItemId(productId, storeId);
			//判断商品是否存在
			if(catalogProductEntity==null){
				 JsonUtils.modifyJSONObject(resultObj,VankleConstants.VANKLE_CODE_FAIL_10002, VankleConstants.VANKLE_CODE_FAIL_10002_MESSAGE).toString();
				 return resultObj.toString();
			}else{
				productId = catalogProductEntity.getId();
			}
			 
			paramObj.put("productId", productId);
			logger.info("itemId:"+paramObj.toString());
			 
			String resultStr = this.getCatalogProductInfoByParamJson(paramObj.toString());
			
			if(catalogProductEntity.getProductCustomizedId() != null ) {
				try {  
					paramObj.put("productId",catalogProductEntity.getProductCustomizedId() );
					logger.info("itemId:"+paramObj.toString()); 
					String resultCustomizedStr = this.getCatalogProductInfoByParamJson(paramObj.toString()); 
					JSONObject obj = JSONObject.fromObject(resultStr);
					obj.put("productCustomizedEntity", JSONObject.fromObject(resultCustomizedStr)); 
					return obj.toString(); 
				}catch (Exception e) {
					e.printStackTrace(); 
				}
			} 

			return resultStr;
		}catch (Exception e) {
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
		JSONObject paramObj = new  JSONObject();
		paramObj = VankleUtils.checkParamJsonString(resultObj, paramJson);
		if(!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))){
			return resultObj.toString();
		}
		int productId = paramObj.getInt("productId");
		int languageId = paramObj.getInt("languageId");
		int currencyId = paramObj.getInt("currencyId");
		String countryId = "us";
		try{
			if(paramObj.get("prefixion")!=null){
				countryId = paramObj.getString("prefixion");
				if(countryId.split("-").length==2){
					countryId = countryId.split("-")[1];
				}
			}
		}catch (Exception e) {
			logger.info(e.getMessage());
			// TODO: handle exception
		} 
		Object requestType = paramObj.get("requestType");
		
		resultObj.put("requestType", requestType);
		
		String resultStr = this.getProductLanguageInfo(resultObj,productId,languageId,currencyId,countryId);
		//System.out.println(resultStr);
		//logger.info("resultObj.getString(\"code\"):"+resultObj.getString("code"));
		if(!VankleConstants.VANKLE_CODE_SUCCESS.equals(resultObj.getString("code"))){
			return resultObj.toString();
		}else{
			return this.getCurrencyProduct(resultStr, currencyId );
		}
	}
		
	/**
	 *  商品信息汇率转换
	 * @param resultStr
	 * @param currencyId
	 * @param storeId
	 * @return
	 */
	public String getCurrencyProduct(String resultStr,int currencyId ){
		if(currencyId==1){
			return resultStr;
		}else{
			JSONObject resultObj = JSONObject.fromObject(resultStr);
			JSONObject productObj = resultObj.getJSONObject("data");
			String jsonCurrency =  systemService.getCurrencyEntity(currencyId);
			logger.error(resultStr);
			productObj.put("currency", JSONObject.fromObject(jsonCurrency));
			//商品信息价格换算
			BigDecimal originalPrice =  systemCurrencyService.getAmountByCurrencyId(new BigDecimal(productObj.getString("originalPrice")), currencyId);
			productObj.put("originalPrice", originalPrice);
			BigDecimal sellPrice =  systemCurrencyService.getAmountByCurrencyId(new BigDecimal(productObj.getString("sellPrice")), currencyId);
			productObj.put("baseSellPrice",productObj.getString("sellPrice")); 
			productObj.put("sellPrice", sellPrice);
			
			String  customizedDesc = productObj.getString("customizedDesc");
			if(customizedDesc.indexOf(",")>=0) {
				String[]  priceArr = customizedDesc.split(",");
				String customizedDescCur = "";
				for(String price:priceArr) {
					try {
						BigDecimal priceCur =  systemCurrencyService.getAmountByCurrencyId(new BigDecimal(price), currencyId);
						customizedDescCur += priceCur+",";
					}catch (Exception e) {
						customizedDescCur += "0,";
						e.printStackTrace();
						// TODO: handle exception
					} 
				}
				if(customizedDescCur.indexOf(",")>=0) {
					customizedDescCur = customizedDescCur.substring(0,customizedDescCur.length()-1);
				}
				productObj.put("customizedDesc_cur", customizedDescCur);
			}
			
			
			//商品折扣信息价格换算
			JSONObject catalogProductEntityDiscount = productObj.getJSONObject("catalogProductEntityDiscount");
			catalogProductEntityDiscount.put("baseDiscountAmount", catalogProductEntityDiscount.getString("discountAmount")); 
			BigDecimal discountAmount =  systemCurrencyService.getAmountByCurrencyId(new BigDecimal(catalogProductEntityDiscount.getString("discountAmount")), currencyId);
			catalogProductEntityDiscount.put("discountAmount", discountAmount);
			
			try {
				logger.error(productObj.toString());
				
				JSONArray setList =productObj.getJSONArray("setList");
				if(setList.getJSONObject(0).containsKey("value")) {
					JSONArray valueList =setList.getJSONObject(0).getJSONArray("value");
					for(int i =0 ; i<valueList.size() ; i++) {
						JSONObject oneObj = valueList.getJSONObject(i); 
						BigDecimal onePrice =  systemCurrencyService.getAmountByCurrencyId(new BigDecimal( oneObj.getString("price")), currencyId);
						valueList.getJSONObject(i).put("price", onePrice);
						
						if(oneObj.containsKey("value")) {
							JSONArray twoList =oneObj.getJSONArray("value").getJSONObject(0).getJSONArray("value");
							logger.error(twoList.toString());
							logger.error(twoList.size()+"");
							for(int m =0 ; m< twoList.size() ; m++) { 
								logger.error(m+"");
								JSONObject towObj = twoList.getJSONObject(m); 
								logger.error(""+towObj.getString("price"));
								BigDecimal towPrice =  systemCurrencyService.getAmountByCurrencyId(new BigDecimal( towObj.getString("price")), currencyId);
								logger.error(""+towPrice);
								towObj.put("price", towPrice);  
								
								try {
									if(towObj.containsKey("value")) {
										
										JSONArray threeList =towObj.getJSONArray("value").getJSONObject(0).getJSONArray("value");
										logger.error(threeList.toString());
										logger.error(threeList.size()+"");
										for(int m3 =0 ; m3< threeList.size() ; m3++) { 
											logger.error(m3+"");
											JSONObject threeObj = threeList.getJSONObject(m3); 
											logger.error(""+threeObj.getString("price"));
											BigDecimal threePrice =  systemCurrencyService.getAmountByCurrencyId(new BigDecimal( threeObj.getString("price")), currencyId);
											logger.error(""+threePrice);
											threeObj.put("price", threePrice);   
										} 
									} 
								}catch (Exception e) { 
									e.printStackTrace();
									// TODO: handle exception
								} 
							}
						}
					}
				}
				
			}catch (Exception e) {
				 
				e.printStackTrace();
				// TODO: handle exception
			} 
			
			logger.error(resultObj.toString());
			return resultObj.toString();
		}
	}
	
	public String getProductLanguageInfo(JSONObject resultObj, int productId,int languageId,int currencyId,String countryId){
		 
		CatalogProductEntity catalogProductEntity = catalogProductEntityMapper.findCatalogProductEntity(productId);
		//判断商品是否存在
		if(catalogProductEntity==null){
			 JsonUtils.modifyJSONObject(resultObj,VankleConstants.VANKLE_CODE_FAIL_10002, VankleConstants.VANKLE_CODE_FAIL_10002_MESSAGE).toString();
			 return resultObj.toString();
		}
		
		
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());  
		JSONObject jsonProduct = JSONObject.fromObject(catalogProductEntity, jsonConfig);  
		jsonProduct.put("requestType", resultObj.get("requestType"));
		
		String jsonCurrency =  systemService.getCurrencyEntity(currencyId);
		jsonProduct.put("currency", JSONObject.fromObject(jsonCurrency));
		
		//添加商品分类
		this.addCatalogProductCategory(jsonProduct,jsonConfig,languageId);
		//添加折扣信息
		this.addCatalogProductDiscount(jsonProduct,jsonConfig);
		//添加商品规格
		this.addCatalogProductSpec(jsonProduct,jsonConfig,  countryId);
		//添加商品图片
		this.addCatalogProductImage(jsonProduct,jsonConfig);
		//添加商品视频
		this.addCatalogProductVideo(jsonProduct,jsonConfig);
		//添加捆绑销售资料
		this.addCatalogProductGroupSell(jsonProduct,jsonConfig,languageId,currencyId,countryId);
		//添加商品推荐商品
		this.addCatalogProductRecommended(jsonProduct,jsonConfig,languageId,currencyId,countryId);
		
		if(catalogProductEntity.getCustomizedType() != null && catalogProductEntity.getCustomizedType() ==5 ){
			this.addCatalogProductCustomizeAttributeValue(catalogProductEntity,jsonProduct, jsonConfig,languageId);
		}
		
		//添加商品自定义属性
		//this.addCatalogProductAttributeValue(jsonProduct, jsonConfig);
		jsonProduct.put("id", jsonProduct.getInt("itemId"));
		resultObj.put("data", jsonProduct); 
		if(languageId == 1){
			return resultObj.toString();  
		}else{
			String resultStrLanguage = this.getLanguageByJosnProduct(resultObj.toString(), productId, languageId);
			return resultStrLanguage;
		}
		
	}


	/**
	  * 添加商品折扣
	  * @param jsonProduct
	  */
	public void addCatalogProductCategory(JSONObject jsonProduct,JsonConfig config,int languageId) {
		
		List<CatalogCategoryProduct> catalogProductList =
				catalogCategoryProductMapper.findCatalogCategoryProductListByProduct(jsonProduct.getInt("id")); 
		
		jsonProduct.put("categoryIds", "");
		String categoryIds = "";
		JSONArray cartRuleEntitys = new JSONArray();
		for(CatalogCategoryProduct catalogCategoryProduct :catalogProductList){ 
			List<CartRuleEntity>  cartRuleEntityList = cartRuleEntityMapper.findCartRuleEntityByStoreIdAndCategoryId(jsonProduct.getInt("storeId"), catalogCategoryProduct.getCategoryId());
			for(CartRuleEntity cartRuleEntity:cartRuleEntityList) {  
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
		for(CatalogCategoryProduct catalogCategoryProduct :catalogProductList){
			CatalogCategoryEntity categoryEntity =
					catalogCategoryEntityMapper.findCatalogCategoryEntityById(catalogCategoryProduct.getCategoryId(), languageId);
			if(categoryEntity!=null){
				if(categoryEntity.getLevel()==2){
					category = categoryEntity.getName();
				}else if(categoryEntity.getLevel()==3){
					CatalogCategoryEntity categoryEntity2 =
							catalogCategoryEntityMapper.findCatalogCategoryEntityById(categoryEntity.getParentCategoryId(), languageId);
					if(categoryEntity2!=null)
						category = categoryEntity2.getName() + "/" + categoryEntity.getName();
					else
						category = categoryEntity.getName();
				}

				jsonProduct.put("category", category);
				 
			}
			if(!"".equals(category)) {
				break;
			} 
		}
	}
	 
	
	
	
	/**
	  * 添加商品折扣
	  * @param jsonProduct
	  */
	public void addCatalogProductDiscount(JSONObject jsonProduct,JsonConfig config) {
			CatalogProductEntityDiscount catalogProductEntityDiscount = 
					catalogProductEntityDiscountMapper.findCatalogProductEntityDiscount(jsonProduct.getInt("id"));
			jsonProduct.put("catalogProductEntityDiscount", JSONObject.fromObject(  catalogProductEntityDiscount,config));
	}
	
	
	/** 
	  *  英语商品信息 翻译对应的语言
	  * @param resultObj productId languageId
	  */
	public String getLanguageByJosnProduct(String englishProductJson,int productId,int  languageId){
		JSONObject resultObj = JSONObject.fromObject(englishProductJson);
		JSONObject productObj = resultObj.getJSONObject("data");
		String spu = productObj.getString("spu");
		
		CatalogProductEntityLanguage catalogProductEntityLanguage = catalogProductEntityLanguageMapper.findCatalogProductEntityLanguage(spu,languageId);
		if(catalogProductEntityLanguage!=null){
			JSONObject jsonProductLang = JSONObject.fromObject(catalogProductEntityLanguage);  
			Class<CatalogProductEntityLanguage> clz = CatalogProductEntityLanguage.class;
			Field[] fields = clz.getDeclaredFields();  
	        for(Field field : fields){  
	            boolean fieldHasAnno = field.isAnnotationPresent(LanguageAnnotation.class);  
	            if(fieldHasAnno){  
	                //输出注解属性   
	            	//logger.info(field.getName());   
	            	if(!"".equals(jsonProductLang.getString(field.getName()))){
	            		productObj.put(field.getName(), jsonProductLang.getString(field.getName()));
	            	}
	            }  
	        }  
		}
		return resultObj.toString();
	}
	
	
	/**
	 * 添加商品SKU
	 * @param jsonProduct
	 * @param config
	 */
	public void addCatalogProductSku(JSONObject jsonProduct,JsonConfig config,String countryId) {
		List<CatalogProductSku> catalogProductSkus = catalogProductSkuMapper.findCatalogProductSkuList(jsonProduct.getInt("id"),countryId);
		JSONArray jsonCatalogProductSkus = new JSONArray();
		for(CatalogProductSku catalogProductSku:catalogProductSkus){
			if(catalogProductSku.getStatus()==1)
				jsonCatalogProductSkus.add(catalogProductSku);
		}
		//JSONArray jsonCatalogProductSkus = JSONArray.fromObject(catalogProductSkus);
		jsonProduct.put("catalogProductSkus", jsonCatalogProductSkus);
	}
	
	/**
	  * 添加商品自定义属性
	  * @param jsonProduct
	  */
	public void addCatalogProductCustomizeAttributeValue(CatalogProductEntity catalogProductEntity,JSONObject jsonProduct,JsonConfig config,int languageId) {
		
			List<CatalogProductAttributeValue> catalogProductAttributeValues = catalogProductAttributeValueMapper.findCatalogProductAttributeValue(jsonProduct.getInt("id"));
			JSONArray jsonCatalogProductAttributeValues = JSONArray.fromObject(catalogProductAttributeValues);
			jsonProduct.put("catalogProductAttributeValues", jsonCatalogProductAttributeValues); 
			JSONArray set_list = new JSONArray();
			
			List<Map<String,Object>> catalogProductCustomizedAttributeList  = 
					catalogProductCustomizedAttributeMapper.findCatalogProductcustomizedAttributListByProductId(catalogProductEntity.getId());
			
//			List<Map<String,Object>> catalogProductCustomizedAttributeValueList  = 
//					catalogProductCustomizedAttributeMapper.findCatalogProductcustomizedAttributVaueListByProductId(catalogProductEntity.getStoreId());
//			Map<String,Object> attributeValueNameMap = new HashMap<String, Object>();
//			for(Map<String,Object> attributeValueMap:catalogProductCustomizedAttributeValueList) {
//				String key = attributeValueMap.get("customized_attribute_id")+""+attributeValueMap.get("value");
//				attributeValueNameMap.put(key, attributeValueMap);
//			} 
			for(Map<String,Object> customizedAttributeMap:catalogProductCustomizedAttributeList) { 
				JSONObject obj = new JSONObject();
				obj.put("key", customizedAttributeMap.get("short_name"));
				obj.put("name", customizedAttributeMap.get("name"));
				obj.put("nameFr", customizedAttributeMap.get("name_fr"));
				obj.put("sort", customizedAttributeMap.get("sort_number"));
				JSONArray valueArr = JSONArray.fromObject( customizedAttributeMap.get("json_service_string"));
//				String[] values =  customizedAttributeMap.get("bvalue").toString().split(",");
//				
//				for(String value:values) {
//					 
//					String key = customizedAttributeMap.get("id").toString() + value; 
//					if(attributeValueNameMap.containsKey(key)) {
//						Map<String,Object> valueMap = (Map<String, Object>) attributeValueNameMap.get(key);
//						logger.error(valueMap.toString());
//						JSONObject valueObj = new JSONObject();
//						valueObj.put("key", value);
//						valueObj.put("name", valueMap.get("name")); 
//						valueObj.put("price", valueMap.get("price")); 
//						valueObj.put("imageUrl", valueMap.get("image_url")); 
//						valueArr.add(valueObj);
//					}
//				}
				obj.put("value", valueArr);
				set_list.add(obj);
			} 
			jsonProduct.put("setList", set_list); 
	}
	
	
	
	/**
	  * 添加商品自定义属性
	  * @param jsonProduct
	  */
	public void addCatalogProductAttributeValue(JSONObject jsonProduct,JsonConfig config) {
			
			List<CatalogProductAttributeValue> catalogProductAttributeValues = catalogProductAttributeValueMapper.findCatalogProductAttributeValue(jsonProduct.getInt("id"));
			JSONArray jsonCatalogProductAttributeValues = JSONArray.fromObject(catalogProductAttributeValues); 
			jsonProduct.put("catalogProductAttributeValues", jsonCatalogProductAttributeValues);
	}
	
	/**
	  * 添加商品视频
	  * @param jsonProduct
	  */
	public void addCatalogProductVideo(JSONObject jsonProduct,JsonConfig config) {
			List<CatalogProductEntityVideo> catalogProductEntityVideos = catalogProductEntityVideoMapper.findCatalogProductEntityVideoList(jsonProduct.getInt("id"));
			JSONArray jsonCatalogProductEntityVideos = JSONArray.fromObject(catalogProductEntityVideos);
			jsonProduct.put("catalogProductEntityVideos", jsonCatalogProductEntityVideos);
	}
	/**
	  * 添加商品图片
	  * @param jsonProduct
	  */
	public void addCatalogProductImage(JSONObject jsonProduct,JsonConfig config) {
			List<CatalogProductEntityImage> minCatalogProductEntityImages = catalogProductEntityImageMapper.findCatalogProductEntityImageList(jsonProduct.getInt("id"));
			JSONArray jsonCatalogProductEntityImages = JSONArray.fromObject(minCatalogProductEntityImages);
			jsonProduct.put("catalogProductEntityImages", jsonCatalogProductEntityImages);
	}
	/**
	  * 添加商品规格
	  * @param jsonProduct
	  */
	public void addCatalogProductSpec(JSONObject jsonProduct,JsonConfig config,String countryId) {
			
		List<CatalogProductSku> catalogProductSkus = catalogProductSkuMapper.findCatalogProductSkuList(jsonProduct.getInt("id"),countryId);
		
		List<CatalogProductSpec> catalogProductSpecList = catalogProductSpecMapper.findCatalogProductSpecList(jsonProduct.getInt("id"));
		JSONArray jsonArrSpec = new JSONArray(); 
		for(CatalogProductSpec catalogProductSpec:catalogProductSpecList){
			List<CatalogProductSpecValue> catalogProductSpecValues = catalogProductSpecValueMapper.findCatalogProductSpecValueList(catalogProductSpec.getId(),countryId);
			JSONObject jsonCatalogProductSpec = JSONObject.fromObject(catalogProductSpec); //商品规格
			JSONArray jsonArrCatalogProductSpecValue = JSONArray.fromObject(catalogProductSpecValues);//商品规格值
			JSONArray jsonArrCatalogProductSpecValueNew = new JSONArray();
			for(int i=0;i<jsonArrCatalogProductSpecValue.size();i++){
				JSONObject specVaue = jsonArrCatalogProductSpecValue.getJSONObject(i);
				specVaue.put("description", "");
				for(CatalogProductSku catalogProductSku:catalogProductSkus){
					if(catalogProductSku.getSkuName().equals(specVaue.getString("name"))){
						if(catalogProductSku.getDescription()!=null){
							specVaue.put("description", catalogProductSku.getDescription());
						}else{
							specVaue.put("description", "");
						}
						specVaue.put("status", catalogProductSku.getStatus());
						break;
					}
				}
				if(specVaue.get("status")!=null&&"1".equals(  specVaue.getString("status"))) {
					jsonArrCatalogProductSpecValueNew.add(specVaue);
				}
				
			}
			jsonCatalogProductSpec.put("catalogProductSpecValueList", jsonArrCatalogProductSpecValueNew);//商品规格 添加商品规格值
			//jsonCatalogProductSpec.put("catalogProductSpecValueList", jsonArrCatalogProductSpecValue);//商品规格 添加商品规格值
			jsonArrSpec.add(jsonCatalogProductSpec);//商品规格组
		}
		jsonProduct.put("catalogProductSpecList", jsonArrSpec);
		
		List<CatalogProductSku> catalogProductSkuList = new ArrayList<CatalogProductSku>();
		
		for(CatalogProductSku catalogProductSku:catalogProductSkus){
			if(catalogProductSku.getStatus()==1){
				catalogProductSkuList.add(catalogProductSku);
			}
		}
		
		jsonProduct.put("catalogProductSkuList", catalogProductSkuList);
			
	}
	
	/**
	 * 添加捆绑销售资料
	 * @param JSONObject
	 */
	public void addCatalogProductGroupSell(JSONObject jsonProduct,JsonConfig config,int languageId,int currencyId,String countryId ) {
		//logger.info("addCatalogProductGroupSell:"+jsonProduct.toString());
		if(jsonProduct.getInt("type")==1){
			return;
		}
		
		if(jsonProduct.get("requestType")!=null){
			return;
		} 
		
		jsonProduct.put("catalogProductGroupSell", new JSONObject());
		//判断是捆绑销售groupSellId
		if(jsonProduct.get("groupSellId") !=null){
			int groupSellId = jsonProduct.getInt("groupSellId");
			CatalogProductGroupSell cGroupSellEntity = catalogProductGroupSellEntityMapper.findCatalogProductGroupSellEntity(groupSellId);
			if(cGroupSellEntity!=null){
				JSONObject jsonCatalogProductGroupSell = JSONObject.fromObject(cGroupSellEntity);  
				List<CatalogProductGroupSellLinkProduct> groupLinkProductList = catalogProductGroupSellLinkProductMapper.findCatalogProductGroupSellLinkProductList(groupSellId);
				JSONArray jsonArr = new JSONArray();
				for(CatalogProductGroupSellLinkProduct cSellLinkProduct:groupLinkProductList){
					CatalogProductEntity catalogProductEntity = catalogProductEntityMapper.findCatalogProductEntity(cSellLinkProduct.getProductId());
					if(catalogProductEntity!=null){
						if(catalogProductEntity.getType()==1||catalogProductEntity.getType()==3){
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
	 * @param JSONObject
	 */
	public void addCatalogProductRecommended(JSONObject jsonProduct,JsonConfig config,int languageId,int currencyId,String countryId ) {
		List<CatalogProductRecommended> catalogProductRecommendeds =  catalogProductRecommendedMapper.findCatalogProductRecommendedList(jsonProduct.getInt("id"));
		JSONArray recommendArray = new JSONArray();
		for(CatalogProductRecommended catalogProductRecommended:catalogProductRecommendeds) {
	 
			BigDecimal sellPrice =  systemCurrencyService.getAmountByCurrencyId(catalogProductRecommended.getSellPrice(), currencyId);
			BigDecimal discountAmount =  systemCurrencyService.getAmountByCurrencyId(catalogProductRecommended.getDiscountAmount(), currencyId);
			JSONObject obj = JSONObject.fromObject(catalogProductRecommended);
			obj.put("spu", catalogProductRecommended.getSpu());
			obj.put("recommendType", "sgtj");
			obj.put("customizedType", catalogProductRecommended.getCustomizedType());
			if(catalogProductRecommended.getSpu().split("\\|\\|").length==2) {
				obj.put("spu_customized", catalogProductRecommended.getSpu().split("\\|\\|")[1]);	
			}else {
				obj.put("spu_customized", catalogProductRecommended.getSpu().split("\\|\\|")[0]);	
			}
			obj.put("item_id", catalogProductRecommended.getId());		
			obj.put("remoteUrl", catalogProductRecommended.getSmallImage());	
			obj.put("sellPrice", sellPrice);	
			obj.put("discountAmount", discountAmount );		
			recommendArray.add(obj);
		} 
		
		jsonProduct.put("catalogProductRecommendeds", recommendArray);
	}
    public static void main(String[] args) { 
    	String spu = "cdd||MMC-bps_MSMC-moissanitewhite_SSMC-whitestone";
    	String[] spuStrings = spu.split("\\|\\|");
    	System.out.println(spuStrings[0]);
    }
	
}
