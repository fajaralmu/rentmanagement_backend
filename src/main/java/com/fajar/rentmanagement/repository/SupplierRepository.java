package com.fajar.rentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fajar.rentmanagement.entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>{

	
}

