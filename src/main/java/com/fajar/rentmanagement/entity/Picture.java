package com.fajar.rentmanagement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fajar.rentmanagement.annotation.CustomEntity;
import com.fajar.rentmanagement.dto.model.PictureModel;

import lombok.Data;

@Data
@CustomEntity
@Entity
@Table(name = "picture")
public class Picture extends BaseEntity<PictureModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 677466544593252967L;
	
	@Column(nullable = false)
	private String name; 
	
	@Transient
	private String base64Data;

	public Picture withName(String imageName) {
		this.name = imageName;
		return this;
	}
	

}
