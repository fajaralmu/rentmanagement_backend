//package com.fajar.rentmanagement.externalapp;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.SerializationUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.hibernate.Criteria;
//import org.hibernate.Session;
//import org.hibernate.criterion.Restrictions;
//
//import com.fajar.rentmanagement.constants.TransactionType;
//import com.fajar.rentmanagement.dto.model.ProductFlowModel;
//import com.fajar.rentmanagement.dto.model.TransactionModel;
//import com.fajar.rentmanagement.entity.TransactionItem;
//import com.fajar.rentmanagement.entity.Transaction;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class TestTransactionRelatedRecords {
//
//	static Session session;
//
//	public static void main(String[] args) {
//		try {
//		session = HibernateSessions.setSession();
//
//		transactionHistory(52L);
//		} catch (Exception  | Error e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		session.close();
//		System.exit(0);
//
//	}
//
//	private static void transactionHistory(Long id) {
//		Transaction record = (Transaction) session.get(Transaction.class, id);
//		final TransactionType type = record.getType();
//		List<TransactionItem> productFlows = getProductFlows(record);
//		Transaction cloned = SerializationUtils.clone(record);
//		cloned.setProductFlows(null);
////		cloned.setCustomer(null);
//		productFlows.forEach(p->{
//			p.setTransaction(cloned);
//			p.setReferenceProductFlow(null);
//		});
//		if (type.equals(TransactionType.TRANS_OUT)) {
//			return;
//		}
//		System.out.println("========= level 1 =========");
//		setReferencingFlows(productFlows);
//
//		if (type.equals(TransactionType.TRANS_OUT_TO_WAREHOUSE)) {
//			return;
//		}
//		System.out.println("========= level 2 ========="); 
//		setReferencingFlows(combineReferencingItems(productFlows));
//
//		summary(record.toModel());
//
//	}
//
//	private static void summary(TransactionModel record) {
//		
//		log.info("Id: {}", record.getId());
//		log.info("Type: {}", record.getType());
//		List<ProductFlowModel> productFlows = record.getProductFlows();
//		int i = 1;
//		for (ProductFlowModel item : productFlows) {
////			if (item == null) continue;
//			log.info("{}. Item id: {}, qty: ({}) --- {}",i,  item.getId(), item.getCount(), transactionDate(item));
//			List<ProductFlowModel> referencing1 = item.getReferencingItems();
//			if (referencing1 != null && !referencing1.isEmpty()) {
//				printReferencing(referencing1, 1);
//			}
//			i++;
//		}
//	}
//
//	private static void printReferencing(List<ProductFlowModel> referencingItems, int level) {
//		int i = 1;
////		System.out.println("referencingItems: "+referencingItems.get(0));
////		 if(true) return;
//		for (ProductFlowModel item : referencingItems) {
//			List<ProductFlowModel> referencing1 = item.getReferencingItems();
//
//			log.info(StringUtils.repeat("  ", level) + i + ". Item id:{}, qty: ({}) --- {}", item.getId(),
//					item.getCount(), transactionDate(item));
//			if (referencing1 != null && !referencing1.isEmpty()) {
//				printReferencing(referencing1, 2);
//			}
//			i++;
//		}
//
//	}
//
//	private static String transactionDate(ProductFlowModel item) {
//		return item.getTransaction().getTransactionDate().toGMTString();
//	}
//
//	private static List<TransactionItem> combineReferencingItems(List<TransactionItem> productFlows) {
//		List<TransactionItem> items = new ArrayList<>();
//		if (null != productFlows)
//			productFlows.forEach(p->{
//				items.addAll(p.getReferencingItems());
//			});
//		return items;
//	}
//
//	private static void setReferencingFlows(List<TransactionItem> productFlows) {
//		List<TransactionItem> referencingFlows = getReferencingFlows(productFlows);
//		mapReferencingFlows(productFlows, referencingFlows);
//	}
//
//	private static void mapReferencingFlows(List<TransactionItem> productFlows, List<TransactionItem> referencingFlows) {
//		Map<Long, List<TransactionItem>> mapped = new HashMap<>();
//		for (TransactionItem productFlow : referencingFlows) {
//			
//			Long refId = productFlow.getReferenceProductFlow().getId();
//			productFlow.setReferenceProductFlow(null);
//			if (mapped.get(refId) == null) {
//				mapped.put(refId, new ArrayList<>());
//			}
//			mapped.get(refId).add(productFlow);
//		}
//		for (TransactionItem productFlow : productFlows) {
//			List<TransactionItem> referencingItems = mapped.get(productFlow.getId());
//			productFlow.setReferencingItems(referencingItems == null ? new ArrayList<>() : referencingItems);
//		}
//	}
//
//	private static List<TransactionItem> getReferencingFlows(List<TransactionItem> productFlows) {
//		 
//		if (productFlows == null || productFlows.isEmpty()) {
//			return new ArrayList<>();
//		}
//		Criteria criteria = session.createCriteria(TransactionItem.class);
//		criteria.add(Restrictions.in("referenceProductFlow", productFlows));
//		return criteria.list();
//	}
//
//	private static List<TransactionItem> getProductFlows(Transaction record) {
//		 
//		Criteria criteria = session.createCriteria(TransactionItem.class);
//		criteria.add(Restrictions.eq("transaction", record));
//		List<TransactionItem> items = criteria.list();
//		record.setProductFlows(items);
//		return items;
//	}
//}
