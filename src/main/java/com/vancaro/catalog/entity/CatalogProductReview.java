package com.vancaro.catalog.entity;
import java.util.Date;
/**
 * <b> catalog_product_review </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:33:09
 */
public class CatalogProductReview{
	/**
	 * 商品评论ID
	 */
	private Integer id;
	/**
	 * 店铺ID
	 */
	private Integer storeId;
	/**
	 * 商品编码：记录此次评价针对的商品条码，如果评价类型为“订单评价”则为空
	 */
	private Integer productId;
	/**
	 * 商品打分:1~5针对“商品评价”中的商品情况打分，如果评价类型为“订单评价”则为空
	 */
	private Integer score;
	/**
	 * 评价描述:客户对商品的描述，如果评价类型为“商品评价”则为空
	 */
	private String description;
	/**
	 * 点赞数
	 */
	private Integer numOfLike;
	/**
	 * 评价状态: 0:未审核, 1:已审核
	 */
	private Integer status;
	/**
	 * 评价用户ID
	 */
	private Integer userId;
	/**
	 * 是否删除:0:否1:是
	 */
	private Integer deletedStatus;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 商品评论ID
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 商品评论ID
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 店铺ID
	 * 
	 * @return storeId
	 */
	public Integer getStoreId() {
		return storeId;
	}
	/**
	 * 店铺ID
	 * 
	 * @param storeId
	 */
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	/**
	 * 商品编码：记录此次评价针对的商品条码，如果评价类型为“订单评价”则为空
	 * 
	 * @return productId
	 */
	public Integer getProductId() {
		return productId;
	}
	/**
	 * 商品编码：记录此次评价针对的商品条码，如果评价类型为“订单评价”则为空
	 * 
	 * @param productId
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	/**
	 * 商品打分:1~5针对“商品评价”中的商品情况打分，如果评价类型为“订单评价”则为空
	 * 
	 * @return score
	 */
	public Integer getScore() {
		return score;
	}
	/**
	 * 商品打分:1~5针对“商品评价”中的商品情况打分，如果评价类型为“订单评价”则为空
	 * 
	 * @param score
	 */
	public void setScore(Integer score) {
		this.score = score;
	}
	/**
	 * 评价描述:客户对商品的描述，如果评价类型为“商品评价”则为空
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 评价描述:客户对商品的描述，如果评价类型为“商品评价”则为空
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 点赞数
	 * 
	 * @return numOfLike
	 */
	public Integer getNumOfLike() {
		return numOfLike;
	}
	/**
	 * 点赞数
	 * 
	 * @param numOfLike
	 */
	public void setNumOfLike(Integer numOfLike) {
		this.numOfLike = numOfLike;
	}
	/**
	 * 评价状态: 0:未审核, 1:已审核
	 * 
	 * @return status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 评价状态: 0:未审核, 1:已审核
	 * 
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 评价用户ID
	 * 
	 * @return userId
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * 评价用户ID
	 * 
	 * @param userId
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * 是否删除:0:否1:是
	 * 
	 * @return deletedStatus
	 */
	public Integer getDeletedStatus() {
		return deletedStatus;
	}
	/**
	 * 是否删除:0:否1:是
	 * 
	 * @param deletedStatus
	 */
	public void setDeletedStatus(Integer deletedStatus) {
		this.deletedStatus = deletedStatus;
	}
	/**
	 * 创建时间
	 * 
	 * @return createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 创建时间
	 * 
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 更新时间
	 * 
	 * @return updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 更新时间
	 * 
	 * @param updateTime
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
 

}