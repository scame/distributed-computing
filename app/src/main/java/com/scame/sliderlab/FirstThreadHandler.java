package com.scame.sliderlab;


import android.util.Log;

public class FirstThreadHandler extends ThreadHandler {

    private static final int LOWER_LIMIT_VAL = 10;

    public FirstThreadHandler(int priority) {
        super(priority);
    }

    @Override
    protected void scheduleUpdates() {
        Log.i("onxFirst", "scheduled");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SliderActivity.seekValue.get() > LOWER_LIMIT_VAL) {
                    SliderActivity.seekValue.decrementAndGet();
                }
                if (!Thread.interrupted()) handler.postDelayed(this, UPDATE_INTERVAL_MS);
            }
        }, UPDATE_INTERVAL_MS);
    }
}
