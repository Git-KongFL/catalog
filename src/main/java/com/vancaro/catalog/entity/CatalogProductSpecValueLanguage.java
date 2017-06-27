package com.vancaro.catalog.entity;
import java.util.Date;
/**
 * <b> catalog_product_spec_value_language </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:34:04
 */
public class CatalogProductSpecValueLanguage{
	/**
	 * 商品规格属性值翻译表ID
	 */
	private Integer id;
	/**
	 * 商品规格属性ID
	 */
	private Integer productSpecValueId;
	/**
	 * 语言ID
	 */
	private Integer languageId;
	/**
	 * 属性值
	 */
	private String value;
	/**
	 * 规格ID
	 */
	private Integer specId;
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
	 * 商品规格属性值翻译表ID
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 商品规格属性值翻译表ID
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 商品规格属性ID
	 * 
	 * @return productSpecValueId
	 */
	public Integer getProductSpecValueId() {
		return productSpecValueId;
	}
	/**
	 * 商品规格属性ID
	 * 
	 * @param productSpecValueId
	 */
	public void setProductSpecValueId(Integer productSpecValueId) {
		this.productSpecValueId = productSpecValueId;
	}
	/**
	 * 语言ID
	 * 
	 * @return languageId
	 */
	public Integer getLanguageId() {
		return languageId;
	}
	/**
	 * 语言ID
	 * 
	 * @param languageId
	 */
	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
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
	 * 规格ID
	 * 
	 * @return specId
	 */
	public Integer getSpecId() {
		return specId;
	}
	/**
	 * 规格ID
	 * 
	 * @param specId
	 */
	public void setSpecId(Integer specId) {
		this.specId = specId;
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