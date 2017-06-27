package com.vancaro.catalog.entity;
import java.util.Date;
/**
 * <b> catalog_product_review_image </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:33:19
 */
public class CatalogProductReviewImage{
	/**
	 * 商品评论图片ID
	 */
	private Integer id;
	/**
	 * 所属品论ID
	 */
	private Integer productReviewId;
	/**
	 * 图片url
	 */
	private String imageUrl;
	/**
	 * 顺序
	 */
	private Integer position;
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
	 * 商品评论图片ID
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 商品评论图片ID
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 所属品论ID
	 * 
	 * @return productReviewId
	 */
	public Integer getProductReviewId() {
		return productReviewId;
	}
	/**
	 * 所属品论ID
	 * 
	 * @param productReviewId
	 */
	public void setProductReviewId(Integer productReviewId) {
		this.productReviewId = productReviewId;
	}
	/**
	 * 图片url
	 * 
	 * @return imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	/**
	 * 图片url
	 * 
	 * @param imageUrl
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	/**
	 * 顺序
	 * 
	 * @return position
	 */
	public Integer getPosition() {
		return position;
	}
	/**
	 * 顺序
	 * 
	 * @param position
	 */
	public void setPosition(Integer position) {
		this.position = position;
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