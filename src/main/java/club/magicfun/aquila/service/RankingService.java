package club.magicfun.aquila.service;

import java.util.List;

import club.magicfun.aquila.model.Rank;
import club.magicfun.aquila.model.RankSearchQueue;

public interface RankingService {

	List<RankSearchQueue> findAllRankSearchQueues();
	
	Rank persist(Rank rankSearchHistory);
	
}