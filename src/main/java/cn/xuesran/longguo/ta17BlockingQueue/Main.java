package cn.xuesran.longguo.ta17BlockingQueue;

/**
 * @Author xueshun
 * @Create 2018-03-15 16:51
 */
public class Main {
    public static void main(String[] args) {
        Tmall tmall = new Tmall();

        PushTarget p = new PushTarget(tmall);
        TakeTarget t = new TakeTarget(tmall);

        new Thread(p).start();
        new Thread(p).start();
        new Thread(p).start();
        new Thread(p).start();

        new Thread(t).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                tmall.size();
            }
        }).start();
    }
}
