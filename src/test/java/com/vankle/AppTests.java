package com.vankle;
/** 
*
* @author 作者 denghaihui
* @version 创建时间：2017年6月28日 下午2:35:27 
* 类说明 
*
*/

import java.io.IOException;
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

import com.vankle.code.dao.RedisDao;

import net.sf.json.JSONObject;

import com.vankle.catalog.dao.CatalogProductEntityMapper;
import com.vankle.catalog.dao.CatalogProductSpecValueMapper;
import com.vankle.catalog.entity.CatalogProductEntity;
import com.vankle.catalog.entity.CatalogProductSpecValue;
import com.vankle.catalog.service.CalalogCategoryService;
import com.vankle.catalog.service.CalalogProductService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTests {

	public static Logger logger= LoggerFactory.getLogger(AppTests.class);
	@Autowired
	CalalogProductService calalogProductService;
	@Autowired
	RedisDao redisDao;
	@Autowired
	CatalogProductEntityMapper catalogProductEntityMapper;
	@Autowired
	CatalogProductSpecValueMapper catalogProductSpecValueMapper;
	@Autowired
	CalalogCategoryService calalogCategoryService;
//	@Test
//	public void testAddProductSpecValue(){
//		CatalogProductSpecValue catalogProductSpecVaule = new CatalogProductSpecValue();
//		catalogProductSpecVaule.setId(5);
//		catalogProductSpecVaule.setValue("10");
//		catalogProductSpecVaule.setName("color");
//		catalogProductSpecVaule.setCreateTime(new Date());
//		catalogProductSpecVaule.setProductSpecId(1);
//		catalogProductSpecVaule.setStatus(1);
//		catalogProductSpecVaule.setDeletedStatus(1);
//		catalogProductSpecValueMapper.addCatalogProductSpecValue(catalogProductSpecVaule);
//		System.out.println(catalogProductSpecVaule.toString());
//	}
	
	@Test
	public void testCategory(){ 
		String paramJson = "{storeId:'1'}" ;
		String resout = calalogCategoryService.getCatalogCategoryInfoByParamJson(paramJson);
		System.out.println(resout);
		logger.info(resout);
	}
	
//	@Test
//	public void testProduct(){ 
//		String paramJson = "{productId:'2',storeId:'1',languageId:'2',currencyId:'1'}" ;
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
}
