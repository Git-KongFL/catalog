package com.vancaro.catalog.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.vancaro.catalog.entity.CatalogProductSpecValueLanguage;
/**
 * <b> catalog_product_spec_value_language </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:34:04
 */
@Mapper
public interface CatalogProductSpecValueLanguageMapper {

	@Select("select  "
		 + " id as id, " /**  商品规格属性值翻译表ID  */
		 + " product_spec_value_id as productSpecValueId, " /**  商品规格属性ID  */
		 + " language_id as languageId, " /**  语言ID  */
		 + " value as value, " /**  属性值  */
		 + " spec_id as specId, " /**  规格ID  */
		 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
		 + " create_time as createTime, " /**  创建时间  */
		 + " update_time as updateTime " /**  更新时间  */
		+ " from catalog_product_spec_value_language  where id = #{id}  and  deleted_status = 0   ")
	CatalogProductSpecValueLanguage findCatalogProductSpecValueLanguage( @Param("id") int id);

}