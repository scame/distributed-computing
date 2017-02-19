package com.scame.sliderlab;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SliderActivity extends AppCompatActivity {

    public static final int UPDATE_INTERVAL_MS = 100;

    static final AtomicInteger seekValue = new AtomicInteger(50);

    private FirstThreadHandler firstThreadHandler;

    private SecondThreadHandler secondThreadHandler;

    @BindView(R.id.seekbar)
    SeekBar seekBar;

    @BindView(R.id.thread1_group)
    RadioGroup radioGroup1;

    @BindView(R.id.thread2_group)
    RadioGroup radioGroup2;

    private BusyThreadsHandler busyThreadsHandler;

    private Handler updateHandler;

    private boolean started;

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(seekValue.get());
            updateHandler.postDelayed(this, UPDATE_INTERVAL_MS);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        ButterKnife.bind(this);
        updateHandler = new Handler();

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        makeCoresBusy(availableProcessors - 2);
        setupRadioGroups();
        scheduleUpdates();
    }

    private void makeCoresBusy(int threadNumber) {
        busyThreadsHandler = new BusyThreadsHandler(threadNumber);
        busyThreadsHandler.activate();
    }

    private void setupRadioGroups() {
        radioGroup1.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (!started) return;
            switch (checkedId) {
                case R.id.max_btn1:
                    firstThreadHandler.setPriority(Thread.MAX_PRIORITY);
                    break;
                case R.id.normal_btn1:
                    firstThreadHandler.setPriority(Thread.NORM_PRIORITY);
                    break;
                case R.id.min_btn1:
                    firstThreadHandler.setPriority(Thread.MIN_PRIORITY);
                    break;
            }
        });

        radioGroup2.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (!started) return;
            switch (checkedId) {
                case R.id.max_btn2:
                    secondThreadHandler.setPriority(Thread.MAX_PRIORITY);
                    break;
                case R.id.normal_btn2:
                    secondThreadHandler.setPriority(Thread.NORM_PRIORITY);
                    break;
                case R.id.min_btn2:
                    secondThreadHandler.setPriority(Thread.MIN_PRIORITY);
                    break;
            }
        });
    }

    private void scheduleUpdates() {
        updateHandler.postDelayed(updateRunnable, UPDATE_INTERVAL_MS);
    }

    @OnClick(R.id.start_btn)
    public void onStartButtonClick(View view) {
        started = true;
        view.setClickable(false);
        view.setEnabled(false);

        firstThreadHandler = new FirstThreadHandler();
        secondThreadHandler = new SecondThreadHandler();
        firstThreadHandler.setPriority(getFirstThreadPriority());
        secondThreadHandler.setPriority(getSecondThreadPriority());
    }

    public int getFirstThreadPriority() {
        int checkedId = radioGroup1.getCheckedRadioButtonId();

        switch (checkedId) {
            case R.id.max_btn1:
                return Thread.MAX_PRIORITY;
            case R.id.normal_btn1:
                return Thread.NORM_PRIORITY;
            case R.id.min_btn1:
                return Thread.MIN_PRIORITY;
            default:
                return -1;
        }
    }

    public int getSecondThreadPriority() {
        int checkedId = radioGroup2.getCheckedRadioButtonId();

        switch (checkedId) {
            case R.id.max_btn2:
                return Thread.MAX_PRIORITY;
            case R.id.normal_btn2:
                return Thread.NORM_PRIORITY;
            case R.id.min_btn2:
                return Thread.MIN_PRIORITY;
            default:
                return -1;
        }
    }


    @Override
    protected void onDestroy() {
        updateHandler.removeCallbacks(updateRunnable);
        if (firstThreadHandler != null) firstThreadHandler.interrupt();
        if (secondThreadHandler != null) secondThreadHandler.interrupt();
        busyThreadsHandler.destroy();
        super.onDestroy();
    }
}
