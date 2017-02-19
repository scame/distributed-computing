package com.scame.sliderlab;


import android.util.Log;

public class SecondThreadHandler extends ThreadHandler {

    private static final int UPPER_LIMIT_VAL = 90;

    public SecondThreadHandler(int priority) {
        super(priority);
    }

    @Override
    protected void scheduleUpdates() {
        Log.i("onxSecond", "scheduled");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SliderActivity.seekValue.get() < UPPER_LIMIT_VAL) {
                    SliderActivity.seekValue.incrementAndGet();
                }
                if (!Thread.interrupted()) handler.postDelayed(this, UPDATE_INTERVAL_MS);
            }
        }, UPDATE_INTERVAL_MS);
    }
}
