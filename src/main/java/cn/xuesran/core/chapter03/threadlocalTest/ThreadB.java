package cn.xuesran.core.chapter03.threadlocalTest;

public class ThreadB extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Tools.t1.set("ThreadB" + (i + 1));
                System.out.println("ThreadB get Value = " + Tools.t1.get());
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
