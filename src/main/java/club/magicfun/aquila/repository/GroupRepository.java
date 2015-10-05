package club.magicfun.aquila.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import club.magicfun.aquila.model.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {

	Group findByName(String name);
	
}
