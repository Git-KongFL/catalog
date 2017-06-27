package com.vancaro.catalog.entity;
import java.util.Date;
/**
 * <b> catalog_product_spec </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 20:39:23
 */
public class CatalogProductSpec{
	/**
	 * 商品规格ID
	 */
	private Integer id;
	/**
	 * 店铺ID
	 */
	private Integer storeId;
	/**
	 * 商品ID
	 */
	private Integer productId;
	/**
	 * 规格名称
	 */
	private String name;
	/**
	 * 规格值
	 */
	private String value;
	/**
	 * 类型
	 */
	private Integer type;
	/**
	 * 顺序
	 */
	private Integer sort;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 状态：0:不启用,1:启用
	 */
	private Integer status;
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
	 * 商品规格ID
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 商品规格ID
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
	 * 规格名称
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 规格名称
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 规格值
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * 规格值
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 类型
	 * 
	 * @return type
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 类型
	 * 
	 * @param type
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 顺序
	 * 
	 * @return sort
	 */
	public Integer getSort() {
		return sort;
	}
	/**
	 * 顺序
	 * 
	 * @param sort
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	/**
	 * 描述
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 描述
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 状态：0:不启用,1:启用
	 * 
	 * @return status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 状态：0:不启用,1:启用
	 * 
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
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