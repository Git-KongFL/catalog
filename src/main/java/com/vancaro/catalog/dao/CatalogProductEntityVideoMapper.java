package com.vancaro.catalog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.vancaro.catalog.entity.CatalogProductEntityVideo;
/**
 * <b> catalog_product_entity_video </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:31:20
 */
@Mapper
public interface CatalogProductEntityVideoMapper {

	@Select("select  "
		 + " id as id, " /**  商品视频ID  */
		 + " product_id as productId, " /**  商品ID  */
		 + " product_spec_value_id as productSpecValueId, " /**  商品规格属性值ID  */
		 + " res_type as resType, " /**  资源来源  */
		 + " url as url, " /**  本地url：记录此信息的在本地服务器上面的地址  */
		 + " remote_url as remoteUrl, " /**  远程URL：记录此信息的在远程服务器上面的地址  */
		 + " status as status " /**  默认状态：0:否 1:是  */
//		 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
//		 + " create_time as createTime, " /**  创建时间  */
//		 + " update_time as updateTime " /**  更新时间  */
		+ " from catalog_product_entity_video  where product_id = #{productId}  and  deleted_status = 0    ")
	List<CatalogProductEntityVideo> findCatalogProductEntityVideoList( @Param("productId") int productId);

}