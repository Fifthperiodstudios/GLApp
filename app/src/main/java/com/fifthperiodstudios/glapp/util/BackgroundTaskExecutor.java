package com.fifthperiodstudios.glapp.util;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BackgroundTaskExecutor implements Executor {
    private final Executor backgroundthread;

    public BackgroundTaskExecutor() {
        backgroundthread= Executors.newSingleThreadExecutor();
    }
    @Override
    public void execute(@NonNull Runnable command) {
        backgroundthread.execute(command);
    }
}
