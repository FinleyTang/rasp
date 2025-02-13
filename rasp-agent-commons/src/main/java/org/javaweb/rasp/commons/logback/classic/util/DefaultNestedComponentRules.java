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
package org.javaweb.rasp.commons.logback.classic.util;

import org.javaweb.rasp.commons.logback.classic.PatternLayout;
import org.javaweb.rasp.commons.logback.classic.encoder.PatternLayoutEncoder;
import org.javaweb.rasp.commons.logback.core.AppenderBase;
import org.javaweb.rasp.commons.logback.core.UnsynchronizedAppenderBase;
import org.javaweb.rasp.commons.logback.core.joran.spi.DefaultNestedComponentRegistry;
import org.javaweb.rasp.commons.logback.core.net.ssl.SSLNestedComponentRegistryRules;

/**
 * Contains mappings for the default type of nested components in
 * logback-classic.
 * 
 * @author Ceki Gulcu
 * 
 */
public class DefaultNestedComponentRules {

    static public void addDefaultNestedComponentRegistryRules(DefaultNestedComponentRegistry registry) {
        registry.add(AppenderBase.class, "layout", PatternLayout.class);
        registry.add(UnsynchronizedAppenderBase.class, "layout", PatternLayout.class);

        registry.add(AppenderBase.class, "encoder", PatternLayoutEncoder.class);
        registry.add(UnsynchronizedAppenderBase.class, "encoder", PatternLayoutEncoder.class);

        SSLNestedComponentRegistryRules.addDefaultNestedComponentRegistryRules(registry);
    }

}
