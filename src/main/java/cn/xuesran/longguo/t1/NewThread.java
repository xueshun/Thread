package cn.xuesran.longguo.t1;

public class NewThread implements Runnable {

    @Override
    public synchronized void run() {
        while (true) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("自定义线程执行了.......");
        }
    }

    public static void main(String[] args) {
        NewThread n = new NewThread();

        Thread thread = new Thread(n);

        thread.start();
        while (true) {
            synchronized (n) {
                System.out.println("主线程执行了");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                n.notifyAll();
            }
        }
    }
}

