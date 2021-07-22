package com.fajar.rentmanagement.dto.model;

import com.fajar.rentmanagement.annotation.Dto;
import com.fajar.rentmanagement.entity.Configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Dto()
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationModel extends BaseModel<Configuration> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5536800374303913968L;
	
	private String code;
	private int expiredWarningDays;
	private int leadTime;
	private int cycleTime;
	@Override
	public Configuration toEntity() {
		Configuration entity = new Configuration();
		return null;
	}

}
