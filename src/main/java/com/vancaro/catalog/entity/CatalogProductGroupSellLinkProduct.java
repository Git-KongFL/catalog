package com.vancaro.catalog.entity;
import java.util.Date;
/**
 * <b> catalog_product_group_sell_link_product </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 15:52:45
 */
public class CatalogProductGroupSellLinkProduct{
	/**
	 * 商品捆绑名关联ID
	 */
	private Integer id;
	/**
	 * 商品ID
	 */
	private Integer productId;
	/**
	 * 捆绑名ID
	 */
	private Integer productGroupSellId;
	/**
	 * 位置
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
	 * 商品捆绑名关联ID
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 商品捆绑名关联ID
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
	 * 捆绑名ID
	 * 
	 * @return productGroupSellId
	 */
	public Integer getProductGroupSellId() {
		return productGroupSellId;
	}
	/**
	 * 捆绑名ID
	 * 
	 * @param productGroupSellId
	 */
	public void setProductGroupSellId(Integer productGroupSellId) {
		this.productGroupSellId = productGroupSellId;
	}
	/**
	 * 位置
	 * 
	 * @return position
	 */
	public Integer getPosition() {
		return position;
	}
	/**
	 * 位置
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