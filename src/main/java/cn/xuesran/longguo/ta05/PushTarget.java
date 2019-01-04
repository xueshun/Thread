package cn.xuesran.longguo.ta05;

/**
 * @Author xueshun
 * @Create 2018-03-15 16:51
 */
public class PushTarget implements Runnable {

    private Tmall tmall;

    public PushTarget(Tmall tmall) {
        this.tmall = tmall;
    }

    @Override
    public void run() {
        while (true) {
            tmall.push();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
