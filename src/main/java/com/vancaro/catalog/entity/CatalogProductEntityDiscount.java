package com.vancaro.catalog.entity;
import java.math.BigDecimal;
import java.util.Date;
/**
 * <b> catalog_product_entity_discount </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:20:51
 */
public class CatalogProductEntityDiscount{
	/**
	 * 商品折扣表ID
	 */
	private Integer id;
	/**
	 * 商品ID
	 */
	private Integer productId;
	/**
	 * 店铺ID
	 */
	private Integer storeId;
	/**
	 * 折扣金额
	 */
	private BigDecimal discountAmount;
	/**
	 * 折扣
	 */
	private BigDecimal discountPercentage;
	/**
	 * 折扣开始时间
	 */
	private Date discountStartTime;
	/**
	 * 折扣结束时间
	 */
	private Date discountEndTime;
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
	 * 商品折扣表ID
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 商品折扣表ID
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 商品ID
	 * 
	 * @return productId
	 */
	public Integer getProductId() {
		return productId;
	}
	/**
	 * 商品ID
	 * 
	 * @param productId
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
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
	 * 折扣金额
	 * 
	 * @return discountAmount
	 */
	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}
	/**
	 * 折扣金额
	 * 
	 * @param discountAmount
	 */
	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}
	/**
	 * 折扣
	 * 
	 * @return discountPercentage
	 */
	public BigDecimal getDiscountPercentage() {
		return discountPercentage;
	}
	/**
	 * 折扣
	 * 
	 * @param discountPercentage
	 */
	public void setDiscountPercentage(BigDecimal discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	/**
	 * 折扣开始时间
	 * 
	 * @return discountStartTime
	 */
	public Date getDiscountStartTime() {
		return discountStartTime;
	}
	/**
	 * 折扣开始时间
	 * 
	 * @param discountStartTime
	 */
	public void setDiscountStartTime(Date discountStartTime) {
		this.discountStartTime = discountStartTime;
	}
	/**
	 * 折扣结束时间
	 * 
	 * @return discountEndTime
	 */
	public Date getDiscountEndTime() {
		return discountEndTime;
	}
	/**
	 * 折扣结束时间
	 * 
	 * @param discountEndTime
	 */
	public void setDiscountEndTime(Date discountEndTime) {
		this.discountEndTime = discountEndTime;
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