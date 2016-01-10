package club.magicfun.aquila.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import club.magicfun.aquila.model.Rank;

public interface RankRepository extends JpaRepository<Rank, Integer> {

}
