package com.fajar.rentmanagement.dto.model;

import com.fajar.rentmanagement.annotation.Dto;
import com.fajar.rentmanagement.constants.AuthorityType;
import com.fajar.rentmanagement.entity.Authority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Dto
public class AuthorityModel extends BaseModel<Authority> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2534190215509155334L;

	private AuthorityType name;

	@Override
	public Authority toEntity() {
		Authority entity = new Authority(); 
		copy(entity);
		return entity;
	}

	 
}
