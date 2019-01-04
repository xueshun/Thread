package cn.xuesran.longguo.ta04;

/**
 * @Author xueshun
 * @Create 2018-03-15 16:01
 */
public class Target2 implements Runnable {
    private Demo03 demo;

    public Target2(Demo03 demo) {
        this.demo = demo;
    }

    @Override
    public void run() {
        demo.getSignal();
    }
}
