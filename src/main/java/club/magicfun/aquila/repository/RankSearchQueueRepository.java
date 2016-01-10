package club.magicfun.aquila.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import club.magicfun.aquila.model.RankSearchQueue;

public interface RankSearchQueueRepository extends JpaRepository<RankSearchQueue, Integer> {
	
	List<RankSearchQueue> findFewActivePendingRankSearchQueues(Integer number);
	
}
