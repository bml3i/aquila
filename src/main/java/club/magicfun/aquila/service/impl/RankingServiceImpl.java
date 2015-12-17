package club.magicfun.aquila.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	
	
	
}
