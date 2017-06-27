package com.vancaro.catalog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
 
import com.vancaro.catalog.entity.CatalogProductGroupSellLinkProduct;
/**
 * <b> catalog_product_group_sell_link_product </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:03:14
 */
@Mapper
public interface CatalogProductGroupSellLinkProductMapper {

	@Select("select  "
		 + " id as id, " /**  商品捆绑名关联ID  */
		 + " product_id as productId, " /**  商品ID  */
		 + " product_group_sell_id as productGroupSellId, " /**  捆绑名ID  */
		 + " position as position, " /**  位置  */
		 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
		 + " create_time as createTime, " /**  创建时间  */
		 + " update_time as updateTime " /**  更新时间  */
		+ " from catalog_product_group_sell_link_product  where id = #{id}  and  deleted_status = 0 ")
	
	CatalogProductGroupSellLinkProduct findCatalogProductGroupSellLinkProduct( @Param("id") int id);

	
	@Select("select  "
			 + " id as id, " /**  商品捆绑名关联ID  */
			 + " product_id as productId, " /**  商品ID  */
			 + " product_group_sell_id as productGroupSellId, " /**  捆绑名ID  */
			 + " position as position, " /**  位置  */
			 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
			 + " create_time as createTime, " /**  创建时间  */
			 + " update_time as updateTime " /**  更新时间  */
			+ " from catalog_product_group_sell_link_product  where product_group_sell_id = #{id}  and  deleted_status = 0  order by position ")
	List<CatalogProductGroupSellLinkProduct> findCatalogProductGroupSellLinkProductList( @Param("id") int id);
}