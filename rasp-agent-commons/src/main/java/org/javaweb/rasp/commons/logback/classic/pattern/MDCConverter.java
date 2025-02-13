/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2015, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package org.javaweb.rasp.commons.logback.classic.pattern;

import org.javaweb.rasp.commons.logback.classic.spi.ILoggingEvent;

import java.util.Map;

import static org.javaweb.rasp.commons.logback.core.util.OptionHelper.extractDefaultReplacement;

public class MDCConverter extends ClassicConverter {

    private String key;
    private String defaultValue = "";

    @Override
    public void start() {
        String[] keyInfo = extractDefaultReplacement(getFirstOption());
        key = keyInfo[0];
        if (keyInfo[1] != null) {
            defaultValue = keyInfo[1];
        }
        super.start();
    }

    @Override
    public void stop() {
        key = null;
        super.stop();
    }

    @Override
    public String convert(ILoggingEvent event) {
        Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();

        if (mdcPropertyMap == null) {
            return defaultValue;
        }

        if (key == null) {
            return outputMDCForAllKeys(mdcPropertyMap);
        } else {

            String value = mdcPropertyMap.get(key);
            if (value != null) {
                return value;
            } else {
                return defaultValue;
            }
        }
    }

    /**
     * if no key is specified, return all the values present in the MDC, in the format "k1=v1, k2=v2, ..."
     */
    private String outputMDCForAllKeys(Map<String, String> mdcPropertyMap) {
        StringBuilder buf = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : mdcPropertyMap.entrySet()) {
            if (first) {
                first = false;
            } else {
                buf.append(", ");
            }
            // format: key0=value0, key1=value1
            buf.append(entry.getKey()).append('=').append(entry.getValue());
        }
        return buf.toString();
    }
    
    /**
     * PrefixCompositeConverter needs the key
     * @return
     */
    public String getKey() {
    	return key;
    }
    
}
