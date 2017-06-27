package com.vancaro.catalog.entity;
import java.util.Date;
/**
 * <b> catalog_product_bundled_link </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 18:35:49
 */
public class CatalogProductBundledLink{
	/**
	 * 全局规格ID
	 */
	private Integer id;
	/**
	 * 套装编号
	 */
	private Integer parentProductId;
	/**
	 * 套装子商品编号
	 */
	private Integer childProductId;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 是否删除:0:否1:是
	 */
	private Integer deletedStatus;
	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 全局规格ID
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 全局规格ID
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 套装编号
	 * 
	 * @return parentProductId
	 */
	public Integer getParentProductId() {
		return parentProductId;
	}
	/**
	 * 套装编号
	 * 
	 * @param parentProductId
	 */
	public void setParentProductId(Integer parentProductId) {
		this.parentProductId = parentProductId;
	}
	/**
	 * 套装子商品编号
	 * 
	 * @return childProductId
	 */
	public Integer getChildProductId() {
		return childProductId;
	}
	/**
	 * 套装子商品编号
	 * 
	 * @param childProductId
	 */
	public void setChildProductId(Integer childProductId) {
		this.childProductId = childProductId;
	}
	/**
	 * 创建时间
	 * 
	 * @return createTime
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * 创建时间
	 * 
	 * @param createTime
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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