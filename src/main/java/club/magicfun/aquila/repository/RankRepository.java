package club.magicfun.aquila.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import club.magicfun.aquila.model.Rank;

public interface RankRepository extends JpaRepository<Rank, Integer> {

	List<Rank> findAllRanksByRankSearchQueueId(Integer rankSearchQueueId);
	
}
