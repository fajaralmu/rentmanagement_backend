package com.fajar.rentmanagement.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fajar.rentmanagement.entity.Product;
import com.fajar.rentmanagement.entity.ProductStock;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long>{

	List<ProductStock> findByProductIn(List<Product> items);
	
	default Map<Long, ProductStock> mapByProductId(List<ProductStock> stocks) {
		return new HashMap<Long, ProductStock>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -3406768119998656973L;

			{
				for (ProductStock productStock : stocks) {
					put(productStock.getProduct().getId(), productStock);
				}
			}
		};
	}

}
