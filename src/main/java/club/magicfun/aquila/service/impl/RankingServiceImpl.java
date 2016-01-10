package club.magicfun.aquila.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import club.magicfun.aquila.model.RankSearchHistory;
import club.magicfun.aquila.model.RankSearchQueue;
import club.magicfun.aquila.repository.RankSearchHistoryRepository;
import club.magicfun.aquila.repository.RankSearchQueueRepository;
import club.magicfun.aquila.repository.RankSearchTypeRepository;
import club.magicfun.aquila.service.RankingService;

@Service
@Transactional
public class RankingServiceImpl implements RankingService {

	@Autowired
	private RankSearchQueueRepository rankSearchKeywordRepository;
	
	@Autowired
	private RankSearchTypeRepository rankSearchTypeRepository;
	
	@Autowired
	private RankSearchHistoryRepository rankSearchHistoryRepository;

	@Override
	public List<RankSearchQueue> findAllRankSearchQueues() {
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
