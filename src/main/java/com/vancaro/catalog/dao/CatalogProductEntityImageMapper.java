package com.vancaro.catalog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.vancaro.catalog.entity.CatalogProductEntityImage;
/**
 * <b> catalog_product_entity_image </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:25:55
 */
@Mapper
public interface CatalogProductEntityImageMapper {

	@Select("select  "
		 + " id as id, " /**  shang  */
		 + " product_id as productId, " /**  商品ID  */
		 + " product_spec_value_id as productSpecValueId, " /**  商品规格属性值ID  */
		 + " res_type as resType, " /**  资源类型：1:小图 2:中图 3:大图  */
		 + " url as url, " /**  本地url：记录此信息的在本地服务器上面的地址  */
		 + " remote_url as remoteUrl  " /**  远程url：记录此信息的在远程服务器上面的地址  */
//		 + " status as status ," /**  是否默认：0:否 1:是  */
//		 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
//		 + " create_time as createTime, " /**  创建时间  */
//		 + " update_time as updateTime " /**  更新时间  */
		+ " from catalog_product_entity_image  where product_id = #{productId}    and  deleted_status = 0 and status =1 ")
	List<CatalogProductEntityImage> findCatalogProductEntityImageList( @Param("productId") int productId);
	
	@Select("select  "
			 + " id as id, " /**  shang  */
			 + " product_id as productId, " /**  商品ID  */
			 + " product_spec_value_id as productSpecValueId, " /**  商品规格属性值ID  */
			 + " res_type as resType, " /**  资源类型：1:小图 2:中图 3:大图  */
			 + " url as url, " /**  本地url：记录此信息的在本地服务器上面的地址  */
			 + " remote_url as remoteUrl " /**  远程url：记录此信息的在远程服务器上面的地址  */
//			 + " status as status ," /**  是否默认：0:否 1:是  */
//			 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
//			 + " create_time as createTime, " /**  创建时间  */
//			 + " update_time as updateTime " /**  更新时间  */
			+ " from catalog_product_entity_image  where product_id = #{productId} and  deleted_status = 0 and status =1  and res_type = 1 oder by create_time limit 1 ")
	CatalogProductEntityImage findCatalogProductEntityImageByProductId( @Param("productId") int productId);

}