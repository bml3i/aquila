package club.magicfun.aquila.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import club.magicfun.aquila.model.Product;
import club.magicfun.aquila.model.ProductSearchQueue;
import club.magicfun.aquila.repository.ProductRepository;
import club.magicfun.aquila.repository.ProductSearchQueueRepository;
import club.magicfun.aquila.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductSearchQueueRepository productSearchQueueRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public List<ProductSearchQueue> findAllProductSearchQueues() {
		return productSearchQueueRepository.findAll();
	}

	@Override
	public ProductSearchQueue persist(ProductSearchQueue productSearchQueue) {
		if (productSearchQueue.getId() == null) {
			productSearchQueue.setCreateDatetime(new Date());
		}
		
		return productSearchQueueRepository.save(productSearchQueue);
	}

	@Override
	public void deleteProductSearchQueue(Integer productSearchQueueId) {
		productSearchQueueRepository.delete(productSearchQueueId);
	}

	@Override
	public Product findProductByProductId(Long productId) {
		return productRepository.findByProductId(productId);
	}
	
	@Override
	public Product persist(Product product) {
		if (product.getCreateDatetime() == null) {
			product.setCreateDatetime(new Date());
		}
		
		return productRepository.save(product);
	}

	@Override
	public void deleteProduct(Product product) {
		productRepository.delete(product);
	}

	@Override
	public void deleteProduct(Integer productTableId) {
		productRepository.delete(productTableId); 
	}


	

}
