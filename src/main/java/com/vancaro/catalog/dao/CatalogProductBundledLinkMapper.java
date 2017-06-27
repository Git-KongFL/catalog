package com.vancaro.catalog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.vancaro.catalog.entity.CatalogProductBundledLink;
/**
 * <b> catalog_product_bundled_link </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 18:35:49
 */
@Mapper
public interface CatalogProductBundledLinkMapper {

	@Select("select  "
		 + " id as id, " /**  全局规格ID  */
		 + " parent_product_id as parentProductId, " /**  套装编号  */
		 + " child_product_id as childProductId  " /**  套装子商品编号  */
//		 + " create_time as createTime  " /**  创建时间  */
//		 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
//		 + " update_time as updateTime " /**  更新时间  */
		+ " from catalog_product_bundled_link  where id = #{id} and  deleted_status = 0  ")
	CatalogProductBundledLink findCatalogProductBundledLink( @Param("id") int id);

	
	@Select("select  "
			 + " id as id, " /**  全局规格ID  */
			 + " parent_product_id as parentProductId, " /**  套装编号  */
			 + " child_product_id as childProductId  " /**  套装子商品编号  */
//			 + " create_time as createTime  " /**  创建时间  */
//			 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
//			 + " update_time as updateTime " /**  更新时间  */
			+ " from catalog_product_bundled_link  where parent_product_id = #{parentProductId}  and  deleted_status = 0 ")
	List<CatalogProductBundledLink> findCatalogProductBundledLinkList( @Param("parentProductId") int parent_product_id);
	
}