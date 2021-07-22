package com.fajar.rentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fajar.rentmanagement.entity.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
 
}
