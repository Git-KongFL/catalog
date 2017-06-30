package com.vancaro;
/** 
*
* @author 作者 denghaihui
* @version 创建时间：2017年6月28日 下午2:35:27 
* 类说明 
*
*/

import java.io.IOException;

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

import com.vancaro.dao.RedisDao;

import net.sf.json.JSONObject;

import com.vancaro.catalog.dao.CatalogProductEntityMapper;
import com.vancaro.catalog.entity.CatalogProductEntity;
import com.vancaro.catalog.service.CalalogProductService;

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
	
	@Test
	public void testProduct(){ 
		String paramJson = "{productId:'2',storeId:'1',languageId:'1',currencyId:'1'}" ;
		String resout = calalogProductService.getCatalogProductInfoByParamJson(paramJson);
		System.out.println(resout);
		logger.info(resout);
	}
	@Test
	public void testRedis(){
		redisDao.setKey("name","forezp");
		redisDao.setKey("age","11");
		logger.info(redisDao.getValue("name"));
		logger.info(redisDao.getValue("age"));
	}
	@Test
	public void testSolr() throws Exception{
		HttpSolrClient  clientCatalog = new HttpSolrClient("http://123.57.206.102:8983/solr/catalog_product");;
		CatalogProductEntity catalogProductEntity = catalogProductEntityMapper.findCatalogProductEntity(2);
		 SolrInputDocument input = new SolrInputDocument();
		 input.addField("id", catalogProductEntity.getId(), 1.0f);
		 input.addField("name", catalogProductEntity.getName());
		 clientCatalog.add(input);
		 clientCatalog.commit();
	} 
}
