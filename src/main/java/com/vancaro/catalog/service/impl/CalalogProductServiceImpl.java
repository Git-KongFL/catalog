package com.vancaro.catalog.service.impl;

import java.util.List;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.JSONArray;
import com.alibaba.dubbo.config.annotation.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.vancaro.catalog.dao.CatalogProductBundledLinkMapper;
import com.vancaro.catalog.dao.CatalogProductEntityDiscountMapper;
import com.vancaro.catalog.dao.CatalogProductEntityImageMapper;
import com.vancaro.catalog.dao.CatalogProductEntityMapper;
import com.vancaro.catalog.dao.CatalogProductEntityVideoMapper;
import com.vancaro.catalog.dao.CatalogProductGroupSellEntityMapper;
import com.vancaro.catalog.dao.CatalogProductGroupSellLinkProductMapper;
import com.vancaro.catalog.dao.CatalogProductSpecMapper;
import com.vancaro.catalog.dao.CatalogProductSpecValueMapper;
import com.vancaro.catalog.dao.RedisDao;
import com.vancaro.catalog.entity.CatalogProductBundledLink;
import com.vancaro.catalog.entity.CatalogProductEntity;
import com.vancaro.catalog.entity.CatalogProductEntityDiscount;
import com.vancaro.catalog.entity.CatalogProductEntityImage;
import com.vancaro.catalog.entity.CatalogProductEntityVideo;
import com.vancaro.catalog.entity.CatalogProductGroupSell;
import com.vancaro.catalog.entity.CatalogProductGroupSellLinkProduct;
import com.vancaro.catalog.entity.CatalogProductSpec;
import com.vancaro.catalog.entity.CatalogProductSpecValue;
import com.vancaro.catalog.service.CalalogProductService;
import com.vancaro.util.JsonDateValueProcessor;
import com.vancaro.util.JsonUtil;

@Service(group="calalogProductService", version="1.0")
public class CalalogProductServiceImpl implements CalalogProductService {

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
	RedisDao redisDao;
	

	private final static Logger logger = LoggerFactory.getLogger(CalalogProductServiceImpl.class); 
	
