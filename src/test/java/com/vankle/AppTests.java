package com.vankle;
/** 
*
* @author 作者 denghaihui
* @version 创建时间：2017年6月28日 下午2:35:27 
* 类说明 
*
*/

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.vankle.system.service.SystemCurrencyService;

import net.sf.json.JSONObject;

import com.vankle.catalog.dao.CatalogProductEntityMapper;
import com.vankle.catalog.dao.CatalogProductSpecValueMapper;
import com.vankle.catalog.entity.CatalogProductEntity;
import com.vankle.catalog.entity.CatalogProductSpecValue;
import com.vankle.catalog.service.CalalogCategoryService;
import com.vankle.catalog.service.CalalogProductReviewService;
import com.vankle.catalog.service.CalalogProductService;
import com.vankle.catalog.service.CalalogSearchService;
import com.vankle.catalog.service.CalalogVoteService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTests {

	public static Logger logger= LoggerFactory.getLogger(AppTests.class);
	@Autowired
	CalalogProductService calalogProductService;
	@Autowired
	CatalogProductEntityMapper catalogProductEntityMapper;
	@Autowired
	CatalogProductSpecValueMapper catalogProductSpecValueMapper;
	@Autowired
	CalalogCategoryService calalogCategoryService;
	@Autowired
	CalalogSearchService calalogSearchService;
	@Autowired
	SystemCurrencyService systemCurrencyService;
	@Autowired
	CalalogProductReviewService calalogProductReviewService;
	@Autowired
	CalalogVoteService calalogVoteService;
	
	
	
//	@Test
//	public void getCategoryProductInfoByParamJson(){ 
//		String paramJson = "{'categoryId':'302','pageIndex':1,'adp':null,'orderBy':{'dir':'desc','order':'Recommend','pathInfo':'302'},'storeId':1,'languageId':1,'currencyId':1,'prefixion':'en-nz','remoteIp':'127.0.0.1'}" ;
//		String resout = calalogCategoryService.getCategoryProductInfoByParamJson(paramJson);
//		System.out.println(resout);
//		logger.info(resout);
//	}
	
//	@Test
//	public void getCatalogCategoryInfoByParamJson(){ 
//		String paramJson = "{'productId':'10231','storeId':1,'languageId':1,'currencyId':1,'prefixion':'zh-us'}" ;
//		String resout = calalogCategoryService.getCatalogCategoryInfoByParamJson(paramJson);
//		System.out.println(resout);
//		logger.info(resout);	
//	}
	
	
//	
//	@Test
//	public void testCategoryProduct(){ 
//		String paramJson = "{'productId':'10231','prefixion':'zh-us','storeId':1,'languageId':1,'currencyId':1}" ;
//		String resout = calalogProductService.getCatalogProductInfoByItemId(paramJson);
//		System.out.println(resout);
//		logger.info(resout);
//	}

	
	@Test
	public void getCategorySearchByParamJson(){ 
		String paramJson = "{storeId:1,q:'pierres',languageId:4,currencyId:1,pageIndex:1,orderBy:{order:'price',dir:'desc'}}" ;
		String resout = calalogSearchService.getCategorySearchByParamJson(paramJson);
		System.out.println(resout);
		logger.info(resout);
	}
	
	
//	@Test
//	public void getCatalogCategoryInfoByParamJson(){ 
//		String paramJson = "{\"storeId\":\"1\",\"languageId\":\"1\",\"currencyId\":1}" ;
//		String resout = calalogCategoryService.getCatalogCategoryInfoByParamJson(paramJson);
//		System.out.println(resout);
//		logger.info(resout);
//	}
	
//2017-10-09 11:53:48.579
//2017-10-09 11:53:46.440

//	@Test
//	public void getCategoryVoteListByParamJson(){ 
//		String paramJson = "{languageId:1,currencyId:1,storeId:1,pageIndex:1}" ;
//		String resout = calalogVoteService.getCategoryVoteListByParamJson(paramJson);
//		System.out.println(resout);
//		logger.info(resout);
//	}
//	

//	@Test
//	public void getCategoryVoteInfoByParamJson(){ 
//		String paramJson = "{languageId:1,currencyId:1,storeId:1,voteId:1}" ;
//		String resout = calalogVoteService.getCategoryVoteInfoByParamJson(paramJson);
//		System.out.println(resout);
//		logger.info(resout);
//	}
	
	
//	@Test
//	public void setCategoryVoteByParamJson(){ 
//		String paramJson = "{languageId:1,currencyId:1,storeId:1,voteId:1,customerId:'cabb5e72a274463e8a2e8d5ac7964843',voteType:1}" ;
//		String resout = calalogVoteService.setCategoryVoteByParamJson(paramJson);
//		System.out.println(resout);
//		logger.info(resout);
//	}
	
	
//	@Test
//	public void testAddProductSpecValue(){
//		CatalogProductSpecValue catalogProductSpecVaule = new CatalogProductSpecValue();
//		catalogProductSpecVaule.setId(5);
//		catalogProductSpecVaule.setValue("10");
//		catalogProductSpecVaule.setName("color");
//		catalogProductSpecVaule.setCreateTime(new Date());
//		catalogProductSpecVaule.setProductSpecId(1);
//		catalogProductSpecVaule.setStatus(1);
//		catalogProductSpecVaule.setDeletedStatus(VankleConstants.VANKLE_DELETED_STATUS_1);
//		catalogProductSpecValueMapper.addCatalogProductSpecValue(catalogProductSpecVaule);
//		System.out.println(catalogProductSpecVaule.toString());
//	}
	
	
//	
//	@Test
//	public void testCurrency(){ 
//		BigDecimal out =  systemCurrencyService.getAmountByCurrencyId(new BigDecimal(7), 2);
//		System.out.println(out);
//		System.out.println();
//		System.out.println();
//		logger.info(out.toString());
//	}
	
	
	//http://123.57.206.102:8085/catalog/product/review
//	@Test
//	public void getCategoryProductReviewByParamJson(){ 
//		String paramJson = "{productId:10267,pageIndex:1,'storeId':1}" ;
//		String resout = calalogProductReviewService.getCategoryProductReviewByParamJson(paramJson);
//		System.out.println(resout);
//		logger.info(resout);
//	}


	
	
//	@Test
//	public void testCategoryProduct(){ 
//		String paramJson = "{storeId:1,categoryId:494,languageId:2,currencyId:1,pageIndex:1,orderBy:{order:'price',dir:'desc'}}" ;
//		String resout = calalogCategoryService.getCategoryProductInfoByParamJson(paramJson);
//		System.out.println(resout);
//		logger.info(resout);
//	}
	
	
//
//	@Test
//	public void testProduct(){ 
//		String paramJson = "{productId:367,languageId:1,currencyId:1,storeId:1}" ;
//		String resout = calalogProductService.getCatalogProductInfoByParamJson(paramJson);
//		System.out.println(resout);
//		logger.info(resout);
//	}
	
//	@Test
//	public void testRedis(){
//		redisDao.setKey("name","forezp");
//		redisDao.setKey("age","11");
//		logger.info(redisDao.getValue("name"));
//		logger.info(redisDao.getValue("age"));
//	}
//	@Test
//	public void testSolr() throws Exception{
//		HttpSolrClient  clientCatalog = new HttpSolrClient("http://123.57.206.102:8983/solr/catalog_product");;
//		CatalogProductEntity catalogProductEntity = catalogProductEntityMapper.findCatalogProductEntity(2);
//		 SolrInputDocument input = new SolrInputDocument();
//		 input.addField("id", catalogProductEntity.getId(), 1.0f);
//		 input.addField("name", catalogProductEntity.getName());
//		 clientCatalog.add(input);
//		 clientCatalog.commit();
//	} 
	
//
	public static void main(String[] args)
    {
		System.out.print("fr-Us".substring(3, 5).toUpperCase());
//		 String str = "e2NhdGVnb3J5SWQ6NyxsYW5ndWFnZUlkOjEsY3VycmVuY3lJZDoxLHN0ZXA6MzAsb2Zmc2V0OjB9IA==";
//		 byte[] bt = null;    
//		   try {    
//		       sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();    
//		       bt = decoder.decodeBuffer( str );    
//		   } catch (IOException e) {    
//		       e.printStackTrace();    
//		   }    
//		   String t = new String(bt);
//		   System.out.println(t);
    }
}
