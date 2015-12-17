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
@Table(name = "rank_search_detail")
public class RankSearchDetail {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rank_search_keyword_id")
	private RankSearchKeyword rankSearchKeyword;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rank_search_type_id")
	private RankSearchType rankSearchType;
	
	@Column(name = "rank_num")
	private Integer rankNumber;

	@Column(name = "product_id")
	private Integer productId;
	
	@Column(name = "create_datetime")
	private Date createDatetime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RankSearchKeyword getRankSearchKeyword() {
		return rankSearchKeyword;
	}

	public void setRankSearchKeyword(RankSearchKeyword rankSearchKeyword) {
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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}
	
	
	
}
