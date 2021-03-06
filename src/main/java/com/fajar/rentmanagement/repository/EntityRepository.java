package com.fajar.rentmanagement.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.JoinColumn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.fajar.rentmanagement.annotation.CustomEntity;
import com.fajar.rentmanagement.annotation.Dto;
import com.fajar.rentmanagement.dto.model.BaseModel;
import com.fajar.rentmanagement.entity.BaseEntity;
import com.fajar.rentmanagement.entity.setting.EntityManagementConfig;
import com.fajar.rentmanagement.entity.setting.EntityUpdateInterceptor;
import com.fajar.rentmanagement.service.config.WebConfigService;
import com.fajar.rentmanagement.service.entity.BaseEntityUpdateService;
import com.fajar.rentmanagement.util.CollectionUtil;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@Data
public class EntityRepository {

	@Autowired
	private WebConfigService webConfigService;
	@Autowired
	private CustomRepositoryImpl customRepository;
	@Autowired
	private ApplicationContext applicationContext; 
	 

	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private final Map<String, EntityManagementConfig> entityConfiguration = new HashMap<String, EntityManagementConfig>();

	/**
	 * put configuration to entityConfiguration map
	 * 
	 * @param _class
	 * @param updateService
	 * @param updateInterceptor
	 */
	private void putConfig(Class<? extends BaseEntity> _class, BaseEntityUpdateService updateService,
			EntityUpdateInterceptor<?> updateInterceptor) {
		String key = _class.getSimpleName().toLowerCase();
		entityConfiguration.put(key, config(key, _class, updateService, updateInterceptor));
		log.info("put entity config, key: {} - class: {}", key, _class);
	}

	@PostConstruct
	public void init() throws Exception {
		putEntitiesConfig(); 
	}

	private void putEntitiesConfig() throws Exception {
		entityConfiguration.clear();

		List<Type> persistenceClasses = webConfigService.getEntityClassess();
		log.info(">>>> persistenceClasses count: {}", persistenceClasses.size());
		for (Type type : persistenceClasses) {
			log.info("checking : {}", type);
			try {
				Class<? extends BaseEntity> entityClass = (Class<? extends BaseEntity>) type;
				CustomEntity customEntity =  entityClass.getAnnotation(CustomEntity.class);
				if (null == customEntity) {
					log.info(" SKIP {}, cause = customEntity is null", type);
					continue;
				}
				Class<? extends BaseModel> modelClass = BaseEntity.getModelClass(entityClass);
				if (null == modelClass.getAnnotation(Dto.class)) {
					log.info(" SKIP {}, cause = {}'s Dto is null", type, modelClass);
					continue;
				}
				String beanName = modelClass.getAnnotation(Dto.class).updateService();
//				String beanName = StringUtil.lowerCaseFirstChar(updateServiceClass.getSimpleName());

				BaseEntityUpdateService updateServiceBean = (BaseEntityUpdateService) applicationContext
						.getBean(beanName);
				EntityUpdateInterceptor updateInterceptor = ((BaseEntity) entityClass.newInstance())
						.modelUpdateInterceptor();

				log.info("Registering entity config: {}, updateServiceBean: {}", entityClass.getSimpleName(),
						updateServiceBean);

				putConfig(entityClass, updateServiceBean, updateInterceptor);
			} catch (Exception e) {
				log.error("Error registering entity: {}", type.getTypeName());
				e.printStackTrace();
			}

		}
		log.info("///////////// END PUT ENTITY CONFIGS: {} //////////////", entityConfiguration.size());
	}

	/**
	 * get entity configuration from map by entity code
	 * 
	 * @param key
	 * @return
	 */
	public EntityManagementConfig getConfig(String entityCode) {
		return entityConfiguration.get(entityCode);
	}

	/**
	 * construct EntityManagementConfig object
	 * 
	 * @param object
	 * @param class1
	 * @param commonUpdateService2
	 * @param updateInterceptor
	 * @return
	 */
	private EntityManagementConfig config(String object, Class<? extends BaseEntity> class1,
			BaseEntityUpdateService commonUpdateService2, EntityUpdateInterceptor updateInterceptor) {
		return new EntityManagementConfig(object, class1, commonUpdateService2, updateInterceptor);
	}

