package com.vancaro.catalog.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.vancaro.catalog.entity.CatalogProductSpecLanguage;
/**
 * <b> catalog_product_spec_language </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:33:47
 */
@Mapper
public interface CatalogProductSpecLanguageMapper {

	@Select("select  "
		 + " id as id, " /**  商品规格翻译ID  */
		 + " product_spec_id as productSpecId, " /**  商品规格ID  */
		 + " store_id as storeId, " /**  店铺ID  */
		 + " product_id as productId, " /**  商品ID  */
		 + " name as name, " /**  名称  */
		 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
		 + " create_time as createTime, " /**  创建时间  */
		 + " update_time as updateTime " /**  更新时间  */
		+ " from catalog_product_spec_language  where id = #{id}  and  deleted_status = 0   ")
	CatalogProductSpecLanguage findCatalogProductSpecLanguage( @Param("id") int id);

}