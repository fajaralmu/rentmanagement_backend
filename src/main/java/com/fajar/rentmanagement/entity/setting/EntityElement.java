package com.fajar.rentmanagement.entity.setting;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;
import javax.persistence.JoinColumn;

import org.springframework.util.Assert;

import com.fajar.rentmanagement.annotation.AdditionalQuestionField;
import com.fajar.rentmanagement.annotation.BaseField;
import com.fajar.rentmanagement.annotation.Dto;
import com.fajar.rentmanagement.annotation.FormField;
import com.fajar.rentmanagement.constants.FieldType;
import com.fajar.rentmanagement.constants.Filterable;
import com.fajar.rentmanagement.dto.model.BaseModel;
import com.fajar.rentmanagement.entity.BaseEntity;
import com.fajar.rentmanagement.util.CollectionUtil;
import com.fajar.rentmanagement.util.EntityUtil;
import com.fajar.rentmanagement.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@AllArgsConstructor
@Slf4j
@JsonInclude(Include.NON_NULL)
public class EntityElement implements Serializable {

	@JsonIgnore
	final ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * 
	 */
	private static final long serialVersionUID = -6768302238247458766L;
	public final boolean ignoreBaseField;
	public final boolean isGrouped;
	public final boolean editable;
	@JsonIgnore
	public final Field field;

	private String id;
	private String type;
	private String className;
	private String labelName;
	private String optionItemName;
	private String optionValueName;
	private String entityReferenceName;
	private String entityReferenceClass;

	private String detailFields;
	private String inputGroupname;
	private String previewLink;
	private String[] defaultValues;

	private List<Object> plainListValues;
	private List<? extends BaseModel> options;

	private boolean identity;
	private boolean required;
	private boolean hasJoinColumn;
	private boolean multiple;
	private boolean detailField;
	private boolean hasPreview;
	@Default
	private boolean filterable = true;
	@Default
	private boolean orderable = true;

	@JsonIgnore
	public EntityProperty entityProperty;
	@JsonIgnore
	private FormField formField;
	@JsonIgnore
	private BaseField baseField;
	@JsonIgnore
	public Map<String, List<?>> additionalMap;
	final FieldType fieldType;

	public EntityElement(Field field, EntityProperty entityProperty, Map<String, List<?>> additionalMap) {
		this.field = field;
		this.ignoreBaseField = entityProperty.isIgnoreBaseField();
		this.entityProperty = entityProperty;
		this.additionalMap = additionalMap;
		this.isGrouped = entityProperty.isQuestionare();
		this.formField = field.getAnnotation(FormField.class);

		if (formField != null) {
			this.fieldType = formField.type();
			this.editable = formField.editable();
			setOptionItemName(formField.optionItemName());
			setRequiredProp(formField);
			
			setFilterableProp();
		} else {
			this.fieldType = FieldType.FIELD_TYPE_TEXT;
			this.editable = false;
		}
		init();
	}

	private void setFilterableProp() {
		if (null == formField) {
			log.debug("formField is null for: {}", this.id);
			return;
		}
		Filterable filterableInfo = formField.filterable();
		boolean enableFilter = filterableInfo.equals(Filterable.ENABLE_ALL);
		boolean _filterable = !filterableInfo.equals(Filterable.DISABLE_ALL) &&
				!filterableInfo.toString().contains("UNFILTERABLE");

		boolean _orderable = !filterableInfo.equals(Filterable.DISABLE_ALL) &&
				!filterableInfo.toString().contains("UNORDERABLE");
		setFilterable(enableFilter || _filterable);
		setOrderable(enableFilter ||_orderable);
	}

	private void init() {

		baseField = field.getAnnotation(BaseField.class);

		checkIfIdField();

		checkIfJoinColumn();
		if (getEntityField() != null) {
			setEntityReferenceClass(getEntityField().getType().getSimpleName());
		}
		checkIfGroupedInput();
	}

	private void checkIfJoinColumn() {
		Field entityField = getEntityField();
		if (null == entityField)
			return;

		JoinColumn joinColumn = entityField.getAnnotation(JoinColumn.class);
		setHasJoinColumn(joinColumn != null);
	}

	private Class<? extends BaseEntity> getEntityClass() {
		Class<? extends BaseModel> modelClass = entityProperty.getModelClass();
		Dto dto = modelClass.getAnnotation(Dto.class);
		if (null == dto)
			return null;

		Class<? extends BaseEntity> entityClass = BaseModel.getEntityClass(modelClass);
		return entityClass;
	}

	private Field getEntityField() {

		Class<? extends BaseEntity> entityClass = getEntityClass();
		Field entityField = EntityUtil.getDeclaredField(entityClass, field.getName());
		return entityField;
	}

	private void checkIfIdField() {

		Field entityField = getEntityField();
		if (null == entityField)
			return;

		Id id = entityField.getAnnotation(Id.class);
		setIdentity(id != null);
	}

	public String getFieldTypeConstants() {
		try {
			return formField.type().toString();
		} catch (Exception e) {
			return null;
		}
	}

	private void checkIfGroupedInput() {

		if (isGrouped) {
			AdditionalQuestionField annotation = field.getAnnotation(AdditionalQuestionField.class);
			inputGroupname = annotation != null ? annotation.value() : AdditionalQuestionField.DEFAULT_GROUP_NAME;
		}
	}

