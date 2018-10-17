/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.service.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author os_namndh
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    
    String columnName();
}
