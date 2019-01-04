package cn.xuesran.inaction.design.chapter10.example;

import cn.xuesran.inaction.design.chapter10.ManagedThreadLocal;
import cn.xuesran.inaction.design.util.Debug;

public class ManagedThreadLocalClient {
    final static ManagedThreadLocal<Long> mtl;

    static {
        mtl = new ManagedThreadLocal<Long>() {
            @Override
            protected Long initialValue() {
                Debug.info(Thread.currentThread().getName());
                return Thread.currentThread().getId();
            }

        };
    }

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Helper().start();
        }

    }

    static class Helper extends Thread {

        @Override
        public void run() {

            Debug.info("Thread Id:%d", mtl.get());
        }

    }

}