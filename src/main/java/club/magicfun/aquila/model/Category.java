package club.magicfun.aquila.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	
	@Column(name = "category_name")
	private String categoryName;
	
	@Column(name = "category_price")
	private Double categoryPrice; 

	@Column(name = "category_stock_number")
	private Integer categoryStockNumber;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Double getCategoryPrice() {
		return categoryPrice;
	}

	public void setCategoryPrice(Double categoryPrice) {
		this.categoryPrice = categoryPrice;
	}

	public Integer getCategoryStockNumber() {
		return categoryStockNumber;
	}

	public void setCategoryStockNumber(Integer categoryStockNumber) {
		this.categoryStockNumber = categoryStockNumber;
	}
	
}
