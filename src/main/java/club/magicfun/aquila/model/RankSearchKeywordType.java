package club.magicfun.aquila.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(RankSearchKeywordTypePK.class)
@Table(name = "rank_search_keyword_type")
public class RankSearchKeywordType {

	@Id
	private RankSearchKeyword rankSearchKeyword;

	@Id
	private RankSearchType rankSearchType;

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
	
}
