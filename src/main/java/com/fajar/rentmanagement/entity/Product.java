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
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fajar.rentmanagement.annotation.CustomEntity;
import com.fajar.rentmanagement.dto.model.ProductModel;
import com.fajar.rentmanagement.entity.setting.MultipleImageModel;

import lombok.Data;

/**
 * 
 * @author fajar
 */
@CustomEntity 
@Entity
@Table(name = "product")
@Data  
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
	
	@Column(name="for_rent")
	private boolean forRent;
	
	@Column
	private boolean available;

	@Column
	private double price;
	
//	@Fetch (FetchMode.SELECT)	-> MULTIPLE query 
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) 
	@JoinTable(name = "product_pictures", 
			joinColumns = { @JoinColumn(name = "product_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "picture_id") }) 
	
	private Set<Picture> pictures = new HashSet<>();
	
	public Product() {
		pictures = new HashSet<>();
	}
	@Transient
	private int count;
	
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
