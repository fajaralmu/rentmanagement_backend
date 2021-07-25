/*
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fajar.rentmanagement.dto.model;

import java.util.HashSet;
import java.util.Set;

import com.fajar.rentmanagement.annotation.Dto;
import com.fajar.rentmanagement.annotation.FormField;
import com.fajar.rentmanagement.constants.FieldType;
import com.fajar.rentmanagement.constants.Filterable;
import com.fajar.rentmanagement.entity.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 
 * @author fajar
 */
@Dto( value= "Produk", updateService = "productUpdateService") 
@JsonInclude(value = Include.NON_NULL)
@Data
public class ProductModel extends BaseModel<Product>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4398862371443391887L;

	 
	@FormField
	private String code;
	@FormField
	private String name;
	@FormField(type = FieldType.FIELD_TYPE_FIXED_LIST, optionItemName = "name")
	private UnitModel unit;
	@FormField(type = FieldType.FIELD_TYPE_NUMBER, required = true)
	private double price;
	@FormField(type = FieldType.FIELD_TYPE_TEXTAREA, required = false)
	private String description;
	@FormField(type = FieldType.FIELD_TYPE_CHECKBOX)
	private boolean forRent;
	@FormField(multipleImage = true, type = FieldType.FIELD_TYPE_IMAGE)
	private Set<PictureModel> pictures = new HashSet<>();
	@FormField(type = FieldType.FIELD_TYPE_TEXTAREA, editable = false, filterable = Filterable.DISABLE_ALL)
	private int count;
	 
	@Override
	public Product toEntity() {
		Product entity = new Product();
		if (null != pictures) {
			pictures.forEach(p -> {
				entity.addPicture(p.toEntity());
			});
		}
		return copy(entity, "pictures");
	}


	public void addPicture(PictureModel model) {
		if (null == pictures) {
			pictures = new HashSet<>();
		}
		this.pictures.add(model);
	}

}
