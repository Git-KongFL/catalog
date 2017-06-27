package com.vancaro.catalog.dao;


import org.apache.ibatis.annotations.*; 
import com.vancaro.catalog.entity.CatalogProductGroupSellLanguage;

/**
 * 
 * @author 作者 denghaihui
 * @version 创建时间：2017年6月26日 下午2:19:59 
 *
 */
@Mapper
public interface CatalogProductGroupSellLanguageEntityMapper {
	
	@Select("select  "
			 + " id as id, " /**  捆绑名翻译ID  */
			 + " product_group_sell_id as productGroupSellId, " /**  捆绑名ID  */
			 + " language_id as languageId, " /**  语言ID  */
			 + " name as name, " /**  名称  */
			 + " description as description, " /**  描述  */
			 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
			 + " create_time as createTime, " /**  创建时间  */
			 + " update_time as updateTime " /**  更新时间  */
			+ " from catalog_product_group_sell_language  where id = #{id}  and  deleted_status = 0   ")
	CatalogProductGroupSellLanguage findCatalogProductGroupSellLanguageEntity( @Param("id") int id);
	
	
     
}