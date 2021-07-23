package com.fajar.rentmanagement.service.entity;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.rentmanagement.entity.Product;
import com.fajar.rentmanagement.entity.ProductStock;
import com.fajar.rentmanagement.repository.ProductStockRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductUpdateService extends CommonUpdateService<Product>{

	@Autowired
	private ProductStockRepository productStockRepository;
	
	@Override
	public void postFilter(List<Product> items) {
		mapStock(items); 
	}

	private void mapStock(List<Product> items) {
		if (items.isEmpty()) 
			return; 
		
		List<ProductStock> stocks = productStockRepository.findByProductIn(items);
		log.info("Map product stocks, records: {}", stocks.size());
		Map<Long, ProductStock> mappedByProductId = productStockRepository.mapByProductId(stocks);
		for (Product product : items) {
			ProductStock stock = mappedByProductId.get(product.getId());
			if (null == stock) continue;
			product.setCount(stock.getCount());
		}
	}
}
