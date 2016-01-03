package club.magicfun.aquila.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import club.magicfun.aquila.model.Agent;

public interface AgentRepository extends JpaRepository<Agent, Integer> {

	List<Agent> findAllActiveAgents();
	
	List<Agent> findFewRecentActiveAgents(Integer number);
	
	List<Agent> findAllInactiveAgents();
	
}
