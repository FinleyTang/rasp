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
package org.javaweb.rasp.commons.logback.core.property;

import org.javaweb.rasp.commons.logback.core.PropertyDefinerBase;
import org.javaweb.rasp.commons.logback.core.util.OptionHelper;
import org.javaweb.rasp.commons.logback.core.joran.action.PropertyAction;

import java.io.File;

/**
 * In conjunction with {@link PropertyAction} sets
 * the named variable to "true" if the file specified by {@link #setPath(String) path}
 * property exists, to "false" otherwise.
 *
 * @see #getPropertyValue()
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public class FileExistsPropertyDefiner extends PropertyDefinerBase {

    String path;

    public String getPath() {
        return path;
    }

    /**
     * The path for the file to search for.
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Returns "true" if the file specified by {@link #setPath(String) path} property exists.
     * Returns "false" otherwise.
     *
     * @return "true"|"false" depending on the existence of file
     */
    public String getPropertyValue() {
        if (OptionHelper.isEmpty(path)) {
            addError("The \"path\" property must be set.");
            return null;
        }

        File file = new File(path);
        return booleanAsStr(file.exists());
    }
}
