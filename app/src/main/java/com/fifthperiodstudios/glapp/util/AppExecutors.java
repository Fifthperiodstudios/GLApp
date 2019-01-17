package com.fifthperiodstudios.glapp.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

public class AppExecutors {
    private final Executor backgroundThread;
    private final Executor mainThread;

    public AppExecutors() {
        backgroundThread = new BackgroundTaskExecutor();
        mainThread = new MainThreadExecutor();
    }

    private static class MainThreadExecutor implements Executor{
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    public Executor getBackgroundThread() {
        return backgroundThread;
    }

    public Executor getMainThread() {
        return mainThread;
    }
}
