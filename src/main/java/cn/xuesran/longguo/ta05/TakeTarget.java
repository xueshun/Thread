package cn.xuesran.longguo.ta05;

import java.util.concurrent.TimeUnit;

/**
 * @Author xueshun
 * @Create 2018-03-15 16:52
 */
public class TakeTarget implements Runnable {

    private Tmall tmall;

    public TakeTarget(Tmall tmall) {
        this.tmall = tmall;
    }

    @Override
    public void run() {
        while (true) {
            tmall.take();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
