package cn.xuesran.longguo.t2;

/**
 * 作为线程任务存在
 */
public class Demo02 implements Runnable {
    @Override
    public void run() {
        int i = 0;
        while (true) {
            System.out.println("thread runing..." + i);
            i++;
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new Demo02());
        thread.start();
    }
}
