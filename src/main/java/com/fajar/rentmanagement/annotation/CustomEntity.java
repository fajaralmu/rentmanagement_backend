package com.fajar.rentmanagement.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fajar.rentmanagement.dto.model.BaseModel;

/**
 * @author Fajar AM
 * 03/10/2019 02.18 PM
 * <p>
 * this annotation is used to map JPA result list into object
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value =
        ElementType.TYPE)
public @interface CustomEntity {

//   public Class<? extends BaseModel> value();

}
