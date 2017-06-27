package com.vancaro.catalog.dao;



import org.apache.ibatis.annotations.*;

import com.vancaro.catalog.entity.CatalogProductEntity;
 

/**
 * 
 * @author 作者 denghaihui
 * @version 创建时间：2017年6月26日 下午2:19:59 
 *
 */
@Mapper
public interface CatalogProductEntityMapper {
	@Select("select  "
			 + " id as id, " /**  商品ID  */
			 + " store_id as storeId, " /**  店铺编号  */
			 + " store_default as storeDefault, " /**  默认店铺：1默认店铺，0非默认店铺  */
			 + " is_bundle as isBundle, " /**  是否套装销售商品  */
			 + " design as design, " /**  设计编码  */
			 + " group_sell_id as groupSellId, " /**  销售编码  */
			 + " spu as spu, " /**  商品资料的唯一码，唯一值，商品的货号，ERP唯一编码  */
			 + " type as type, " /**  商品类型： 1：普通商品 2：折扣商品  */
			 + " name as name, " /**  商品名称  */
			 + " status as status, " /**  商品状态： 0：下架 1：上架  */
			 + " is_backorder as isBackorder, " /**  是否预定：  0:否,1:是  */
			 + " visibility as visibility, " /**  可见范围：0:不可见，1:只在目录中，2:只在搜索结果中，3:目录和搜索，4:只能直接访问  */
			 + " manage_stock as manageStock, " /**  是否开启库存  0:否 1:是 (如果否，则商品默认为无限库存，并且不接受接口的同步库存)  */
			 + " sell_price as sellPrice, " /**  销售价格  */
			 + " original_price as originalPrice, " /**  商品原始价格  */
			 + " meta_title as metaTitle, " /**  SEO标题  */
			 + " meta_keywords as metaKeywords, " /**  SEO关键词  */
			 + " meta_description as metaDescription, " /**  SEO描述  */
			 + " description as description, " /**  商品展示描述  */
			 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
			 + " create_time as createTime, " /**  创建时间  */
			 + " update_time as updateTime  " /**  更新时间  */
			+ " from catalog_product_entity  where id = #{id}  and  deleted_status = 0  and status =1  ")
  CatalogProductEntity findCatalogProductEntity( @Param("id") int id);
	
	
     
}