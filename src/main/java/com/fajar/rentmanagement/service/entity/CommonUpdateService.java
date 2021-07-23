package com.fajar.rentmanagement.service.entity;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.fajar.rentmanagement.entity.BaseEntity;
import com.fajar.rentmanagement.entity.setting.EntityUpdateInterceptor;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommonUpdateService<T extends BaseEntity > extends BaseEntityUpdateService<T> {

	
	@Override
	public T saveEntity(T entity, boolean newRecord, HttpServletRequest httpServletRequest) {
		log.info("saving entity: {}", entity.getClass());
		entity = copyNewElement(entity, newRecord);
		
		validateEntityFormFields(entity, newRecord, httpServletRequest);
		
		interceptPreUpdate(entity);
		T newEntity = entityRepository.save(entity);
		return newEntity;
	}

	/**
	 * execute things before persisting
	 * 
	 * @param entity
	 * @param updateInterceptor
	 */
	private void interceptPreUpdate(BaseEntity entity) {
		EntityUpdateInterceptor<BaseEntity> updateInterceptor = entity.modelUpdateInterceptor();
		if (null != updateInterceptor && null != entity) {
			log.info("Pre Update {}", entity.getClass().getSimpleName());
			try {
				updateInterceptor.preUpdate(entity);
				log.info("success pre update");
			} catch (Exception e) {

				log.error("Error pre update entity");
				e.printStackTrace();
				throw e;
			}
		}
	}

	
}
