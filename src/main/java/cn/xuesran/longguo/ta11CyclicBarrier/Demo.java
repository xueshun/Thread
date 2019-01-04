package cn.xuesran.longguo.ta11CyclicBarrier;

import java.util.concurrent.CyclicBarrier;

/**
 * @Author xueshun
 * @Create 2018-03-16 20:45
 */
public class Demo {

    public void meeting(CyclicBarrier barrier) {
        System.out.println(Thread.currentThread().getName() + "到达会议室，等待开会...");

        //当有一个线程发生异常，所有线程都会等待状态
        if (Thread.currentThread().getName().equals("Thread-1")) {
            throw new RuntimeException();
        }

        if (Thread.currentThread().getName().equals("Thread-7")) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                barrier.reset();
            }
        }

        try {
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Demo d = new Demo();

        CyclicBarrier barrier = new CyclicBarrier(10, new Runnable() {
            @Override
            public void run() {
                System.out.println("好，我们开会....");
            }
        });

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        d.meeting(barrier);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("等待线程数：" + barrier.getNumberWaiting());
                    System.out.println("is broken" + barrier.isBroken());
                }
            }
        }).start();
    }
}
