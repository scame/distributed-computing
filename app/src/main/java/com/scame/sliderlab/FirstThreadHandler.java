package com.scame.sliderlab;


public class FirstThreadHandler extends ThreadHandler {

    private static final int LOWER_LIMIT_VAL = 10;

    @Override
    protected void scheduleUpdates() {
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
