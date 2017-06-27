package com.vancaro.catalog.entity;
import java.util.Date;
/**
 * <b> catalog_product_group_sell_language </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 15:43:47
 */
public class CatalogProductGroupSellLanguage{
	/**
	 * 捆绑名翻译ID
	 */
	private Integer id;
	/**
	 * 捆绑名ID
	 */
	private Integer productGroupSellId;
	/**
	 * 语言ID
	 */
	private Integer languageId;
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
	 * 捆绑名翻译ID
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 捆绑名翻译ID
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
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