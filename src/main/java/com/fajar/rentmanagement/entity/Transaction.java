/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fajar.rentmanagement.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.lang.Nullable;

import com.fajar.rentmanagement.annotation.CustomEntity;
import com.fajar.rentmanagement.constants.TransactionType;
import com.fajar.rentmanagement.dto.model.TransactionModel;
import com.fajar.rentmanagement.exception.ApplicationException;
import com.fajar.rentmanagement.util.DateUtil;
import com.fajar.rentmanagement.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author fajar
 */
@JsonInclude(value = Include.NON_NULL)
@CustomEntity
@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class Transaction extends BaseEntity<TransactionModel> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1055517081803635273L;
	@Column(unique = true) 
	private String code;
	@Column(name="transaction_date") 
	@Default
	private Date transactionDate = new Date();
	@Column(columnDefinition = "TEXT") 
	private String description;

	@Column 
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	@ManyToOne 
	@JoinColumn(name = "user_id")
	private User user;
	@ManyToOne 
	@JoinColumn(name = "supplier_id")
	@Nullable
	private Supplier supplier;
	@ManyToOne 
	@Nullable
	@JoinColumn(name = "customer_id" )
	private Customer customer;

	@Transient
	@Default
	private List<TransactionItem> items = new ArrayList<>();

	private void generateUniqueCode(TransactionType type) {
		int year = DateUtil.getCalendarYear(transactionDate);
		int month = DateUtil.getCalendarMonth(transactionDate);
		int day = DateUtil.getCalendarDayOfMonth(transactionDate);
		String dateCode = year + StringUtil.twoDigits(month+1) + StringUtil.twoDigits(day);
		this.code = dateCode + type.ordinal()+"-" + StringUtil.generateRandomNumber(6);
	}
	
	/**
	 * determine type 
	 */
	public void setTypeAndCode() {
		TransactionType type;
		if (supplier != null) {
			type = TransactionType.TRANS_IN;
		} else if (customer != null) {
			type = TransactionType.TRANS_OUT;
		} else {
			throw new ApplicationException("Missing transaction data!");
		}
		log.info("Transaction type: {}", type);
		setType(type);
		generateUniqueCode(type);
		
	}
	
	public int getTotalProductFlowCount() {
		if (null == items) return 0;
		int count = 0;
		for (TransactionItem productFlow : items) {
			count += productFlow.getCount();
		}
		return count;
	}

	public void addItem(TransactionItem productFlow) {
		items.add(productFlow);
	}
	
	public int getProductCount(Product product) {
		if (null == items) return 0;
		int count = 0;
		for (TransactionItem productFlow : items) {
			if (product.idEquals(productFlow.getProduct())) {
				count += productFlow.getCount();
			}
		}
		return count;
	}

	public void setProductFlowsTransactionNull() {
		if (null == items) return;
		items.forEach(p->p.setTransaction(null));
	}
	@Override
	public TransactionModel toModel() {
		TransactionModel model = new TransactionModel();
		if (null != items) {
			items.forEach(p-> {
				model.addItem(p.toModel());
			});
		}
		return copy(model, "items");
	}
	 

}
