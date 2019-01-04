package cn.xuesran.longguo.ta04;

import java.util.concurrent.TimeUnit;

/**
 * @Author xueshun
 * @Create 2018-03-15 15:32
 * wait notify 的使用必须在同步代码块中,
 * 否则会报错
 */
public class Demo02 {
    private volatile int signal;

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public static void main(String[] args) {
        Demo02 d = new Demo02();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (d) {
                    System.out.println("修改状态的线程执行.....");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    d.setSignal(1);
                    System.out.println("状态值修改成功.....");
                    d.notify();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (d) {
                    //等待signal为1开始执行，否则处于等待状态
                    while (d.getSignal() != 1) {
                        try {
                            d.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("模拟代码执行完毕.....");
                }
            }
        }).start();
    }


}
