package club.magicfun.aquila.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "rank_search_types")
public class RankSearchType {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;
	
	@Column(name = "sort_type")
	private String sortType;
	
	@OneToMany(mappedBy = "rankSearchType", fetch = FetchType.LAZY)
	private Set<RankSearchKeywordType> rankSearchKeywordTypes;

	@ManyToMany(mappedBy = "rankSearchTypes", fetch = FetchType.LAZY)
	private Set<RankSearchQueue> rankSearchKeywords = new HashSet<RankSearchQueue>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<RankSearchKeywordType> getRankSearchKeywordTypes() {
		return rankSearchKeywordTypes;
	}

	public void setRankSearchKeywordTypes(Set<RankSearchKeywordType> rankSearchKeywordTypes) {
		this.rankSearchKeywordTypes = rankSearchKeywordTypes;
	}

	public Set<RankSearchQueue> getRankSearchKeywords() {
		return rankSearchKeywords;
	}

	public void setRankSearchKeywords(Set<RankSearchQueue> rankSearchKeywords) {
		this.rankSearchKeywords = rankSearchKeywords;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	
}
