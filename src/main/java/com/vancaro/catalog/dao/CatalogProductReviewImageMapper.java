package com.vancaro.catalog.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.vancaro.catalog.entity.CatalogProductReviewImage;
/**
 * <b> catalog_product_review_image </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:33:19
 */
@Mapper
public interface CatalogProductReviewImageMapper {

	@Select("select  "
		 + " id as id, " /**  商品评论图片ID  */
		 + " product_review_id as productReviewId, " /**  所属品论ID  */
		 + " image_url as imageUrl, " /**  图片url  */
		 + " position as position, " /**  顺序  */
		 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
		 + " create_time as createTime, " /**  创建时间  */
		 + " update_time as updateTime " /**  更新时间  */
		+ " from catalog_product_review_image  where id = #{id}   and  deleted_status = 0  ")
	CatalogProductReviewImage findCatalogProductReviewImage( @Param("id") int id);

}