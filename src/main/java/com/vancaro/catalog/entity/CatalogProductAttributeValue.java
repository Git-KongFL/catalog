package com.vancaro.catalog.entity;
import java.util.Date;
/**
 * <b> catalog_product_attribute_value </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:34:27
 */
public class CatalogProductAttributeValue{
	/**
	 * 商品属性值ID
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
	 * 属性ID
	 */
	private Integer attributeId;
	/**
	 * 属性类型  1:单选,2:复选,3:下拉,4:输入框
	 */
	private Integer attributeType;
	/**
	 * 属性值
	 */
	private String value;
	/**
	 * 状态： 0:不启用 1:启用
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 是否删除:0:否1:是
	 */
	private Integer deletedStatus;

	/**
	 * 商品属性值ID
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 商品属性值ID
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
	 * 属性ID
	 * 
	 * @return attributeId
	 */
	public Integer getAttributeId() {
		return attributeId;
	}
	/**
	 * 属性ID
	 * 
	 * @param attributeId
	 */
	public void setAttributeId(Integer attributeId) {
		this.attributeId = attributeId;
	}
	/**
	 * 属性类型  1:单选,2:复选,3:下拉,4:输入框
	 * 
	 * @return attributeType
	 */
	public Integer getAttributeType() {
		return attributeType;
	}
	/**
	 * 属性类型  1:单选,2:复选,3:下拉,4:输入框
	 * 
	 * @param attributeType
	 */
	public void setAttributeType(Integer attributeType) {
		this.attributeType = attributeType;
	}
	/**
	 * 属性值
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * 属性值
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 状态： 0:不启用 1:启用
	 * 
	 * @return status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 状态： 0:不启用 1:启用
	 * 
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
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
 

}