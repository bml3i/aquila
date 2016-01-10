package club.magicfun.aquila.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import club.magicfun.aquila.model.Rank;
import club.magicfun.aquila.model.RankSearchQueue;
import club.magicfun.aquila.repository.RankRepository;
import club.magicfun.aquila.repository.RankSearchQueueRepository;
import club.magicfun.aquila.repository.RankSearchTypeRepository;
import club.magicfun.aquila.service.RankingService;

@Service
@Transactional
public class RankingServiceImpl implements RankingService {

	@Autowired
	private RankSearchQueueRepository rankSearchQueueRepository;
	
	@Autowired
	private RankSearchTypeRepository rankSearchTypeRepository;
	
	@Autowired
	private RankRepository rankRepository;

	@Override
	public List<RankSearchQueue> findAllRankSearchQueues() {
		return rankSearchQueueRepository.findAll();
	}

	@Override
	public Rank persist(Rank rank) {
		if (rank.getId() != null) {
			rank.setCreateDatetime(new Date());
		}
		return rankRepository.save(rank);
	}
	
}
