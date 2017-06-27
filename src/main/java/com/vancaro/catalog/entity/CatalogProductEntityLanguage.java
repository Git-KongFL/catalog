package com.vancaro.catalog.entity;
import java.util.Date;
/**
 * <b> catalog_product_entity_language </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:30:53
 */
public class CatalogProductEntityLanguage{
	/**
	 * 商品信息翻译ID
	 */
	private Integer id;
	/**
	 * product_id
	 */
	private Integer productId;
	/**
	 * 商品资料的唯一码，商品的货号，ERP唯一编码
	 */
	private String spu;
	/**
	 * 店铺ID
	 */
	private Integer storeId;
	/**
	 * 语言ID
	 */
	private Integer languageId;
	/**
	 * 商品名称
	 */
	private String name;
	/**
	 * SEO标题
	 */
	private String metaTitle;
	/**
	 * SEO关键词
	 */
	private String metaKeywords;
	/**
	 * SEO描述
	 */
	private String metaDescription;
	/**
	 * 商品展示描述
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
	 * 商品信息翻译ID
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 商品信息翻译ID
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * product_id
	 * 
	 * @return productId
	 */
	public Integer getProductId() {
		return productId;
	}
	/**
	 * product_id
	 * 
	 * @param productId
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	/**
	 * 商品资料的唯一码，商品的货号，ERP唯一编码
	 * 
	 * @return spu
	 */
	public String getSpu() {
		return spu;
	}
	/**
	 * 商品资料的唯一码，商品的货号，ERP唯一编码
	 * 
	 * @param spu
	 */
	public void setSpu(String spu) {
		this.spu = spu;
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
	 * 商品名称
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 商品名称
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * SEO标题
	 * 
	 * @return metaTitle
	 */
	public String getMetaTitle() {
		return metaTitle;
	}
	/**
	 * SEO标题
	 * 
	 * @param metaTitle
	 */
	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}
	/**
	 * SEO关键词
	 * 
	 * @return metaKeywords
	 */
	public String getMetaKeywords() {
		return metaKeywords;
	}
	/**
	 * SEO关键词
	 * 
	 * @param metaKeywords
	 */
	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}
	/**
	 * SEO描述
	 * 
	 * @return metaDescription
	 */
	public String getMetaDescription() {
		return metaDescription;
	}
	/**
	 * SEO描述
	 * 
	 * @param metaDescription
	 */
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
	/**
	 * 商品展示描述
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 商品展示描述
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