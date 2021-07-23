package com.fajar.rentmanagement.service.transaction;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.rentmanagement.constants.TransactionType;
import com.fajar.rentmanagement.dto.WebRequest;
import com.fajar.rentmanagement.dto.WebResponse;
import com.fajar.rentmanagement.entity.Product;
import com.fajar.rentmanagement.entity.ProductStock;
import com.fajar.rentmanagement.entity.Transaction;
import com.fajar.rentmanagement.entity.TransactionItem;
import com.fajar.rentmanagement.service.SessionValidationService;

import io.jsonwebtoken.lang.Assert;

@Service
public class TransactionService {
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private SessionValidationService sessionValidationService;

	public WebResponse addStock(WebRequest request, HttpServletRequest httpRequest, boolean b) throws Exception {
		Session session = sessionFactory.openSession();
		org.hibernate.Transaction tx = session.beginTransaction();
		try {
			Transaction transaction = request.getTransaction().toEntity(); 
			validateInput(transaction, session);

			List<TransactionItem> items = transaction.getItems();
			transaction.setUser(sessionValidationService.getLoggedUser(httpRequest));
			transaction.setType(TransactionType.TRANS_IN);
			transaction.setItems(null);
			transaction.setTypeAndCode();
			
			Long id = (Long) session.save(transaction);
			transaction.setId(id);
			
			for (TransactionItem item : items) {
				item.setTransaction(transaction);
				updateStock(item, session);
				session.save(item);
			}
			tx.commit();
			transaction.setItemsAndRemoveAssociation(items);
			return WebResponse.builder().transaction(transaction.toModel()).build();
		} catch (Exception e) {
			if (null != tx) {
				tx.rollback();
			}
			throw e;
		} finally {
			if (null != session) {
				session.close();
			}
		}
		 
	}

	private void updateStock(TransactionItem item, Session session) {
		Criteria criteria = session.createCriteria(ProductStock.class);
		criteria.add(Restrictions.eq("product.id", item.getProduct().getId()));
		List result = criteria.list();
		ProductStock productStock;
		if (result.size() == 0) {
			productStock = new ProductStock(item.getProduct(), item.getCount());
			session.save(productStock);
		} else {
			productStock = (ProductStock) result.get(0);
			productStock.addCount(item.getCount());
			session.merge(productStock);
		}
		
		
	}

	private static void validateInput(Transaction transaction, Session session) throws Exception {
		  
		Assert.notEmpty(transaction.getItems(), "Items must not empty");
		Objects.requireNonNull(transaction.getSupplier(), "Supplier not present");
		
		if (null == transaction.getTransactionDate()) {
			transaction.setTransactionDate(new Date());
		}
		for (TransactionItem item : transaction.getItems()) {
			Objects.requireNonNull(item.getProduct(), "Product not present");
			Object existing = session.get(Product.class, item.getProduct().getId());
			Objects.requireNonNull(existing, "Existing product not present");
		}
		
	}

}
