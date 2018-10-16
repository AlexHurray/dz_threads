package com.example.ermolaenkoalex.dz_threads;

import android.support.annotation.NonNull;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public abstract class LegRunnable implements Runnable {

    private boolean firstStep = true;
    private CountDownLatch latch;
    private WeakReference<TextView> textField;
    private String text;

    boolean isRunning = true;
    Semaphore semaphore;

    LegRunnable(@NonNull Semaphore semaphore, @NonNull CountDownLatch latch, @NonNull TextView textField, @NonNull String text) {
        this.semaphore = semaphore;
        this.latch = latch;
        this.textField = new WeakReference<>(textField);
        this.text = text;
    }

    void stopRun() {
        isRunning = false;
    }

    void print() throws InterruptedException {
        Thread.sleep(1000); // For usability
        System.out.println(text + Thread.currentThread());

        final TextView tv = textField.get();
        if (tv != null) {
            tv.post(new Runnable() {
                @Override
                public void run() {
                    tv.setText(text);
                }
            });
        }
    }

    void waitOther() throws InterruptedException {
        if (firstStep) {
            firstStep = false;
            latch.countDown();
            latch.await();
        }
    }
}