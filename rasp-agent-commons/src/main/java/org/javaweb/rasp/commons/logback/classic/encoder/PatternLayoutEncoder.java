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
package org.javaweb.rasp.commons.logback.classic.encoder;

import org.javaweb.rasp.commons.logback.classic.PatternLayout;
import org.javaweb.rasp.commons.logback.classic.spi.ILoggingEvent;
import org.javaweb.rasp.commons.logback.core.pattern.PatternLayoutEncoderBase;

public class PatternLayoutEncoder extends PatternLayoutEncoderBase<ILoggingEvent> {

    @Override
    public void start() {
        PatternLayout patternLayout = new PatternLayout();
        patternLayout.setContext(context);
        patternLayout.setPattern(getPattern());
        patternLayout.setOutputPatternAsHeader(outputPatternAsHeader);
        patternLayout.start();
        this.layout = patternLayout;
        super.start();
    }

}
