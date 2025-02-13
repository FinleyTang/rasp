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
package org.javaweb.rasp.commons.logback.classic.spi;

import java.util.concurrent.CopyOnWriteArrayList;

import org.javaweb.rasp.commons.logback.classic.Level;
import org.javaweb.rasp.commons.logback.classic.Logger;
import org.javaweb.rasp.commons.logback.classic.turbo.TurboFilter;
import org.javaweb.rasp.commons.slf4j.Marker;

import org.javaweb.rasp.commons.logback.core.spi.FilterReply;

/**
 * Implementation of TurboFilterAttachable.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
final public class TurboFilterList extends CopyOnWriteArrayList<TurboFilter> {

    private static final long serialVersionUID = 1L;

    /**
     * Loop through the filters in the chain. As soon as a filter decides on
     * ACCEPT or DENY, then that value is returned. If all of the filters return
     * NEUTRAL, then NEUTRAL is returned.
     */
    public FilterReply getTurboFilterChainDecision(final Marker marker, final Logger logger, final Level level, final String format, final Object[] params,
                                                   final Throwable t) {

        final int size = size();
        // if (size == 0) {
        // return FilterReply.NEUTRAL;
        // }
        if (size == 1) {
            try {
                TurboFilter tf = get(0);
                return tf.decide(marker, logger, level, format, params, t);
            } catch (IndexOutOfBoundsException iobe) {
                return FilterReply.NEUTRAL;
            }
        }

        Object[] tfa = toArray();
        final int len = tfa.length;
        for (int i = 0; i < len; i++) {
            // for (TurboFilter tf : this) {
            final TurboFilter tf = (TurboFilter) tfa[i];
            final FilterReply r = tf.decide(marker, logger, level, format, params, t);
            if (r == FilterReply.DENY || r == FilterReply.ACCEPT) {
                return r;
            }
        }
        return FilterReply.NEUTRAL;
    }

    // public boolean remove(TurboFilter turboFilter) {
    // return tfList.remove(turboFilter);
    // }
    //
    // public TurboFilter remove(int index) {
    // return tfList.remove(index);
    // }
    //
    // final public int size() {
    // return tfList.size();
    // }

}
