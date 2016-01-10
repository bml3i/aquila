package club.magicfun.aquila.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

@Entity
@NamedNativeQueries({
	@NamedNativeQuery(
		name = "ProductSearchQueue.findFewActivePendingProductSearchQueues", 
		query = "select psq.* from product_search_queue psq where psq.active_flg = 1 and (psq.cutoff_date is null or psq.cutoff_date <= date_sub(now(), interval 1 day)) limit ?1", 
		resultClass=ProductSearchQueue.class),
})
@Table(name = "product_search_queue")
public class ProductSearchQueue {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "product_id")
	private Long productId;
	
	@Column(name = "active_flg")
	private Boolean activeFlag;
	
	@Column(name = "retry_cnt")
	private Integer retryCount;
	
	@Column(name = "create_datetime")
	private Date createDatetime;
	
	@Column(name = "update_datetime")
	private Date updateDatetime;
	
	@Column(name = "cutoff_date")
	private Date cutoffDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public Boolean getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Date getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public Date getCutoffDate() {
		return cutoffDate;
	}

	public void setCutoffDate(Date cutoffDate) {
		this.cutoffDate = cutoffDate;
	}
	
}