	/**
	 * save entity
	 * 
	 * @param <T>
	 * @param baseEntity
	 * @return
	 */
	public <T extends BaseEntity, ID> T save(T baseEntity) {
		log.info("execute method save");

		boolean joinEntityExist = validateJoinColumn(baseEntity);

		if (!joinEntityExist) {

			throw new InvalidParameterException("JOIN COLUMN INVALID");
		}

		try {
			return savev2(baseEntity);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			//databaseProcessorNotifier.refresh();
		}
	}

	public <T extends BaseEntity> T savev2(T entity) {
		DatabaseProcessor databatseProcessor = customRepository.createDatabaseProcessor();
		T result = databatseProcessor.saveObject(entity); 
		 
		return result;

	}

	public <T extends BaseEntity> boolean validateJoinColumn(T baseEntity) {

		List<Field> joinColumns = getJoinColumn(baseEntity.getClass());

		if (joinColumns.size() == 0) {
			return true;
		}

		for (Field field : joinColumns) {

			try {
				field.setAccessible(true);
				Object value = field.get(baseEntity);
				if (value == null || (value instanceof BaseEntity) == false) {
					continue;
				}

				BaseEntity entity = (BaseEntity) value; 
				BaseEntity result = findById(entity.getClass(), entity.getId());

				if (result == null) {
					return false;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		}

		return true;
	}

	public List<Field> getJoinColumn(Class<? extends BaseEntity> clazz) {

		List<Field> joinColumns = new ArrayList<>();
		Field[] fields = clazz.getFields();

		for (Field field : fields) {
			if (field.getAnnotation(JoinColumn.class) != null) {
				joinColumns.add(field);
			}
		}

		return joinColumns;
	}

	/**
	 * find suitable repository (declared in this class) for given entity object
	 * 
	 * @param entityClass
	 * @return
	 */
	public <T extends BaseEntity> JpaRepository findRepo(Class<T> entityClass) {
		JpaRepository repository = webConfigService.getJpaRepository(entityClass);
		return repository;
	}

	/**
	 * find by id
	 * 
	 * @param clazz
	 * @param ID
	 * @return
	 */
	public <ID, T extends BaseEntity> T findById(Class<T> clazz, ID ID) {
		log.info("find {} By Id: {}", clazz.getSimpleName(), ID);
		JpaRepository<T, ID> repository = findRepo(clazz);

		log.info("found repo : {} for {}", repository.getClass(), clazz);

		Optional<T> result = repository.findById(ID);
		if (result.isPresent()) {
			return result.get();
		}
		log.debug("{} is NULL", clazz.getSimpleName());
		return null;
	}
	
//	public <ID extends Serializable, T extends BaseEntity> T findByIdv2(Class<T> _class, ID id) {
//		T result = databaseReader.getById(_class, id);
//		
//		return result;
//	}

	/**
	 * find all entity
	 * 
	 * @param clazz
	 * @return
	 */
	public <T extends BaseEntity> List<T> findAll(Class<T> clazz) {
		JpaRepository repository = findRepo(clazz);
		if (repository == null) {
			return CollectionUtil.emptyList();
		}
		return repository.findAll();
	}

	/**
	 * delete entity by id
	 * 
	 * @param id
	 * @param class1
	 * @return
	 */
	public <T extends BaseEntity> boolean deleteById(Long id, Class<T> class1) {
		log.info("Will delete entity: {}, id: {}", class1.getClass(), id);
		DatabaseProcessor databatseProcessor = customRepository.createDatabaseProcessor();
		return databatseProcessor.deleteObjectById(class1, id);

	}

	public EntityManagementConfig getConfiguration(String key) {
		return this.entityConfiguration.get(key);
	}

	public List findByKey(Class entityClass, Field idField, Object... objectArray) {
		 
		DatabaseProcessor databatseProcessor = customRepository.createDatabaseProcessor();
		return databatseProcessor.findByKeyAndValues(entityClass,idField.getName() , objectArray);
	}

}
