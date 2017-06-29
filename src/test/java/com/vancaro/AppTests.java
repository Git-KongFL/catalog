package com.vancaro;
/** 
*
* @author 作者 denghaihui
* @version 创建时间：2017年6月28日 下午2:35:27 
* 类说明 
*
*/

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.vancaro.dao.RedisDao;

import net.sf.json.JSONObject;

import com.vancaro.catalog.service.CalalogProductService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTests {

	public static Logger logger= LoggerFactory.getLogger(AppTests.class);
	@Autowired
	CalalogProductService calalogProductService;
	@Autowired
	RedisDao redisDao;
	
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
}
