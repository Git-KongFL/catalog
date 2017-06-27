package com.vancaro.catalog.entity;
import java.math.BigDecimal;
import java.util.Date;
/**
 * <b> catalog_product_sku </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:33:29
 */
public class CatalogProductSku{
	/**
	 * SKU货品表ID
	 */
	private Integer id;
	/**
	 * 商品ID
	 */
	private Integer productId;
	/**
	 * 规格码：SKU的唯一码，同时将其作为条形码使用货品的货号(以商品的货号加横线加数字组成)
	 */
	private String sku;
	/**
	 * 库存数：当开启库存后，必填。并展示在商品明细页。随着客户的下单，此库存数也随着变化。
	 */
	private Integer stockQty;
	/**
	 * 最小库存：当开启库存后，必填。当商品小于最小库存数时，B2C端自动下架。当受库存同步影响后，库存数如果大于最小库存数，则自动上架。
	 */
	private Integer stockMinQty;
	/**
	 * 参差价格：和SPU上面的商品价格相比，参差价格
	 */
	private BigDecimal priceDifference;
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
	 * SKU货品表ID
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * SKU货品表ID
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
	 * 规格码：SKU的唯一码，同时将其作为条形码使用货品的货号(以商品的货号加横线加数字组成)
	 * 
	 * @return sku
	 */
	public String getSku() {
		return sku;
	}
	/**
	 * 规格码：SKU的唯一码，同时将其作为条形码使用货品的货号(以商品的货号加横线加数字组成)
	 * 
	 * @param sku
	 */
	public void setSku(String sku) {
		this.sku = sku;
	}
	/**
	 * 库存数：当开启库存后，必填。并展示在商品明细页。随着客户的下单，此库存数也随着变化。
	 * 
	 * @return stockQty
	 */
	public Integer getStockQty() {
		return stockQty;
	}
	/**
	 * 库存数：当开启库存后，必填。并展示在商品明细页。随着客户的下单，此库存数也随着变化。
	 * 
	 * @param stockQty
	 */
	public void setStockQty(Integer stockQty) {
		this.stockQty = stockQty;
	}
	/**
	 * 最小库存：当开启库存后，必填。当商品小于最小库存数时，B2C端自动下架。当受库存同步影响后，库存数如果大于最小库存数，则自动上架。
	 * 
	 * @return stockMinQty
	 */
	public Integer getStockMinQty() {
		return stockMinQty;
	}
	/**
	 * 最小库存：当开启库存后，必填。当商品小于最小库存数时，B2C端自动下架。当受库存同步影响后，库存数如果大于最小库存数，则自动上架。
	 * 
	 * @param stockMinQty
	 */
	public void setStockMinQty(Integer stockMinQty) {
		this.stockMinQty = stockMinQty;
	}
	/**
	 * 参差价格：和SPU上面的商品价格相比，参差价格
	 * 
	 * @return priceDifference
	 */
	public BigDecimal getPriceDifference() {
		return priceDifference;
	}
	/**
	 * 参差价格：和SPU上面的商品价格相比，参差价格
	 * 
	 * @param priceDifference
	 */
	public void setPriceDifference(BigDecimal priceDifference) {
		this.priceDifference = priceDifference;
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