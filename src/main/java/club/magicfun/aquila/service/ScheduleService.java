package club.magicfun.aquila.service;

import club.magicfun.aquila.model.Job;

public interface ScheduleService {

	Job findJobByClassName(String className);
	
	Job persist(Job job);
	
	Job startJob(Job job);
	
	Job completeJob(Job job);
	
	Job failJob(Job job);
}
