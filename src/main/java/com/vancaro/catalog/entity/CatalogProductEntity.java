package com.vancaro.catalog.entity;



 

import java.math.BigDecimal;
import java.util.Date;

/**
 * <b> 商品信息 catalog_product_entity </b>
 * 
 * @author dhh
 * @date 2017-06-25 09:47:03
 */
public class CatalogProductEntity{
 

	/**
	 * 商品ID
	 */
	private Integer id;
	/**
	 * 店铺编号
	 */
	private Integer storeId;
	/**
	 * 默认店铺：1默认店铺，0非默认店铺
	 */
	private Integer storeDefault;
	/**
	 * 是否套装销售商品
	 */
	private Integer isBundle;
	/**
	 * 设计编码
	 */
	private String design;
	/**
	 * 销售编码
	 */
	private Integer groupSellId;
	/**
	 * 商品资料的唯一码，唯一值，商品的货号，ERP唯一编码
	 */
	private String spu;
	/**
	 * 商品类型： 1：普通商品 2：折扣商品
	 */
	private Integer type;
	/**
	 * 商品名称
	 */
	private String name;
	/**
	 * 商品状态： 0：下架 1：上架
	 */
	private Integer status;
	/**
	 * 是否预定：  0:否,1:是
	 */
	private Integer isBackorder;
	/**
	 * 可见范围：0:不可见，1:只在目录中，2:只在搜索结果中，3:目录和搜索，4:只能直接访问
	 */
	private Integer visibility;
	/**
	 * 是否开启库存  0:否 1:是 (如果否，则商品默认为无限库存，并且不接受接口的同步库存)
	 */
	private Integer manageStock;
	/**
	 * 销售价格
	 */
	private BigDecimal sellPrice;
	/**
	 * 商品原始价格
	 */
	private BigDecimal originalPrice;
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
	 * 商品ID
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 商品ID
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 店铺编号
	 * 
	 * @return storeId
	 */
	public Integer getStoreId() {
		return storeId;
	}
	/**
	 * 店铺编号
	 * 
	 * @param storeId
	 */
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	/**
	 * 默认店铺：1默认店铺，0非默认店铺
	 * 
	 * @return storeDefault
	 */
	public Integer getStoreDefault() {
		return storeDefault;
	}
	/**
	 * 默认店铺：1默认店铺，0非默认店铺
	 * 
	 * @param storeDefault
	 */
	public void setStoreDefault(Integer storeDefault) {
		this.storeDefault = storeDefault;
	}
	/**
	 * 是否套装销售商品
	 * 
	 * @return isBundle
	 */
	public Integer getIsBundle() {
		return isBundle;
	}
	/**
	 * 是否套装销售商品
	 * 
	 * @param isBundle
	 */
	public void setIsBundle(Integer isBundle) {
		this.isBundle = isBundle;
	}
	/**
	 * 设计编码
	 * 
	 * @return design
	 */
	public String getDesign() {
		return design;
	}
	/**
	 * 设计编码
	 * 
	 * @param design
	 */
	public void setDesign(String design) {
		this.design = design;
	}
	/**
	 * 销售编码
	 * 
	 * @return groupSellId
	 */
	public Integer getGroupSellId() {
		return groupSellId;
	}
	/**
	 * 销售编码
	 * 
	 * @param groupSellId
	 */
	public void setGroupSellId(Integer groupSellId) {
		this.groupSellId = groupSellId;
	}
	/**
	 * 商品资料的唯一码，唯一值，商品的货号，ERP唯一编码
	 * 
	 * @return spu
	 */
	public String getSpu() {
		return spu;
	}
	/**
	 * 商品资料的唯一码，唯一值，商品的货号，ERP唯一编码
	 * 
	 * @param spu
	 */
	public void setSpu(String spu) {
		this.spu = spu;
	}
	/**
	 * 商品类型： 1：普通商品 2：折扣商品
	 * 
	 * @return type
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 商品类型： 1：普通商品 2：折扣商品
	 * 
	 * @param type
	 */
	public void setType(Integer type) {
		this.type = type;
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
	 * 商品状态： 0：下架 1：上架
	 * 
	 * @return status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 商品状态： 0：下架 1：上架
	 * 
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 是否预定：  0:否,1:是
	 * 
	 * @return isBackorder
	 */
	public Integer getIsBackorder() {
		return isBackorder;
	}
	/**
	 * 是否预定：  0:否,1:是
	 * 
	 * @param isBackorder
	 */
	public void setIsBackorder(Integer isBackorder) {
		this.isBackorder = isBackorder;
	}
	/**
	 * 可见范围：0:不可见，1:只在目录中，2:只在搜索结果中，3:目录和搜索，4:只能直接访问
	 * 
	 * @return visibility
	 */
	public Integer getVisibility() {
		return visibility;
	}
	/**
	 * 可见范围：0:不可见，1:只在目录中，2:只在搜索结果中，3:目录和搜索，4:只能直接访问
	 * 
	 * @param visibility
	 */
	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}
	/**
	 * 是否开启库存  0:否 1:是 (如果否，则商品默认为无限库存，并且不接受接口的同步库存)
	 * 
	 * @return manageStock
	 */
	public Integer getManageStock() {
		return manageStock;
	}
	/**
	 * 是否开启库存  0:否 1:是 (如果否，则商品默认为无限库存，并且不接受接口的同步库存)
	 * 
	 * @param manageStock
	 */
	public void setManageStock(Integer manageStock) {
		this.manageStock = manageStock;
	}
	/**
	 * 销售价格
	 * 
	 * @return sellPrice
	 */
	public BigDecimal getSellPrice() {
		return sellPrice;
	}
	/**
	 * 销售价格
	 * 
	 * @param sellPrice
	 */
	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}
	/**
	 * 商品原始价格
	 * 
	 * @return originalPrice
	 */
	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}
	/**
	 * 商品原始价格
	 * 
	 * @param originalPrice
	 */
	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
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
	

	@Override
	public String toString() {
		return "CatalogProductEntity [id=" + id + ", storeId=" + storeId + ", storeDefault=" + storeDefault
				+ ", isBundle=" + isBundle + ", design=" + design + ", groupSellId=" + groupSellId + ", spu=" + spu
				+ ", type=" + type + ", name=" + name + ", status=" + status + ", isBackorder=" + isBackorder
				+ ", visibility=" + visibility + ", manageStock=" + manageStock + ", sellPrice=" + sellPrice
				+ ", originalPrice=" + originalPrice + ", metaTitle=" + metaTitle + ", metaKeywords=" + metaKeywords
				+ ", metaDescription=" + metaDescription + ", description=" + description + ", deletedStatus="
				+ deletedStatus + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
}