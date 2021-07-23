/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fajar.rentmanagement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

import com.fajar.rentmanagement.annotation.CustomEntity;
import com.fajar.rentmanagement.dto.model.BaseModel;
import com.fajar.rentmanagement.dto.model.TransactionItemModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author fajar
 */
@CustomEntity
@Component
@Entity
@Table(name = "product_stock")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStock extends BaseEntity  {

	public ProductStock(Product product2) {
		this.product = product2;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8839593046741378229L;
 

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false) 
	private Product product;
 
	@Column (nullable = false)
	private int count;

	public void addCount(int count2) {
		this.count += count2;
	}  
	
	@Override
	public BaseModel toModel() {
		throw new NotImplementedException("not implemented");
	}
	   
	 
	  
	 
}
