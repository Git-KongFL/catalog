package com.vancaro.catalog.entity;
import java.util.Date;
/**
 * <b> catalog_product_group_sell </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 15:34:51
 */
public class CatalogProductGroupSell{
	/**
	 * 捆绑名ID
	 */
	private Integer id;
	/**
	 * 店铺ID
	 */
	private Integer storeId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 描述
	 */
	private String description;
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
	 * 捆绑名ID
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 捆绑名ID
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