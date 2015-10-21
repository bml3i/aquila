package club.magicfun.aquila.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import club.magicfun.aquila.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
