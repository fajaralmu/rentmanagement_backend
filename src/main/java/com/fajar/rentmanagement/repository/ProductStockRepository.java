package com.fajar.rentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fajar.rentmanagement.entity.ProductStock;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long>{

}
