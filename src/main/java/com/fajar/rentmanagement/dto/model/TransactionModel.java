/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fajar.rentmanagement.dto.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fajar.rentmanagement.annotation.Dto;
import com.fajar.rentmanagement.annotation.FormField;
import com.fajar.rentmanagement.constants.FieldType;
import com.fajar.rentmanagement.constants.Filterable;
import com.fajar.rentmanagement.constants.TransactionType;
import com.fajar.rentmanagement.entity.Transaction;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AccessLevel;
import lombok.Builder.Default;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author fajar
 */
@JsonInclude(value = Include.NON_NULL)
@Dto(deletable = false, editable = true, creatable = false, updateService = "transactionUpdateService", value = "Transaksi")
@Data
@Slf4j
public class TransactionModel extends BaseModel<Transaction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1055517081803635273L;
	@FormField(editable = false)
	private String code;
	@FormField(type = FieldType.FIELD_TYPE_DATETIME)
	@Default
	private Date transactionDate = new Date();
	@FormField(type = FieldType.FIELD_TYPE_TEXTAREA, required = false)
	private String description;
	@FormField(editable = false, filterable = Filterable.DISABLE_ALL)
	@Getter(value = AccessLevel.NONE)
	private Integer productCount;

	@FormField(editable = false)
	private TransactionType type;
	@FormField(optionItemName = "displayName", editable = false)
	@Getter(value = AccessLevel.NONE)
	private UserModel user;
	@FormField(type = FieldType.FIELD_TYPE_DYNAMIC_LIST, optionItemName = "name", required = false)
	private SupplierModel supplier;
	@FormField(type = FieldType.FIELD_TYPE_DYNAMIC_LIST, optionItemName = "name", required = false)
	private CustomerModel customer;
	 
	private List<TransactionItemModel> items = new ArrayList<>();

	public TransactionModel() {}
	
	@Override
	public Transaction toEntity() {
		Transaction entity = new Transaction();
		items.forEach(p -> {
			entity.addItem(p.toEntity());
		});
		return copy(entity, "productFlows");
	}

	public void addItem(TransactionItemModel productFlow) {
		items.add(productFlow);
	}

	public Integer getProductCount() {
		if (null == items)
			return null;
		int count = 0;
		for (TransactionItemModel productFlow : items) {
			count += productFlow.getCount();
		}
		return count;
	}
	

	public UserModel getUser() {
		if (null == user) return null;
		user.setPassword(null);
		user.setAuthorities(null);
		return user;
	}
 

}
