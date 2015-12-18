package club.magicfun.aquila.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import club.magicfun.aquila.model.RankSearchHistory;
import club.magicfun.aquila.model.RankSearchKeyword;
import club.magicfun.aquila.repository.RankSearchHistoryRepository;
import club.magicfun.aquila.repository.RankSearchKeywordRepository;
import club.magicfun.aquila.repository.RankSearchTypeRepository;
import club.magicfun.aquila.service.RankingService;

@Service
@Transactional
public class RankingServiceImpl implements RankingService {

	@Autowired
	private RankSearchKeywordRepository rankSearchKeywordRepository;
	
	@Autowired
	private RankSearchTypeRepository rankSearchTypeRepository;
	
	@Autowired
	private RankSearchHistoryRepository rankSearchHistoryRepository;

	@Override
	public List<RankSearchKeyword> findAllRankSearchKeywords() {
		return rankSearchKeywordRepository.findAll();
	}

	@Override
	public RankSearchHistory persist(RankSearchHistory rankSearchHistory) {
		if (rankSearchHistory.getId() != null) {
			rankSearchHistory.setCreateDatetime(new Date());
		}
		return rankSearchHistoryRepository.save(rankSearchHistory);
	}
	
}
