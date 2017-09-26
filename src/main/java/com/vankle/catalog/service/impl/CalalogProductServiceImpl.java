package com.vankle.catalog.service.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.JSONArray;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vankle.code.annotation.LanguageAnnotation;
import com.vankle.catalog.dao.CatalogProductBundledLinkMapper;
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
import com.vankle.catalog.dao.CatalogProductAttributeValueMapper;
import com.vankle.catalog.entity.CatalogProductAttributeValue;
import com.vankle.catalog.entity.CatalogProductBundledLink;
import com.vankle.catalog.entity.CatalogProductEntity;
import com.vankle.catalog.entity.CatalogProductEntityDiscount;
import com.vankle.catalog.entity.CatalogProductEntityImage;
import com.vankle.catalog.entity.CatalogProductEntityLanguage;
import com.vankle.catalog.entity.CatalogProductRecommended;
import com.vankle.catalog.entity.CatalogProductEntityVideo;
import com.vankle.catalog.entity.CatalogProductGroupSell;
import com.vankle.catalog.entity.CatalogProductGroupSellLinkProduct;
import com.vankle.catalog.entity.CatalogProductSku;
import com.vankle.catalog.entity.CatalogProductSpec;
import com.vankle.catalog.entity.CatalogProductSpecValue;
import com.vankle.catalog.service.CalalogProductService;
import com.vankle.code.constants.RedisConstants;
import com.vankle.code.constants.VankleConstants;
import com.vankle.code.util.JsonDateValueProcessor;
import com.vankle.code.util.JsonUtils;
import com.vankle.code.util.ListUtils;
import com.vankle.code.util.VankleUtils; 
import com.vankle.system.service.SystemCurrencyService;
import com.vankle.system.service.SystemService;
import com.vankle.code.dao.RedisDao;


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
	
	
	@Reference(group = "systemService", version = "1.0")
	private SystemService systemService;
	
	
	@Autowired
	RedisDao redisDao;
	private final static Logger logger = LoggerFactory.getLogger(CalalogProductServiceImpl.class); 
	
	
	/*
	 * (non-Javadoc)
	 * @see com.vankle.catalog.service.CalalogProductService#getCatalogProductByParamJson(java.lang.String)
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
		Object requestType = paramObj.get("requestType");
		resultObj.put("requestType", requestType);
		String resultStr = this.getProductLanguageInfo(resultObj,productId,languageId,currencyId);
		
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
			productObj.put("currency", JSONObject.fromObject(jsonCurrency));
			//商品信息价格换算
			BigDecimal originalPrice =  systemCurrencyService.getAmountByCurrencyId(new BigDecimal(productObj.getString("originalPrice")), currencyId);
			productObj.put("originalPrice", originalPrice);
			BigDecimal sellPrice =  systemCurrencyService.getAmountByCurrencyId(new BigDecimal(productObj.getString("sellPrice")), currencyId);
			productObj.put("sellPrice", sellPrice);
			
			//商品折扣信息价格换算
			JSONObject catalogProductEntityDiscount = productObj.getJSONObject("catalogProductEntityDiscount");
			BigDecimal discountAmount =  systemCurrencyService.getAmountByCurrencyId(new BigDecimal(catalogProductEntityDiscount.getString("discountAmount")), currencyId);
			catalogProductEntityDiscount.put("discountAmount", discountAmount);
			 
			return resultObj.toString();
		}
	}
	
	public String getProductLanguageInfo(JSONObject resultObj, int productId,int languageId,int currencyId){
		
		String resultStr =  redisDao.getValue(RedisConstants.VANKLE_REDIS_CATALOG_PRODUCT+productId+languageId);
		logger.info(resultStr);
		if(resultStr!=null){
			return resultStr;
		}
		//查看是否存在 English
		String resultStrEn = redisDao.getValue(RedisConstants.VANKLE_REDIS_CATALOG_PRODUCT+productId+1);
		logger.info(resultStrEn);
		if(resultStrEn!=null){
			String resultStrLanguage = this.getLanguageByJosnProduct(resultStrEn, productId, languageId);
			redisDao.setKey(RedisConstants.VANKLE_REDIS_CATALOG_PRODUCT+productId+languageId,resultStrLanguage);
			return resultStrLanguage;
		} 
		 
		CatalogProductEntity catalogProductEntity = catalogProductEntityMapper.findCatalogProductEntity(productId);
		//判断商品是否存在
		if(catalogProductEntity==null){
			 JsonUtils.modifyJSONObject(resultObj,VankleConstants.VANKLE_CODE_FAIL_10002, VankleConstants.VANKLE_CODE_FAIL_10002_MESSAGE).toString();
			 redisDao.setKey(RedisConstants.VANKLE_REDIS_CATALOG_PRODUCT+productId+languageId,resultObj.toString());
			 return resultObj.toString();
		}

		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());  
		JSONObject jsonProduct = JSONObject.fromObject(catalogProductEntity, jsonConfig);  
		jsonProduct.put("requestType", resultObj.get("requestType"));
		
		//
		String jsonCurrency =  systemService.getCurrencyEntity(currencyId);
		jsonProduct.put("currency", JSONObject.fromObject(jsonCurrency));
		
		//添加折扣信息
		this.addCatalogProductDiscount(jsonProduct,jsonConfig);
		//添加商品规格
		this.addCatalogProductSpec(jsonProduct,jsonConfig);
		//添加商品图片
		this.addCatalogProductImage(jsonProduct,jsonConfig);
		//添加商品视频
		this.addCatalogProductVideo(jsonProduct,jsonConfig);
		//添加捆绑销售资料
		this.addCatalogProductGroupSell(jsonProduct,jsonConfig,languageId,currencyId);
		//添加商品推荐商品
		this.addCatalogProductRecommended(jsonProduct,jsonConfig);
		
		
		//添加商品自定义属性
		//this.addCatalogProductAttributeValue(jsonProduct, jsonConfig);
		//		//添加组合商品资料
		//		this.addCatalogProductIsBundle(jsonProduct,jsonConfig);
		
		resultObj.put("data", jsonProduct);
		redisDao.setKey(RedisConstants.VANKLE_REDIS_CATALOG_PRODUCT+productId+1,resultObj.toString());
		if(languageId == 1){
			return resultObj.toString();  
		}else{
			String resultStrLanguage = this.getLanguageByJosnProduct(resultObj.toString(), productId, languageId);
			redisDao.setKey(RedisConstants.VANKLE_REDIS_CATALOG_PRODUCT+productId+languageId,resultStrLanguage);
			return resultStrLanguage;
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
	            	logger.info(field.getName());   
	                productObj.put(field.getName(), jsonProductLang.getString(field.getName()));
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
	public void addCatalogProductSku(JSONObject jsonProduct,JsonConfig config) {
		List<CatalogProductSku> catalogProductSkus = catalogProductSkuMapper.findCatalogProductSkuList(jsonProduct.getInt("id"));
		JSONArray jsonCatalogProductSkus = JSONArray.fromObject(catalogProductSkus);
		jsonProduct.put("catalogProductSkus", jsonCatalogProductSkus);
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
	public void addCatalogProductSpec(JSONObject jsonProduct,JsonConfig config) {
//		if(jsonProduct.getInt("isBundle")==0){
//			
//			
			List<List<String>> nameList = new ArrayList<List<String>>();
			List<CatalogProductSpec> catalogProductSpecList = catalogProductSpecMapper.findCatalogProductSpecList(jsonProduct.getInt("id"));
			JSONArray jsonArrSpec = new JSONArray();
			List<String> skuKeyList =  new ArrayList<String>();
			for(CatalogProductSpec catalogProductSpec:catalogProductSpecList){
				List<CatalogProductSpecValue> catalogProductSpecValues = catalogProductSpecValueMapper.findCatalogProductSpecValueList(catalogProductSpec.getId());
				JSONObject jsonCatalogProductSpec = JSONObject.fromObject(catalogProductSpec); //商品规格
				JSONArray jsonArrCatalogProductSpecValue = JSONArray.fromObject(catalogProductSpecValues);//商品规格值
				jsonCatalogProductSpec.put("catalogProductSpecValueList", jsonArrCatalogProductSpecValue);//商品规格 添加商品规格值
				jsonArrSpec.add(jsonCatalogProductSpec);//商品规格组

				List<String> naList = new ArrayList<String>();

				for(CatalogProductSpecValue specValueEntity: catalogProductSpecValues){
					naList.add(specValueEntity.getName());
				}
				nameList.add(naList);
				skuKeyList.add(catalogProductSpec.getName());
			}
			jsonProduct.put("catalogProductSpecList", jsonArrSpec);
			List<CatalogProductSku> catalogProductSkus = catalogProductSkuMapper.findCatalogProductSkuList(jsonProduct.getInt("id"));
			jsonProduct.put("catalogProductSkuList", catalogProductSkus);
			
//			List<String> listName = ListUtils.circulate(nameList);
//			List<JSONObject> skuList = new ArrayList<JSONObject>();
//			for(String values:listName){
//
//				JSONObject skuObj = new JSONObject();
//				String[] valueArray = values.split(" ");
//				JSONObject skuKeyObj = new JSONObject();
//				for(int i=0;i<skuKeyList.size();i++){
//					skuKeyObj.put(skuKeyList.get(i), valueArray[i]);
//				}
//				skuObj.put("key", skuKeyObj);
//				skuObj.put("skuName", values);
//				System.out.println(skuObj.toString());
//				skuList.add(skuObj);
//			}
//			jsonProduct.put("skuList", skuList);
			
//		}
	}
//	/**
//	  * 添加组合商品资料
//	  * @param jsonProduct
//	  */
//	public void addCatalogProductIsBundle(JSONObject jsonProduct,JsonConfig config) {
//		if(jsonProduct.getInt("isBundle")==1){
//			List<CatalogProductBundledLink> catalogProductBundledLinks  = catalogProductBundledLinkMapper.findCatalogProductBundledLinkList(jsonProduct.getInt("id"));
//			JSONArray jsonArr = new JSONArray();
//			for(CatalogProductBundledLink catalogProductBundledLink:catalogProductBundledLinks){
//				CatalogProductEntity childCatalogProduct = catalogProductEntityMapper.findCatalogProductEntity(catalogProductBundledLink.getChildProductId());
//				if(childCatalogProduct!=null){
//					CatalogProductEntityDiscount cEntityDiscount = catalogProductEntityDiscountMapper.findCatalogProductEntityDiscount(childCatalogProduct.getId());
//					JSONObject cProduct = JSONObject.fromObject(childCatalogProduct, config); //商品
//					JSONObject cProductDiscount = JSONObject.fromObject(cEntityDiscount, config);//商品折扣属性
//					cProduct.put("productDiscount", cProductDiscount);//商品 添加商品折扣属性
//					List<CatalogProductSpec> catalogProductSpecList = catalogProductSpecMapper.findCatalogProductSpecList(childCatalogProduct.getId());
//					JSONArray jsonArrSpec = new JSONArray();
//					for(CatalogProductSpec catalogProductSpec:catalogProductSpecList){
//						List<CatalogProductSpecValue> catalogProductSpecValues = catalogProductSpecValueMapper.findCatalogProductSpecValueList(catalogProductSpec.getId());
//						JSONObject jsonCatalogProductSpec = JSONObject.fromObject(catalogProductSpec); //商品规格
//						JSONArray jsonArrCatalogProductSpecValue = JSONArray.fromObject(catalogProductSpecValues);//商品规格值
//						jsonCatalogProductSpec.put("catalogProductSpecValueList", jsonArrCatalogProductSpecValue);//商品规格 添加商品规格值
//						jsonArrSpec.add(jsonCatalogProductSpec);//商品规格组
//					}
//					cProduct.put("catalogProductSpecList", jsonArrSpec); //商品 添加商品规格组
//				}
//				jsonProduct.put("bundleProductList", jsonArr);//组合商品
//			}
//		} 
//	}
	
	/**
	 * 添加捆绑销售资料
	 * @param JSONObject
	 */
	public void addCatalogProductGroupSell(JSONObject jsonProduct,JsonConfig config,int languageId,int currencyId) {
		if(jsonProduct.getInt("type")==1){
			return;
		}
		
		if(jsonProduct.get("requestType")!=null){
			return;
		}
		
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
					if(catalogProductEntity.getType()==1||catalogProductEntity.getType()==3){
						JSONObject paramObj = new JSONObject();
						paramObj.put("productId", cSellLinkProduct.getProductId());
						paramObj.put("languageId", languageId);
						paramObj.put("currencyId", currencyId);
						paramObj.put("requestType", 1);
						String resout = this.getCatalogProductInfoByParamJson(paramObj.toString());
						JSONObject resoutJson = JSONObject.fromObject(resout);
						jsonArr.add(resoutJson.get("data"));
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
	public void addCatalogProductRecommended(JSONObject jsonProduct,JsonConfig config) {
		List<CatalogProductRecommended> catalogProductRecommendeds =  catalogProductRecommendedMapper.findCatalogProductRecommendedList(jsonProduct.getInt("id"));
		jsonProduct.put("catalogProductRecommendeds", JSONArray.fromObject(catalogProductRecommendeds,config));
	}
	
	
}
