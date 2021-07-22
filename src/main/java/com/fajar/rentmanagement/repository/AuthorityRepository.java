package com.fajar.rentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fajar.rentmanagement.constants.AuthorityType;
import com.fajar.rentmanagement.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	Authority findTop1ByName(AuthorityType type);
 

}
