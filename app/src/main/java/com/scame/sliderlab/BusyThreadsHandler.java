package com.scame.sliderlab;


import java.util.ArrayList;
import java.util.List;

public class BusyThreadsHandler {

    private final List<Thread> threadList;

    private int threadsNumber;

    public BusyThreadsHandler(int threadsNumber) {
        this.threadsNumber = threadsNumber;
        this.threadList = new ArrayList<>(threadsNumber);
    }

    void activate() {
        for (int i = 0; i < threadsNumber; i++) {
            threadList.add(new Thread(() -> {
                while (!Thread.interrupted()) { }
            }));
        }

        for (Thread thread : threadList) {
            thread.start();
        }
    }

    void destroy() {
        for (Thread thread : threadList) {
            thread.interrupt();
        }
        threadList.clear();
    }
}
