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
package org.javaweb.rasp.commons.logback.core.recovery;

import java.io.IOException;
import java.io.OutputStream;

import org.javaweb.rasp.commons.logback.core.Context;
import org.javaweb.rasp.commons.logback.core.status.ErrorStatus;
import org.javaweb.rasp.commons.logback.core.status.InfoStatus;
import org.javaweb.rasp.commons.logback.core.status.Status;
import org.javaweb.rasp.commons.logback.core.status.StatusManager;

abstract public class ResilientOutputStreamBase extends OutputStream {

    final static int STATUS_COUNT_LIMIT = 2 * 4;

    private int noContextWarning = 0;
    private int statusCount = 0;

    private Context             context;
    private RecoveryCoordinator recoveryCoordinator;

    protected OutputStream os;
    protected boolean presumedClean = true;

    private boolean isPresumedInError() {
        // existence of recoveryCoordinator indicates failed state
        return (recoveryCoordinator != null && !presumedClean);
    }

    public void write(byte b[], int off, int len) {
        if (isPresumedInError()) {
            if (!recoveryCoordinator.isTooSoon()) {
                attemptRecovery();
            }
            return; // return regardless of the success of the recovery attempt
        }

        try {
            os.write(b, off, len);
            postSuccessfulWrite();
        } catch (IOException e) {
            postIOFailure(e);
        }
    }

    @Override
    public void write(int b) {
        if (isPresumedInError()) {
            if (!recoveryCoordinator.isTooSoon()) {
                attemptRecovery();
            }
            return; // return regardless of the success of the recovery attempt
        }
        try {
            os.write(b);
            postSuccessfulWrite();
        } catch (IOException e) {
            postIOFailure(e);
        }
    }

    @Override
    public void flush() {
        if (os != null) {
            try {
                os.flush();
                postSuccessfulWrite();
            } catch (IOException e) {
                postIOFailure(e);
            }
        }
    }

    abstract String getDescription();

    abstract OutputStream openNewOutputStream() throws IOException;

    private void postSuccessfulWrite() {
        if (recoveryCoordinator != null) {
            recoveryCoordinator = null;
            statusCount = 0;
            addStatus(new InfoStatus("Recovered from IO failure on " + getDescription(), this));
        }
    }

    public void postIOFailure(IOException e) {
        addStatusIfCountNotOverLimit(new ErrorStatus("IO failure while writing to " + getDescription(), this, e));
        presumedClean = false;
        if (recoveryCoordinator == null) {
            recoveryCoordinator = new RecoveryCoordinator();
        }
    }

    @Override
    public void close() throws IOException {
        if (os != null) {
            os.close();
        }
    }

    void attemptRecovery() {
        try {
            close();
        } catch (IOException e) {
        }

        addStatusIfCountNotOverLimit(new InfoStatus("Attempting to recover from IO failure on " + getDescription(), this));

        // subsequent writes must always be in append mode
        try {
            os = openNewOutputStream();
            presumedClean = true;
        } catch (IOException e) {
            addStatusIfCountNotOverLimit(new ErrorStatus("Failed to open " + getDescription(), this, e));
        }
    }

    void addStatusIfCountNotOverLimit(Status s) {
        ++statusCount;
        if (statusCount < STATUS_COUNT_LIMIT) {
            addStatus(s);
        }

        if (statusCount == STATUS_COUNT_LIMIT) {
            addStatus(s);
            addStatus(new InfoStatus("Will supress future messages regarding " + getDescription(), this));
        }
    }

    public void addStatus(Status status) {
        if (context == null) {
            if (noContextWarning++ == 0) {
                System.out.println("LOGBACK: No context given for " + this);
            }
            return;
        }
        StatusManager sm = context.getStatusManager();
        if (sm != null) {
            sm.add(status);
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
