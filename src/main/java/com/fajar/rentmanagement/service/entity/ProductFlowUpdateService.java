//package com.fajar.rentmanagement.service.entity;
//
//import java.util.Optional;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.fajar.rentmanagement.dto.WebResponse;
//import com.fajar.rentmanagement.entity.ProductFlow;
//import com.fajar.rentmanagement.exception.ApplicationException;
//import com.fajar.rentmanagement.exception.DataNotFoundException;
//import com.fajar.rentmanagement.repository.ProductFlowRepository;
//import com.fajar.rentmanagement.service.inventory.InventoryService;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Service
//public class ProductFlowUpdateService extends BaseEntityUpdateService<ProductFlow> {
//
//	@Autowired
//	private ProductFlowRepository productFlowRepository;
//	@Autowired
//	private InventoryService inventoryService;
//	@Autowired
//	private SessionFactory sessionFactory;
//	
//	@Override
//	public ProductFlow saveEntity(ProductFlow object, boolean newRecord, HttpServletRequest httpServletRequest)
//			throws Exception {
//		log.info("Save product flow");
//		Optional<ProductFlow> existingObjectOpt = productFlowRepository.findById(object.getId());
//		if (existingObjectOpt.isPresent() == false) {
//			throw new ApplicationException("existing product flow not found");
//		}
//		ProductFlow existingObject = existingObjectOpt.get();
//		existingObject.setCount(object.getCount());
//		existingObject.setPrice(object.getPrice());
//		existingObject.setSuitable(object.isSuitable());
//		
//		if (false == existingObject.isDistributed ()) {
//			existingObject.setExpiredDate(object.getExpiredDate());
//			existingObject.setGeneric(object.isGeneric());
//		}
//		
//		ProductFlow saved = entityRepository.save(existingObject);
//		inventoryService.adjustStock(httpServletRequest);
//		return saved;
//	}
//	
//	@Override
//	public WebResponse deleteEntity(Long id, Class _class, HttpServletRequest httpServletRequest) throws Exception {
//		Session session = sessionFactory.openSession();
//		Transaction tx = session.beginTransaction();
//		try {
//			ProductFlow existing = (ProductFlow) session.get(ProductFlow.class, id);
//			if (existing == null) {
//				throw new DataNotFoundException("Record not found");
//			}
//			session.delete(existing);
//			tx.commit();
//			inventoryService.adjustStock(httpServletRequest);
//			return new WebResponse();
//		} catch (Exception e) {
//			
//			if (tx != null)
//				tx.rollback();
//			throw new ApplicationException(e.getMessage());
//		} finally {
//			session.close();
//		}
//		 
//	}
//}
