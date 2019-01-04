package cn.xuesran.longguo.ta04;

/**
 * @Author xueshun
 * @Create 2018-03-15 15:39
 */
public class Target1 implements Runnable {

    private Demo03 demo;

    public Target1(Demo03 demo) {
        this.demo = demo;
    }

    @Override
    public void run() {
        demo.setSignal();
    }
}
