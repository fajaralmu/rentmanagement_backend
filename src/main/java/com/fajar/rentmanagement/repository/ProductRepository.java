package com.fajar.rentmanagement.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fajar.rentmanagement.entity.Product;
import com.fajar.rentmanagement.entity.Unit;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByUnit(Unit unit);

	List<Product> findByName(String name); 

	List<Product> findByOrderByName(Pageable of); 
	 

}
