package com.vancaro.catalog.entity;
import java.util.Date;
/**
 * <b> catalog_product_spec_value </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 20:39:34
 */
public class CatalogProductSpecValue{
	/**
	 * 商品规格属性值ID
	 */
	private Integer id;
	/**
	 * 商品规格ID
	 */
	private Integer productSpecId;
	/**
	 * 规格名称
	 */
	private String name;
	/**
	 * 规格值
	 */
	private String value;
	/**
	 * 状态：0:不亲用,1:启用
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
	 * 商品规格属性值ID
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 商品规格属性值ID
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
	 * 状态：0:不亲用,1:启用
	 * 
	 * @return status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 状态：0:不亲用,1:启用
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