package cn.xuesran.longguo.ta19Executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Executors
 *
 * @Author xueshun
 * @Create 2018-03-20 17:03
 */
public class Demo {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);

        while (true) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