	public String getCatalogProductById(int id ) {
		try{
			String resultStr =  redisDao.getValue("product"+id);
			logger.info(resultStr);
			if(resultStr!=null){
				return resultStr;
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		JSONObject resultObj = JsonUtil.createJSONObject();
		CatalogProductEntity catalogProductEntity = catalogProductEntityMapper.findCatalogProductEntity(id);
		//判断商品是否存在
		if(catalogProductEntity==null){
			return JsonUtil.modifyJSONObject(resultObj,10001, "商品不存在！").toString();
		}
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());  
		JSONObject jsonProduct = JSONObject.fromObject(catalogProductEntity, jsonConfig);  
		
		//添加捆绑销售资料
		this.addCatalogProductGroupSell(jsonProduct,jsonConfig);
		//添加组合商品资料
		this.addCatalogProductIsBundle(jsonProduct,jsonConfig);
		//添加商品规格
		this.addCatalogProductSpec(jsonProduct,jsonConfig);
		//添加商品图片
		this.addCatalogProductImage(jsonProduct,jsonConfig);
		//添加商品视频
		this.addCatalogProductVideo(jsonProduct,jsonConfig);
		
		resultObj.put("product", jsonProduct);
		try{
			redisDao.setKey("product"+id,resultObj.toString());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return resultObj.toString();  
	}

	/**
	  * 添加商品规格
	  * @param jsonProduct
	  */
	public void addCatalogProductVideo(JSONObject jsonProduct,JsonConfig config) {
			List<CatalogProductEntityVideo> catalogProductEntityVideos = catalogProductEntityVideoMapper.findCatalogProductEntityVideoList(jsonProduct.getInt("id"));
			JSONArray jsonCatalogProductEntityVideos = JSONArray.fromObject(catalogProductEntityVideos);
			jsonProduct.put("catalogProductEntityVideos", jsonCatalogProductEntityVideos);
	}
	/**
	  * 添加商品规格
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
		if(jsonProduct.getInt("isBundle")==0){ 
			List<CatalogProductSpec> catalogProductSpecList = catalogProductSpecMapper.findCatalogProductSpecList(jsonProduct.getInt("id"));
			JSONArray jsonArrSpec = new JSONArray();
			for(CatalogProductSpec catalogProductSpec:catalogProductSpecList){
				List<CatalogProductSpecValue> catalogProductSpecValues = catalogProductSpecValueMapper.findCatalogProductSpecValueList(catalogProductSpec.getId());
				JSONObject jsonCatalogProductSpec = JSONObject.fromObject(catalogProductSpec); //商品规格
				JSONArray jsonArrCatalogProductSpecValue = JSONArray.fromObject(catalogProductSpecValues);//商品规格值
				jsonCatalogProductSpec.put("catalogProductSpecValueList", jsonArrCatalogProductSpecValue);//商品规格 添加商品规格值
				jsonArrSpec.add(jsonCatalogProductSpec);//商品规格组
			}
			jsonProduct.put("catalogProductSpecList", jsonArrSpec);
		}
	}
	/**
	  * 添加组合商品资料
	  * @param jsonProduct
	  */
	public void addCatalogProductIsBundle(JSONObject jsonProduct,JsonConfig config) {
		if(jsonProduct.getInt("isBundle")==1){
			List<CatalogProductBundledLink> catalogProductBundledLinks  = catalogProductBundledLinkMapper.findCatalogProductBundledLinkList(jsonProduct.getInt("id"));
			JSONArray jsonArr = new JSONArray();
			for(CatalogProductBundledLink catalogProductBundledLink:catalogProductBundledLinks){
				CatalogProductEntity childCatalogProduct = catalogProductEntityMapper.findCatalogProductEntity(catalogProductBundledLink.getChildProductId());
				if(childCatalogProduct!=null){
					CatalogProductEntityDiscount cEntityDiscount = catalogProductEntityDiscountMapper.findCatalogProductEntityDiscount(childCatalogProduct.getId());
					JSONObject cProduct = JSONObject.fromObject(childCatalogProduct, config); //商品
					JSONObject cProductDiscount = JSONObject.fromObject(cEntityDiscount, config);//商品折扣属性
					cProduct.put("productDiscount", cProductDiscount);//商品 添加商品折扣属性
					List<CatalogProductSpec> catalogProductSpecList = catalogProductSpecMapper.findCatalogProductSpecList(childCatalogProduct.getId());
					JSONArray jsonArrSpec = new JSONArray();
					for(CatalogProductSpec catalogProductSpec:catalogProductSpecList){
						List<CatalogProductSpecValue> catalogProductSpecValues = catalogProductSpecValueMapper.findCatalogProductSpecValueList(catalogProductSpec.getId());
						JSONObject jsonCatalogProductSpec = JSONObject.fromObject(catalogProductSpec); //商品规格
						JSONArray jsonArrCatalogProductSpecValue = JSONArray.fromObject(catalogProductSpecValues);//商品规格值
						jsonCatalogProductSpec.put("catalogProductSpecValueList", jsonArrCatalogProductSpecValue);//商品规格 添加商品规格值
						jsonArrSpec.add(jsonCatalogProductSpec);//商品规格组
					}
					cProduct.put("catalogProductSpecList", jsonArrSpec); //商品 添加商品规格组
				}
				jsonProduct.put("bundleProductList", jsonArr);//组合商品
			}
		} 
	}
	/**
	 * 添加捆绑销售资料
	 * @param JSONObject
	 */
	public void addCatalogProductGroupSell(JSONObject jsonProduct,JsonConfig config) {
		//判断是捆绑销售groupSellId
		if(jsonProduct.get("groupSellId") !=null){
			int groupSellId = jsonProduct.getInt("groupSellId");
			CatalogProductGroupSell cGroupSellEntity = catalogProductGroupSellEntityMapper.findCatalogProductGroupSellEntity(groupSellId);
			if(cGroupSellEntity!=null){
				JSONObject jsonCatalogProductGroupSell = JSONObject.fromObject(cGroupSellEntity);  
				List<CatalogProductGroupSellLinkProduct> groupLinkProductList = catalogProductGroupSellLinkProductMapper.findCatalogProductGroupSellLinkProductList(groupSellId);
				JSONArray jsonArr = new JSONArray();
				for(CatalogProductGroupSellLinkProduct cSellLinkProduct:groupLinkProductList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("product_id", cSellLinkProduct.getProductId());
					CatalogProductEntityImage catalogProductEntityImage = catalogProductEntityImageMapper.findCatalogProductEntityImageByProductId(cSellLinkProduct.getProductId());
					if(catalogProductEntityImage!=null){
						jsonObj.put("remote_url", catalogProductEntityImage.getRemoteUrl());
					}
					jsonArr.add(jsonObj);
				}
				jsonCatalogProductGroupSell.put("list", jsonArr);
				jsonProduct.put("catalogProductGroupSell", jsonCatalogProductGroupSell);
			}
		}
	}

}
