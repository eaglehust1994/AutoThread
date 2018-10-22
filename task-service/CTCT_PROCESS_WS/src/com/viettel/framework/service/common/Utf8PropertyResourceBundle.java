/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.service.common;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 *
 * @author tuanpv14
 */
public class Utf8PropertyResourceBundle extends ResourceBundle {

    /**
     * Bundle with unicode data
     */
    private final PropertyResourceBundle bundle;

    /**
     * Initializing constructor
     *
     * @param bundle
     */
    public Utf8PropertyResourceBundle(final PropertyResourceBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Enumeration getKeys() {
        return bundle.getKeys();
    }

    @Override
    public Object handleGetObject(final String key) {
        final String value = bundle.getString(key);
        if (value == null) {
            return null;
        }
        try {
            return new String(value.getBytes("ISO-8859-1"), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported", e);
        }
    }
}
