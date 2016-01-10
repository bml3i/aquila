package club.magicfun.aquila.service;

import java.util.List;

import club.magicfun.aquila.model.RankSearchHistory;
import club.magicfun.aquila.model.RankSearchQueue;

public interface RankingService {

	List<RankSearchQueue> findAllRankSearchQueues();
	
	RankSearchHistory persist(RankSearchHistory rankSearchHistory);
	
}