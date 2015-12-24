package club.magicfun.aquila.service;

import java.util.List;

import club.magicfun.aquila.model.Product;
import club.magicfun.aquila.model.ProductSearchQueue;

public interface ProductService {

	List<ProductSearchQueue> findAllProductSearchQueues();
	
	List<ProductSearchQueue> findProductSearchQueuesByMaxRetryCount(Integer maxRetryCount);
	
	ProductSearchQueue persist(ProductSearchQueue productSearchQueue);
	
	void deleteProductSearchQueue(Integer productSearchQueueId);
	
	Product findProductByProductId(Long productId);
	
	Product persist(Product product);
	
	void deleteProduct(Product product);
	
	void deleteProduct(Integer productTableId);
	
}
