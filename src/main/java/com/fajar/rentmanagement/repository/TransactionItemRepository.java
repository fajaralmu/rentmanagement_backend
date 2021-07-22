package com.fajar.rentmanagement.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fajar.rentmanagement.entity.Transaction;
import com.fajar.rentmanagement.entity.TransactionItem;

@Repository
public interface TransactionItemRepository extends JpaRepository<TransactionItem, Long> {

	default List<TransactionItem> empty(){
		return new ArrayList<>();
	}

	List<TransactionItem> findByTransactionIn(List<Transaction> objects);
	 

	
}