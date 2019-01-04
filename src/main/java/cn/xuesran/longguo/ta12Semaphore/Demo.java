package cn.xuesran.longguo.ta12Semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @Author xueshun
 * @Create 2018-03-17 14:13
 */
public class Demo {

    public void method(Semaphore semaphore) {

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " is run ...");

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        semaphore.release();
    }

    public static void main(String[] args) {
        Demo d = new Demo();
        Semaphore semaphore = new Semaphore(10);

        while (true) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    d.method(semaphore);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
