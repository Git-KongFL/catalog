package com.vancaro.catalog.dao;


import org.apache.ibatis.annotations.*; 
import com.vancaro.catalog.entity.CatalogProductGroupSell;
 

/**
 * 
 * @author 作者 denghaihui
 * @version 创建时间：2017年6月26日 下午2:19:59 
 *
 */
@Mapper
public interface CatalogProductGroupSellEntityMapper {
	@Select("select  "
			 + " id as id, " /**  捆绑名ID  */
//			 + " store_id as storeId, " /**  店铺ID  */
			 + " name as name, " /**  名称  */
			 + " description as description  " /**  描述  */
//			 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
//			 + " create_time as createTime, " /**  创建时间  */
//			 + " update_time as updateTime " /**  更新时间  */
			+ " from catalog_product_group_sell  where id = #{id}  and  deleted_status = 0   ")
	CatalogProductGroupSell findCatalogProductGroupSellEntity( @Param("id") int id);
	
	
     
}