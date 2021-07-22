package com.fajar.rentmanagement.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fajar.rentmanagement.constants.TransactionType;
import com.fajar.rentmanagement.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>  {

	Transaction findByCode(String code);

	List<Transaction> findByTransactionDateBefore(Date time);

	List<Transaction> findByTransactionDate(Date d);

	List<Transaction> findByTypeAndTransactionDateBefore(TransactionType transIn, Date d);

 
 
	  
}
