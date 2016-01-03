package club.magicfun.aquila.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import club.magicfun.aquila.model.Agent;
import club.magicfun.aquila.repository.AgentRepository;
import club.magicfun.aquila.service.AgentService;

@Service
@Transactional
public class AgentServiceImpl implements AgentService {

	@Autowired
	private AgentRepository agentRepository;

	@Override
	public Agent persist(Agent agent) {
		if (agent.getCreateDatetime() == null) {
			agent.setCreateDatetime(new Date());
		}
		
		if (agent.getId() != null) {
			agent.setUpdateDatetime(new Date());
		}
		
		return agentRepository.save(agent);
	}

	@Override
	public void deleteAgent(Integer agentId) {
		agentRepository.delete(agentId);
	}
	
	@Override
	public void deleteAgent(Agent agent) {
		agentRepository.delete(agent);
	}

	@Override
	public List<Agent> findAllActiveAgents() {
		return agentRepository.findActiveAgents();
	}

	@Override
	public List<Agent> findAllInactiveAgents() {
		return agentRepository.findInactiveAgents();
	}
	
}
