package com.vancaro.catalog.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.vancaro.catalog.entity.CatalogProductEntityDiscount;
/**
 * <b> catalog_product_entity_discount </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:20:51
 */
@Mapper
public interface CatalogProductEntityDiscountMapper {

	@Select("select  "
		 + " id as id, " /**  商品折扣表ID  */
		 + " product_id as productId, " /**  商品ID  */
		 + " store_id as storeId, " /**  店铺ID  */
		 + " discount_amount as discountAmount, " /**  折扣金额  */
		 + " discount_percentage as discountPercentage, " /**  折扣  */
		 + " discount_start_time as discountStartTime, " /**  折扣开始时间  */
		 + " discount_end_time as discountEndTime " /**  折扣结束时间  */
//		 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
//		 + " create_time as createTime, " /**  创建时间  */
//		 + " update_time as updateTime  " /**  更新时间  */
		+ " from catalog_product_entity_discount  where id = #{id} and  deleted_status = 0   ")
	CatalogProductEntityDiscount findCatalogProductEntityDiscount( @Param("id") int id);

}