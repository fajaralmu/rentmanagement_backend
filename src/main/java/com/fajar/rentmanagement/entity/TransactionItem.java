/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fajar.rentmanagement.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fajar.rentmanagement.annotation.CustomEntity;
import com.fajar.rentmanagement.dto.model.TransactionItemModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "transaction_item")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionItem extends BaseEntity<TransactionItemModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8839593046741372229L;

//	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "transaction_id", nullable = false) 
	private Transaction transaction;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false) 
	private Product product;
 
	@Column (nullable = false)
	private int count;
	@Column
	private int duration;
 
	@Column (nullable = false)
	private double price;  
	   
	////////////
 
	public boolean productsEquals(Product p) {
		if (product == null) return false;
		return product.idEquals(p);
	} 
	
	 
 
	@JsonIgnore
	public Long getTransactionId() {
		if (null == transaction) return null;
		return transaction.getId();
	}
	 
	public static double sumQtyAndPrice(List<TransactionItem> list) {
		double result = 0;
		for (TransactionItem productFlow : list) {
			double priceAndCount = productFlow.getPrice() * productFlow.getCount();
			result += priceAndCount;
		}
		return result;
	}
	  
	 
}
