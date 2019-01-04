package cn.xuesran.longguo.ta06;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author xueshun
 * @Create 2018-03-15 20:06
 */
public class Demo02 {

    private int signal;
    Lock lock = new ReentrantLock();

    //有几个线程创建几个Condition

    Condition a = lock.newCondition();
    Condition b = lock.newCondition();
    Condition c = lock.newCondition();

    public void a() {
        lock.lock();
        while (signal != 0) {
            try {
                a.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("a");
        signal++;
        b.signal();
        lock.unlock();
    }

    public void b() {
        lock.lock();
        while (signal != 1) {
            try {
                b.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("b");
        signal++;
        c.signal();
        lock.unlock();
    }

    public void c() {
        lock.lock();
        while (signal != 2) {
            try {
                c.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("c");
        signal = 0;
        a.signal();
        lock.unlock();
    }

    public static void main(String[] args) {
        Demo02 d = new Demo02();

        E e = new E(d);
        F f = new F(d);
        G g = new G(d);

        new Thread(e).start();
        new Thread(f).start();
        new Thread(g).start();

    }
}

class E implements Runnable {

    private Demo02 demo;

    public E(Demo02 demo) {
        this.demo = demo;
    }

    @Override
    public void run() {
        while (true) {
            try {
                demo.a();
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class F implements Runnable {

    private Demo02 demo;

    public F(Demo02 demo) {
        this.demo = demo;
    }

    @Override
    public void run() {
        while (true) {
            try {
                demo.b();
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class G implements Runnable {

    private Demo02 demo;

    public G(Demo02 demo) {
        this.demo = demo;
    }

    @Override
    public void run() {
        while (true) {
            demo.c();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
