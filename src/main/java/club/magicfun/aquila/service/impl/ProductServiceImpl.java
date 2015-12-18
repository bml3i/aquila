package club.magicfun.aquila.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import club.magicfun.aquila.model.ProductSearchQueue;
import club.magicfun.aquila.repository.ProductSearchQueueRepository;
import club.magicfun.aquila.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductSearchQueueRepository productSearchQueueRepository;
	
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

}
