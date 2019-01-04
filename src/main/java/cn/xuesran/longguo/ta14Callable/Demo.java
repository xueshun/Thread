package cn.xuesran.longguo.ta14Callable;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Callable 和 Runnable的区别
 * Runnable run方法是被线程调用的，在run方法是一部执行的
 * <p>
 * Callable的call方法，不是异步执行的是由Future的run方法调用的
 *
 * @Author xueshun
 * @Create 2018-03-19 13:32
 */
public class Demo {

    public static void main(String[] args) {
        Callable<Integer> call = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("正在计算结果....");
                TimeUnit.SECONDS.sleep(1);
                return 1;
            }
        };

        FutureTask<Integer> task = new FutureTask<Integer>(call);

        Thread thread = new Thread(task);
        thread.start();

        //do something

        System.out.println("do something");

        try {
            Integer integer = task.get();
            System.out.println("拿到的结果为：" + integer);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
