package club.magicfun.aquila.service;

import java.util.List;

import club.magicfun.aquila.model.RankSearchHistory;
import club.magicfun.aquila.model.RankSearchKeyword;

public interface RankingService {

	List<RankSearchKeyword> findAllRankSearchKeywords();
	
	RankSearchHistory persist(RankSearchHistory rankSearchHistory);
	
}