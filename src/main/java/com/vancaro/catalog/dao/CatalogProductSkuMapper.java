package com.vancaro.catalog.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.vancaro.catalog.entity.CatalogProductSku;
/**
 * <b> catalog_product_sku </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:33:29
 */
@Mapper
public interface CatalogProductSkuMapper {

	@Select("select  "
		 + " id as id, " /**  SKU货品表ID  */
		 + " product_id as productId, " /**  商品ID  */
		 + " sku as sku, " /**  规格码：SKU的唯一码，同时将其作为条形码使用货品的货号(以商品的货号加横线加数字组成)  */
		 + " stock_qty as stockQty, " /**  库存数：当开启库存后，必填。并展示在商品明细页。随着客户的下单，此库存数也随着变化。  */
		 + " stock_min_qty as stockMinQty, " /**  最小库存：当开启库存后，必填。当商品小于最小库存数时，B2C端自动下架。当受库存同步影响后，库存数如果大于最小库存数，则自动上架。  */
		 + " price_difference as priceDifference, " /**  参差价格：和SPU上面的商品价格相比，参差价格  */
		 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
		 + " create_time as createTime, " /**  创建时间  */
		 + " update_time as updateTime " /**  更新时间  */
		+ " from catalog_product_sku  where id = #{id}  and  deleted_status = 0   ")
	CatalogProductSku findCatalogProductSku( @Param("id") int id);

}