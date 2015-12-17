package club.magicfun.aquila.service;

import java.util.List;

import club.magicfun.aquila.model.RankSearchDetail;
import club.magicfun.aquila.model.RankSearchKeyword;

public interface RankingService {

	List<RankSearchKeyword> findAllRankSearchKeywords();
	
	RankSearchDetail persist(RankSearchDetail rankSearchDetail);
	
}