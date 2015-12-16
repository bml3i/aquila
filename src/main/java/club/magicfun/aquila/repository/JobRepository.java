package club.magicfun.aquila.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import club.magicfun.aquila.model.Job;

public interface JobRepository extends JpaRepository<Job, Integer> {

	Job findByClassName(String className);
	
}
