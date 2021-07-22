/*
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fajar.rentmanagement.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.fajar.rentmanagement.annotation.CustomEntity;
import com.fajar.rentmanagement.dto.model.ProductModel;
import com.fajar.rentmanagement.entity.setting.MultipleImageModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

/**
 * 
 * @author fajar
 */
@CustomEntity 
@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends BaseEntity<ProductModel> implements MultipleImageModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4398862371443391887L;

	 
	@Column(unique = true) 
	private String code;
	@Column 
	private String name;
	@Column 
	private String description;
	@ManyToOne 
	@JoinColumn(name = "unit_id")
	private Unit unit;  
	
	@Default
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) 
	@JoinTable(name = "product_pictures", 
			joinColumns = { @JoinColumn(name = "product_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "picture_id") }) 
	@BatchSize(size = 100)
	private Set<Picture> pictures = new HashSet<>();
	
	public void addPicture(Picture entity) {
		validatePictures();
		pictures.add(entity);
	}
	
	
	@Override
	public ProductModel toModel() {
		ProductModel model = new ProductModel();
		if (null != pictures) {
			pictures.forEach(p-> {
				model.addPicture(p.toModel());
			});
		}
		return copy(model, "pictures");
	}
	  
	

}
