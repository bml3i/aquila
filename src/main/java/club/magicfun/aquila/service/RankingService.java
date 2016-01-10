package club.magicfun.aquila.service;

import java.util.List;

import club.magicfun.aquila.model.Rank;
import club.magicfun.aquila.model.RankSearchQueue;

public interface RankingService {

	List<RankSearchQueue> findAllRankSearchQueues();
	
	List<RankSearchQueue> findFewActivePendingRankSearchQueues(Integer number);
	
	RankSearchQueue persist(RankSearchQueue rankSearchQueue);
	
	Rank persist(Rank rank);
	
}