package com.fajar.rentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fajar.rentmanagement.entity.ApplicationProfile;

public interface AppProfileRepository extends JpaRepository<ApplicationProfile, Long> {
 

	ApplicationProfile findByAppCode(String appCode); 

}
