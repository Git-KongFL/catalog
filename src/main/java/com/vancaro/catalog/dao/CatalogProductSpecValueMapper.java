package com.vancaro.catalog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.vancaro.catalog.entity.CatalogProductSpecValue;
/**
 * <b> catalog_product_spec_value </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 20:39:34
 */
@Mapper
public interface CatalogProductSpecValueMapper {

	@Select("select  "
		 + " id as id, " /**  商品规格属性值ID  */
		 + " product_spec_id as productSpecId, " /**  商品规格ID  */
		 + " name as name, " /**  规格名称  */
		 + " value as value, " /**  规格值  */
		 + " status as status " /**  状态：0:不亲用,1:启用  */
//		 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
//		 + " create_time as createTime, " /**  创建时间  */
//		 + " update_time as updateTime  " /**  更新时间  */
		+ " from catalog_product_spec_value  where id = #{id}  and  deleted_status = 0 and status =1  ")
	CatalogProductSpecValue findCatalogProductSpecValue( @Param("id") int id);


	@Select("select  "
		 + " id as id, " /**  商品规格属性值ID  */
		 + " product_spec_id as productSpecId, " /**  商品规格ID  */
		 + " name as name, " /**  规格名称  */
		 + " value as value, " /**  规格值  */
		 + " status as status " /**  状态：0:不亲用,1:启用  */
//		 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
//		 + " create_time as createTime, " /**  创建时间  */
//		 + " update_time as updateTime  " /**  更新时间  */
		+ " from catalog_product_spec_value  where productSpecId = #{productSpecId}   and  deleted_status = 0  and status =1  ")
	List<CatalogProductSpecValue> findCatalogProductSpecValueList( @Param("productSpecId") int productSpecId);
	
	
}