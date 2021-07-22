package com.fajar.rentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fajar.rentmanagement.entity.Configuration;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long>{

	Configuration findTop1ByCode(String configCode);
	
	 

}
