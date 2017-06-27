package com.vancaro.catalog.entity;
import java.util.Date;
/**
 * <b> catalog_product_entity_video </b>
 * 
 * @author denghaihui
 * @date 2017-06-26 16:31:20
 */
public class CatalogProductEntityVideo{
	/**
	 * 商品视频ID
	 */
	private Integer id;
	/**
	 * 商品ID
	 */
	private Integer productId;
	/**
	 * 商品规格属性值ID
	 */
	private Integer productSpecValueId;
	/**
	 * 资源来源
	 */
	private Integer resType;
	/**
	 * 本地url：记录此信息的在本地服务器上面的地址
	 */
	private String url;
	/**
	 * 远程URL：记录此信息的在远程服务器上面的地址
	 */
	private String remoteUrl;
	/**
	 * 默认状态：0:否 1:是
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
	 * 商品视频ID
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 商品视频ID
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
	 * 商品规格属性值ID
	 * 
	 * @return productSpecValueId
	 */
	public Integer getProductSpecValueId() {
		return productSpecValueId;
	}
	/**
	 * 商品规格属性值ID
	 * 
	 * @param productSpecValueId
	 */
	public void setProductSpecValueId(Integer productSpecValueId) {
		this.productSpecValueId = productSpecValueId;
	}
	/**
	 * 资源来源
	 * 
	 * @return resType
	 */
	public Integer getResType() {
		return resType;
	}
	/**
	 * 资源来源
	 * 
	 * @param resType
	 */
	public void setResType(Integer resType) {
		this.resType = resType;
	}
	/**
	 * 本地url：记录此信息的在本地服务器上面的地址
	 * 
	 * @return url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 本地url：记录此信息的在本地服务器上面的地址
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 远程URL：记录此信息的在远程服务器上面的地址
	 * 
	 * @return remoteUrl
	 */
	public String getRemoteUrl() {
		return remoteUrl;
	}
	/**
	 * 远程URL：记录此信息的在远程服务器上面的地址
	 * 
	 * @param remoteUrl
	 */
	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}
	/**
	 * 默认状态：0:否 1:是
	 * 
	 * @return status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 默认状态：0:否 1:是
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