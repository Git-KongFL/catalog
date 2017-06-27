package com.vancaro.catalog.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.vancaro.catalog.entity.CatalogProductEntityLanguage;
/**
 * <b> catalog_product_entity_language </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:30:53
 */
@Mapper
public interface CatalogProductEntityLanguageMapper {

	@Select("select  "
		 + " id as id, " /**  商品信息翻译ID  */
		 + " product_id as productId, " /**  product_id  */
		 + " spu as spu, " /**  商品资料的唯一码，商品的货号，ERP唯一编码  */
		 + " store_id as storeId, " /**  店铺ID  */
		 + " language_id as languageId, " /**  语言ID  */
		 + " name as name, " /**  商品名称  */
		 + " meta_title as metaTitle, " /**  SEO标题  */
		 + " meta_keywords as metaKeywords, " /**  SEO关键词  */
		 + " meta_description as metaDescription, " /**  SEO描述  */
		 + " description as description, " /**  商品展示描述  */
		 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
		 + " create_time as createTime, " /**  创建时间  */
		 + " update_time as updateTime " /**  更新时间  */
		+ " from catalog_product_entity_language  where id = #{id}  and  deleted_status = 0   ")
	CatalogProductEntityLanguage findCatalogProductEntityLanguage( @Param("id") int id);

}