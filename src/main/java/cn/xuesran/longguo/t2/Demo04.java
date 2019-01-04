package cn.xuesran.longguo.t2;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Demo04 implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("正在紧张的计算。。。。。");
        Thread.sleep(1000);
        return 1;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Demo04 d = new Demo04();

        FutureTask<Integer> task = new FutureTask<Integer>(d);

        Thread t = new Thread(task);

        t.start();

        Integer result = task.get();

        System.out.println("线程执行结果为：" + result);
    }
}
