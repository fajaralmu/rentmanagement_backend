/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fajar.rentmanagement.dto.model;

import java.io.Serializable;

import com.fajar.rentmanagement.entity.Product;
import com.fajar.rentmanagement.entity.TransactionItem;
import com.fajar.rentmanagement.entity.Transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author fajar
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DrugStockModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4549400312671526707L;
	private Integer id;
	private Product product;
	private Transaction transaction;
	// private Date kadaluarsa;
	private int count;
	private TransactionItem productFlow;
	private int incomingCount ;
	private int disributedCount;

	// UNTUK KEPERLUAN VIEW
	// private String kodegudang;
	// private String namaobat, namasatuan;
	// private Date kadaluarsa, tgltransaksi;
	private int expStatus;
 
}
