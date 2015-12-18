package club.magicfun.aquila.service;

import java.util.List;

import club.magicfun.aquila.model.Job;

public interface ScheduleService {

	Job findJobByClassName(String className);
	
	List<Job> findAllJobs();
	
	Job persist(Job job);
	
	Job startJob(Job job);
	
	Job completeJob(Job job);
	
	Job failJob(Job job);
}
