/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fajar.rentmanagement.dto.model;

import java.util.Date;
import java.util.List;

import com.fajar.rentmanagement.annotation.Dto;
import com.fajar.rentmanagement.annotation.FormField;
import com.fajar.rentmanagement.constants.FieldType;
import com.fajar.rentmanagement.constants.Filterable;
import com.fajar.rentmanagement.entity.TransactionItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author fajar
 */
@Dto(value="Transaction Item", updateService = "productFlowUpdateService", creatable= false, withProgressWhenUpdated = true)
@JsonInclude(value=Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionItemModel extends BaseModel<TransactionItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8839593046741372229L;

 

	@FormField(optionItemName = "name", editable = false)
	private ProductModel product;

	@FormField(optionItemName = "code", editable = false)
	 
	private TransactionModel transaction; 
	@FormField(labelName = "Trans. Type", optionItemName = "type", entityField="transaction", editable = false) 
	private TransactionModel transaction2;
	
	@FormField(type=FieldType.FIELD_TYPE_DATE)
	private Date expiredDate;
	@FormField(type=FieldType.FIELD_TYPE_NUMBER)
	private int count;
	@FormField(type=FieldType.FIELD_TYPE_NUMBER)
	private int usedCount;
	@FormField(type=FieldType.FIELD_TYPE_NUMBER, filterable = Filterable.UNFILTERABLE_ORDERABLE)
	private int stock;
	
	@Setter(value = AccessLevel.NONE)
	@FormField(optionItemName = "id", editable = false) 
	private TransactionItemModel referenceProductFlow;
//	@FormField
//	private Long refStockId;

	@Default
	@FormField(type=FieldType.FIELD_TYPE_CHECKBOX)
	private boolean suitable = true;
	@FormField(type=FieldType.FIELD_TYPE_NUMBER)
	private double price;
	@FormField(type=FieldType.FIELD_TYPE_CHECKBOX)
	private boolean generic;  
	
	private String stockLocation;
	
	private List<TransactionItemModel> referencingItems;
	 
 	public String getType() {
 		try {
 			return transaction.getType().toString();
 		} catch (Exception e) {
 			return null;
		}
	}
	public int getStock() { 
		return count - usedCount;
	}
	
	/**
	 * make the expDate same as referenceProductFlow.expDate
	 */
	public void setExpDate() {
		if (null == referenceProductFlow) {
			return;
		}
		setExpiredDate(referenceProductFlow.getExpiredDate());
	}


	public void setReferenceProductFlow(TransactionItemModel referenceFlow) {
		if (null != referenceFlow && null != referenceFlow.getProduct()) {
			setProduct(referenceFlow.getProduct());
		}
		if (referenceFlow != null) {
			this.setGeneric(referenceFlow.isGeneric());
		}
		this.referenceProductFlow = referenceFlow;
		this.setExpDate();
	} 
}
