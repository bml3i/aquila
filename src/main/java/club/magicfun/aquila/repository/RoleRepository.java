package club.magicfun.aquila.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import club.magicfun.aquila.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
