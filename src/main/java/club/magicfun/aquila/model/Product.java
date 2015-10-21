package club.magicfun.aquila.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "taobao_product_id")
	private Integer taobaoProductId;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "taobao_shop_id")
	private Integer taobaoShopId;
	
	@Column(name = "shop_name")
	private String shopName;
	
	@Column(name = "month_sale_amount")
	private Integer monthSaleAmount;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "product")
	@OrderBy("id ASC")
	private Set<Category> categories = new HashSet<Category>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTaobaoProductId() {
		return taobaoProductId;
	}

	public void setTaobaoProductId(Integer taobaoProductId) {
		this.taobaoProductId = taobaoProductId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getTaobaoShopId() {
		return taobaoShopId;
	}

	public void setTaobaoShopId(Integer taobaoShopId) {
		this.taobaoShopId = taobaoShopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
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
	
	
}
