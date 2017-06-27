package com.vancaro.catalog.entity;
import java.util.Date;
/**
 * <b> catalog_product_spec_language </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:33:47
 */
public class CatalogProductSpecLanguage{
	/**
	 * 商品规格翻译ID
	 */
	private Integer id;
	/**
	 * 商品规格ID
	 */
	private Integer productSpecId;
	/**
	 * 店铺ID
	 */
	private Integer storeId;
	/**
	 * 商品ID
	 */
	private Integer productId;
	/**
	 * 名称
	 */
	private String name;
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
	 * 商品规格翻译ID
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 商品规格翻译ID
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 商品规格ID
	 * 
	 * @return productSpecId
	 */
	public Integer getProductSpecId() {
		return productSpecId;
	}
	/**
	 * 商品规格ID
	 * 
	 * @param productSpecId
	 */
	public void setProductSpecId(Integer productSpecId) {
		this.productSpecId = productSpecId;
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
	 * 名称
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 名称
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
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