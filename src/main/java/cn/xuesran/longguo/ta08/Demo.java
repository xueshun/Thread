package cn.xuesran.longguo.ta08;

/**
 * 线程Join
 *
 * @Author xueshun
 * @Create 2018-03-16 16:13
 */
public class Demo {
    public void a(Thread joinThread) {
        System.out.println("方法a执行了....");
        joinThread.start();
        try {
            joinThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("方法a执行完毕....");
    }

    public void b() {
        System.out.println("加塞线程开始执行了....");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("加塞线程执行完毕....");
    }

    public static void main(String[] args) {
        Demo d = new Demo();
        Thread joinThread = new Thread(new Runnable() {
            @Override
            public void run() {
                d.b();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                d.a(joinThread);
            }
        }).start();
    }
}