	public boolean build() throws Exception {
		boolean result = doBuild();
		setEntityProperty(null);
		return result;
	}

	public void setRequiredProp(FormField formField) {
		if (formField.type().equals(FieldType.FIELD_TYPE_CHECKBOX)) {
			setRequired(false);
		} else {
			setRequired(formField.required());
		}
	}

	private boolean doBuild() throws Exception {

		boolean formFieldIsNullOrSkip = (formField == null);
		if (formFieldIsNullOrSkip) {
			return false;
		}

		String labelName = formField.labelName().equals("") ? field.getName() : formField.labelName();
		FieldType determinedFieldType = determineFieldType();

		try {

			checkFieldType(determinedFieldType);
			boolean collectionOfBaseEntity = CollectionUtil.isCollectionOfBaseEntity(field);

			if (hasJoinColumn || collectionOfBaseEntity) {
				processJoinColumn(determinedFieldType);
			}

			checkDetailField();
			setLabelName(StringUtil.extractCamelCase(labelName));
			setType(determinedFieldType.value);

			setId(field.getName());
			setMultiple(formField.multipleImage());
			setClassName(field.getType().getCanonicalName());

			setHasPreview(formField.hasPreview());
//			if(isHasPreview()) {
//				setPreviewLink(formField.previewLink());
//			}

		} catch (Exception e1) {
			e1.printStackTrace();
			throw e1;
		}
		return true;
	}

	private void checkDetailField() {

		if (formField.detailFields().length > 0) {
			setDetailFields(String.join("~", formField.detailFields()));
		}
		if (formField.showDetail()) {
			setOptionItemName(formField.optionItemName());
			setDetailField(true);
		}
	}

	private void checkFieldType(FieldType fieldType) throws Exception {

		switch (fieldType) {
		case FIELD_TYPE_IMAGE:
			processImageType();
			break;
		case FIELD_TYPE_CURRENCY:
			processCurrencyType();
			break;
		case FIELD_TYPE_DATE:
			processDateType();
			break;
		case FIELD_TYPE_PLAIN_LIST:
			processPlainListType();
			break;
		case FIELD_TYPE_FIXED_LIST:
			if (formField.multipleSelect()) {
				processMultipleSelectElements();
			}
			break;
		default:
			break;

		}

	}

	private void processMultipleSelectElements() {
//		entityProperty.getMultipleSelectElements().add(field.getName());
	}

	private void processCurrencyType() {
//		entityProperty.getCurrencyElements().add(field.getName());
	}

	private void processImageType() {
//		entityProperty.getImageElements().add(field.getName());
	}

	private void processDateType() {
//		entityProperty.getDateElements().add(field.getName());
	}

	private void processPlainListType() throws Exception {
		log.info("Process Plain List Type: {}", field.getName());
		String[] availableValues = formField.availableValues();
		Object[] arrayOfObject = CollectionUtil.toObjectArray(availableValues);

		if (availableValues.length > 0) {
			setPlainListValues(Arrays.asList(arrayOfObject));

		} else if (field.getType().isEnum()) {
			Object[] enumConstants = field.getType().getEnumConstants();
			setPlainListValues(Arrays.asList(enumConstants));

		} else {
			log.error("Ivalid element: {}", field.getName());
			throw new Exception("Invalid Element");
		}
	}

	private FieldType determineFieldType() {

		FieldType fieldType;

		if (EntityUtil.isNumericField(field)) {
			fieldType = FieldType.FIELD_TYPE_NUMBER;

		} else if (field.getType().equals(Date.class) && field.getAnnotation(JsonFormat.class) == null) {
			fieldType = FieldType.FIELD_TYPE_DATE;

		} else if (identity) {
			fieldType = FieldType.FIELD_TYPE_HIDDEN;
		} else {
			fieldType = formField.type();
		}
		return fieldType;
	}

	private void processJoinColumn(FieldType fieldType) throws Exception {
		log.info("field {} of {} is join column, type: {}", field.getName(), field.getDeclaringClass(), fieldType);

		Field referenceEntityIdField = EntityUtil.getIdFieldOfAnObject(getEntityField());
		System.out.println(fieldType + " referenceEntityIdField: " + referenceEntityIdField);
		if (referenceEntityIdField == null) {
			throw new Exception("ID Field Not Found");
		}

		if (fieldType.equals(FieldType.FIELD_TYPE_FIXED_LIST) && additionalMap != null) {

			List<? extends BaseModel> referenceEntityList = (List<? extends BaseModel>) additionalMap
					.get(field.getName());
			if (null == referenceEntityList || referenceEntityList.size() == 0) {
				throw new RuntimeException(
						"Invalid object list provided for key: " + field.getName() + " in EntityElement.AdditionalMap");
			}
			log.info("Additional map with key: {} . Length: {}", field.getName(), referenceEntityList.size());
			if (referenceEntityList != null) {
				setOptions(referenceEntityList);

			}

		} else if (fieldType.equals(FieldType.FIELD_TYPE_DYNAMIC_LIST)) {

//			setEntityReferenceClass(referenceEntityClass.getSimpleName());
		}

		setEntityReferenceClass(getEntityField().getType().getSimpleName());
		setOptionValueName(referenceEntityIdField.getName());
//		setMultipleSelect(formField.multipleSelect());
		setOptionItemName(formField.optionItemName());
	}

}
