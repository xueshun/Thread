package cn.xuesran.longguo.ta04;

import java.util.concurrent.TimeUnit;

/**
 * @Author xueshun
 * @Create 2018-03-15 15:23
 * 线程之间的通信
 * (模拟)
 */
public class Demo01 {

    private volatile int signal;

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public static void main(String[] args) {
        Demo01 d = new Demo01();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("修改状态的线程执行.....");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                d.setSignal(1);
                System.out.println("修改至修改成功.....");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (d.getSignal() != 1) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //当信号量为1的时候，执行代码
                System.out.println("模拟代码的执行完毕......");
            }
        }).start();
    }
}
