package club.magicfun.aquila.model;

import java.util.Date;

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
@Table(name = "rank_search_hist")
public class RankSearchHistory {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rank_search_keyword_id")
	private RankSearchQueue rankSearchKeyword;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rank_search_type_id")
	private RankSearchType rankSearchType;
	
	@Column(name = "rank_num")
	private Integer rankNumber;

	@Column(name = "product_id")
	private Long productId;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "product_price")
	private Double productPrice; 
	
	@Column(name = "deal_count")
	private Integer dealCount;
	
	@Column(name = "shop_name")
	private String shopName;
	
	@Column(name = "create_datetime")
	private Date createDatetime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RankSearchQueue getRankSearchKeyword() {
		return rankSearchKeyword;
	}

	public void setRankSearchKeyword(RankSearchQueue rankSearchKeyword) {
		this.rankSearchKeyword = rankSearchKeyword;
	}

	public RankSearchType getRankSearchType() {
		return rankSearchType;
	}

	public void setRankSearchType(RankSearchType rankSearchType) {
		this.rankSearchType = rankSearchType;
	}

	public Integer getRankNumber() {
		return rankNumber;
	}

	public void setRankNumber(Integer rankNumber) {
		this.rankNumber = rankNumber;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}

	public Integer getDealCount() {
		return dealCount;
	}

	public void setDealCount(Integer dealCount) {
		this.dealCount = dealCount;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
}
