package com.fajar.rentmanagement.dto;

import java.io.Serializable;
import java.util.List;

import com.fajar.rentmanagement.dto.model.ApplicationProfileModel;
import com.fajar.rentmanagement.dto.model.BaseModel;
import com.fajar.rentmanagement.dto.model.ConfigurationModel;
import com.fajar.rentmanagement.dto.model.CustomerModel;
import com.fajar.rentmanagement.dto.model.ProductModel;
import com.fajar.rentmanagement.dto.model.SupplierModel;
import com.fajar.rentmanagement.dto.model.TransactionItemModel;
import com.fajar.rentmanagement.dto.model.TransactionModel;
import com.fajar.rentmanagement.dto.model.UnitModel;
import com.fajar.rentmanagement.dto.model.UserModel;

import lombok.Data;

@Data 
public class WebRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 110411933791444017L;


	
	/**
	 * ENTITY CRUD use lowerCase!!!
	 */

	private String entity;
	
	private CustomerModel customer; 
	private ProductModel product;
	private SupplierModel supplier;
	private UnitModel unit;
	private TransactionItemModel productflow;

	/**
	 * ==========end entity============
	 */

	private Filter filter; 
	
	private UserModel user; 
	private ApplicationProfileModel profile; 
	private ConfigurationModel inventoryConfiguration;
	private BaseModel entityObject; 
	private TransactionModel transaction;
	private List<BaseModel > orderedEntities; 
	
	private boolean regularTransaction; 
	
	private String imageData; 

}
