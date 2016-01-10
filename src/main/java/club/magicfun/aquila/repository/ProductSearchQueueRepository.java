package club.magicfun.aquila.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import club.magicfun.aquila.model.ProductSearchQueue;

public interface ProductSearchQueueRepository extends JpaRepository<ProductSearchQueue, Integer> {

	List<ProductSearchQueue> findFewActivePendingProductSearchQueues(Integer number);
	
}
