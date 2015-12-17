package club.magicfun.aquila.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import club.magicfun.aquila.model.Job;
import club.magicfun.aquila.repository.JobRepository;
import club.magicfun.aquila.service.ScheduleService;


@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

	public static final String JOB_RUN_STATUS_IN_PROCESS = "I";
	public static final String JOB_RUN_STATUS_COMPLETE = "C";
	public static final String JOB_RUN_STATUS_FAILURE = "F";
	
	@Autowired
	MessageSource messageSource;

	@Autowired
	private JobRepository jobRepository;

	@Override
	public Job findJobByClassName(String className) {
		return jobRepository.findByClassName(className);
	}

	@Override
	public Job persist(Job job) {
		
		if (job.getId() != null) {
			job.setCreateDatetime(new Date());
		}
		
		return jobRepository.save(job);
	}
	
	@Override
	public Job startJob(Job job) {
		job.setStartDatetime(new Date());
		job.setRunStatus(JOB_RUN_STATUS_IN_PROCESS);
		
		return jobRepository.save(job);
	}

	@Override
	public Job completeJob(Job job) {
		job.setEndDatetime(new Date());
		job.setRunStatus(JOB_RUN_STATUS_COMPLETE);
		
		return jobRepository.save(job);
	}

	@Override
	public Job failJob(Job job) {
		job.setRunStatus(JOB_RUN_STATUS_FAILURE);
		
		return jobRepository.save(job);
	}
	
}
