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
package org.javaweb.rasp.commons.logback.core.layout;

import org.javaweb.rasp.commons.logback.core.CoreConstants;
import org.javaweb.rasp.commons.logback.core.LayoutBase;

/**
 * Echos the incoming object adding a line separator character(s) at the end.
 * 
 * @author Ceki
 */
public class EchoLayout<E> extends LayoutBase<E> {

    public String doLayout(E event) {
        return event + CoreConstants.LINE_SEPARATOR;
    }

}
