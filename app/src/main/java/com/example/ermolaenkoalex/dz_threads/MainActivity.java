package com.example.ermolaenkoalex.dz_threads;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class MainActivity extends AppCompatActivity {

    private final static String RIGHT_LEG = "Right Leg ";
    private final static String LEFT_LEG = "Left Leg ";

    private LeftLeg leftLeg;
    private RightLeg rightLeg;
    final Semaphore semaphore = new Semaphore(1, true);
    final CountDownLatch latch = new CountDownLatch(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView textField = findViewById(R.id.tv_text);

        leftLeg = new LeftLeg(semaphore, latch, textField, LEFT_LEG);
        rightLeg = new RightLeg(semaphore, latch, textField, RIGHT_LEG);
        new Thread(leftLeg).start();
        new Thread(rightLeg).start();
    }

    @Override
    protected void onStop() {
        leftLeg.stopRun();
        rightLeg.stopRun();

        super.onStop();
    }

    private static class LeftLeg extends LegRunnable {
        LeftLeg(Semaphore semaphore, CountDownLatch latch, TextView textField, String text) {
            super(semaphore, latch, textField, text);
        }

        @Override
        public void run() {

            while (isRunning) {
                try {
                    semaphore.acquire();
                    waitOther();
                    print();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        }
    }

    private static class RightLeg extends LegRunnable {

        RightLeg(Semaphore semaphore, CountDownLatch latch, TextView textField, String text) {
            super(semaphore, latch, textField, text);
        }

        @Override
        public void run() {

            while (isRunning) {
                try {
                    waitOther();
                    semaphore.acquire();
                    print();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        }
    }
}
