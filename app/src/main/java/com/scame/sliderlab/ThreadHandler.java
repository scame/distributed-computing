package com.scame.sliderlab;


import android.os.Handler;
import android.os.HandlerThread;

public abstract class ThreadHandler {

    static final int UPDATE_INTERVAL_MS = 5;

    private HandlerThread handlerThread;

    Handler handler;

    public ThreadHandler() {
        this.handlerThread = new HandlerThread(this.getClass().getCanonicalName());
        this.handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        scheduleUpdates();
    }

    protected abstract void scheduleUpdates();

    protected void setPriority(int priority) {
        handlerThread.setPriority(priority);
    }

    protected void interrupt() {
        handlerThread.interrupt();
    }
}
