package club.magicfun.aquila.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(RankSearchKeywordTypePK.class)
@Table(name = "rank_search_queue_type")
public class RankSearchQueueType {

	@Id
	private RankSearchQueue rankSearchQueue;

	@Id
	private RankSearchType rankSearchType;

	public RankSearchQueue getRankSearchQueue() {
		return rankSearchQueue;
	}

	public void setRankSearchQueue(RankSearchQueue rankSearchQueue) {
		this.rankSearchQueue = rankSearchQueue;
	}

	public RankSearchType getRankSearchType() {
		return rankSearchType;
	}

	public void setRankSearchType(RankSearchType rankSearchType) {
		this.rankSearchType = rankSearchType;
	}
	
}
