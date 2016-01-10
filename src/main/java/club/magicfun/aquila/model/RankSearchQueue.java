package club.magicfun.aquila.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "rank_search_queue")
public class RankSearchQueue {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "keyword")
	private String keyword;

	@Column(name = "description")
	private String description;
	
	@OneToMany(mappedBy = "rankSearchQueue", fetch = FetchType.LAZY)
	private Set<RankSearchQueueType> rankSearchQueueTypes;

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
	@OrderBy("id ASC")
	@JoinTable(name = "rank_search_queue_type", joinColumns = { @JoinColumn(name = "rank_search_queue_id") }, inverseJoinColumns = { @JoinColumn(name = "rank_search_type_id") })
	private Set<RankSearchType> rankSearchTypes = new HashSet<RankSearchType>();

	@Column(name = "create_datetime")
	private Date createDatetime;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<RankSearchQueueType> getRankSearchQueueTypes() {
		return rankSearchQueueTypes;
	}

	public void setRankSearchQueueTypes(Set<RankSearchQueueType> rankSearchQueueTypes) {
		this.rankSearchQueueTypes = rankSearchQueueTypes;
	}

	public Set<RankSearchType> getRankSearchTypes() {
		return rankSearchTypes;
	}

	public void setRankSearchTypes(Set<RankSearchType> rankSearchTypes) {
		this.rankSearchTypes = rankSearchTypes;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

}
