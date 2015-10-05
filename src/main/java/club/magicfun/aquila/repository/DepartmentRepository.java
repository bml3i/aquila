package club.magicfun.aquila.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import club.magicfun.aquila.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

	Department findByName(String name);
	
}
