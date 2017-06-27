package com.vancaro.catalog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.vancaro.catalog.entity.CatalogProductSpec;
/**
 * <b> catalog_product_spec </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 20:39:23
 */
@Mapper
public interface CatalogProductSpecMapper {

	@Select("select  "
		 + " id as id, " /**  商品规格ID  */
		 + " store_id as storeId, " /**  店铺ID  */
		 + " product_id as productId, " /**  商品ID  */
		 + " name as name, " /**  规格名称  */
		 + " value as value, " /**  规格值  */
		 + " type as type, " /**  类型  */
		 + " sort as sort, " /**  顺序  */
		 + " description as description, " /**  描述  */
		 + " status as status  " /**  状态：0:不启用,1:启用  */
    	 + " from catalog_product_spec  where id = #{id}   and  deleted_status = 0  ")
	CatalogProductSpec findCatalogProductSpec( @Param("id") int id);
	
	
	@Select("select  "
			 + " id as id, " /**  商品规格ID  */
			 + " store_id as storeId, " /**  店铺ID  */
			 + " product_id as productId, " /**  商品ID  */
			 + " name as name, " /**  规格名称  */
			 + " value as value, " /**  规格值  */
			 + " type as type, " /**  类型  */
			 + " sort as sort, " /**  顺序  */
			 + " description as description, " /**  描述  */
			 + " status as status  " /**  状态：0:不启用,1:启用  */
			+ " from catalog_product_spec  where productId = #{productId}   and  deleted_status = 0  ")
	List<CatalogProductSpec> findCatalogProductSpecList( @Param("productId") int productId);

}