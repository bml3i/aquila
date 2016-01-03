package club.magicfun.aquila.service;

import java.util.List;

import club.magicfun.aquila.model.Agent;

public interface AgentService {
	
	Agent persist(Agent agent);
	
	void deleteAgent(Integer agentId);
	
	void deleteAgent(Agent agent);
	
	List<Agent> findAllActiveAgents();
	
	List<Agent> findAllInactiveAgents();
	
}
