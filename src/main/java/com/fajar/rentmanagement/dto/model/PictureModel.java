package com.fajar.rentmanagement.dto.model;

import com.fajar.rentmanagement.annotation.Dto;
import com.fajar.rentmanagement.entity.Picture;

import lombok.Data;

@Data
@Dto
public class PictureModel extends BaseModel<Picture> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -695961896052782176L;
	private String name;
	private String base64Data;

}
