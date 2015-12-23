package club.magicfun.aquila.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "month_sale_amount")
	private Integer monthSaleAmount;
	
	@Column(name = "product_price")
	private Double productPrice; 
	
	@Column(name = "shop_name")
	private String shopName;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "product")
	@OrderBy("id ASC")
	private Set<Category> categories = new HashSet<Category>();
	
	@Column(name = "active_flg")
	private Boolean activeFlag;
	
	@Column(name = "create_datetime")
	private Date createDatetime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getMonthSaleAmount() {
		return monthSaleAmount;
	}

	public void setMonthSaleAmount(Integer monthSaleAmount) {
		this.monthSaleAmount = monthSaleAmount;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Boolean getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}
	
}
