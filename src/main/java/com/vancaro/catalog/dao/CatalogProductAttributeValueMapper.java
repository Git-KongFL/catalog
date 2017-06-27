package com.vancaro.catalog.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.vancaro.catalog.entity.CatalogProductAttributeValue;
/**
 * <b> catalog_product_attribute_value </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:34:27
 */
@Mapper
public interface CatalogProductAttributeValueMapper {

	@Select("select  "
		 + " id as id, " /**  商品属性值ID  */
		 + " product_id as productId, " /**  商品ID  */
		 + " store_id as storeId, " /**  店铺ID  */
		 + " attribute_id as attributeId, " /**  属性ID  */
		 + " attribute_type as attributeType, " /**  属性类型  1:单选,2:复选,3:下拉,4:输入框  */
		 + " value as value, " /**  属性值  */
		 + " status as status, " /**  状态： 0:不启用 1:启用  */
		 + " create_time as createTime, " /**  创建时间  */
		 + " update_time as updateTime " /**  更新时间  */
		 + " deleted_status as deletedStatus " /**  是否删除:0:否1:是  */
		+ " from catalog_product_attribute_value  where id = #{id} and  deletedStatus = 0  and status =1")
	CatalogProductAttributeValue findCatalogProductAttributeValue( @Param("id") int id);

}