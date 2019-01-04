package cn.xuesran.longguo.ta04;

import java.util.concurrent.TimeUnit;

/**
 * @Author xueshun
 * @Create 2018-03-15 15:39
 */
public class Demo03 {
    private volatile int signal;


    public synchronized void setSignal() {
        signal = 1;
        //notify(); //随机唤醒一个处于wait状态的线程
        notifyAll();//唤醒所有处于wait线程，争夺时间片的线程只有一个
        this.signal = signal;
    }

    public synchronized int getSignal() {
        System.out.println(Thread.currentThread().getName() + "方法执行了...");
        if (signal != 1) {
            try {
                wait();//会释放当前的线程获取的锁
                System.out.println("叫醒之后....");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + "方法执行完毕...");
        return signal;
    }

    public static void main(String[] args) {
        Demo03 d = new Demo03();
        Target1 t1 = new Target1(d);
        Target2 t2 = new Target2(d);

        new Thread(t2).start();
        new Thread(t2).start();
        new Thread(t2).start();
        new Thread(t2).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(t1).start();

    }
}
