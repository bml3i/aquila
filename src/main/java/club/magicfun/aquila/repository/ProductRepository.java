package club.magicfun.aquila.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import club.magicfun.aquila.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	Product findByProductId(Long productId);
	
}
