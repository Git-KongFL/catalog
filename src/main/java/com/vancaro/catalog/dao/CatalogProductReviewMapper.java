package com.vancaro.catalog.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.vancaro.catalog.entity.CatalogProductReview;
/**
 * <b> catalog_product_review </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:33:09
 */
@Mapper
public interface CatalogProductReviewMapper {

	@Select("select  "
		 + " id as id, " /**  商品评论ID  */
		 + " store_id as storeId, " /**  店铺ID  */
		 + " product_id as productId, " /**  商品编码：记录此次评价针对的商品条码，如果评价类型为“订单评价”则为空  */
		 + " score as score, " /**  商品打分:1~5针对“商品评价”中的商品情况打分，如果评价类型为“订单评价”则为空  */
		 + " description as description, " /**  评价描述:客户对商品的描述，如果评价类型为“商品评价”则为空  */
		 + " num_of_like as numOfLike, " /**  点赞数  */
		 + " status as status, " /**  评价状态: 0:未审核, 1:已审核  */
		 + " user_id as userId, " /**  评价用户ID  */
		 + " deleted_status as deletedStatus, " /**  是否删除:0:否1:是  */
		 + " create_time as createTime, " /**  创建时间  */
		 + " update_time as updateTime " /**  更新时间  */
		+ " from catalog_product_review  where id = #{id}   and  deleted_status = 0 and status =1 ")
	CatalogProductReview findCatalogProductReview( @Param("id") int id);

}