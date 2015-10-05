package club.magicfun.aquila.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import club.magicfun.aquila.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUserId(String userId);
	
	List<User> findByGroup_Id(Integer groupId);
}
