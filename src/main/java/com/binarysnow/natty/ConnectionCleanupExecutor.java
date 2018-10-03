package com.binarysnow.natty;

import java.util.concurrent.Executor;

public class ConnectionCleanupExecutor implements Executor {
    @Override
    public void execute(final Runnable command) {
        new Thread(command).start();
    }
}
