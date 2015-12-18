package club.magicfun.aquila.service;

import java.util.List;

import club.magicfun.aquila.model.ProductSearchQueue;

public interface ProductService {

	List<ProductSearchQueue> findAllProductSearchQueues();
	
	ProductSearchQueue persist(ProductSearchQueue productSearchQueue);
	
	void deleteProductSearchQueue(Integer productSearchQueueId);
	
}
