package club.magicfun.aquila.job;

import club.magicfun.aquila.model.Job;

public abstract class AbstractJob {

	protected Job job;
	
	abstract void lookupJob();
	
	public AbstractJob() {
		super();
		init();
	}

	private void init() {
		lookupJob();
		
	}
}
