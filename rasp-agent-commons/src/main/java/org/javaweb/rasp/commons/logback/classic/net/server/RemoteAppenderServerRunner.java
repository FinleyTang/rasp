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
package org.javaweb.rasp.commons.logback.classic.net.server;

import java.util.concurrent.Executor;

import org.javaweb.rasp.commons.logback.classic.LoggerContext;
import org.javaweb.rasp.commons.logback.core.net.server.ConcurrentServerRunner;
import org.javaweb.rasp.commons.logback.core.net.server.ServerListener;
import org.javaweb.rasp.commons.logback.core.net.server.ServerRunner;

/**
 * A {@link ServerRunner} that receives logging events from remote appender
 * clients.
 *
 * @author Carl Harris
 */
class RemoteAppenderServerRunner extends ConcurrentServerRunner<RemoteAppenderClient> {

    /**
     * Constructs a new server runner.
     * @param listener the listener from which the server will accept new
     *    clients
     * @param executor that will be used to execute asynchronous tasks 
     *    on behalf of the runner.
     */
    public RemoteAppenderServerRunner(ServerListener<RemoteAppenderClient> listener, Executor executor) {
        super(listener, executor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean configureClient(RemoteAppenderClient client) {
        client.setLoggerContext((LoggerContext) getContext());
        return true;
    }

}
