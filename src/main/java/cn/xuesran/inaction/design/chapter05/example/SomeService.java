package cn.xuesran.inaction.design.chapter05.example;

import cn.xuesran.inaction.design.chapter05.AbstractTerminatableThread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * <pre>类名: SomeService</pre>
 * <pre>描述: 停止生产者 - 消费者问题中的线程例子</pre>
 * <pre>日期: 2018/12/29 16:31</pre>
 * <pre>作者: xueshun</pre>
 */
public class SomeService {
    private final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(100);

    private final Producer producer = new Producer();
    private final Consumer consumer = new Consumer();

    /**
     * 生产者
     */
    private class Producer extends AbstractTerminatableThread {
        private int i = 0;

        @Override
        protected void doRun() throws Exception {
            queue.put(String.valueOf(i++));
            consumer.terminationToken.reservations.incrementAndGet();
            System.out.println(i);
        }
    }

    /**
     * 消费者
     */
    private class Consumer extends AbstractTerminatableThread {

        @Override
        protected void doRun() throws Exception {
            String product = queue.take();
            System.out.println("Processing product:" + product);
            try {
                Thread.sleep(new Random().nextInt(100));
            } catch (InterruptedException e) {
                ;
            } finally {
                terminationToken.reservations.decrementAndGet();
            }
        }
    }

    public void shutdown() {
        // 生产者线程停止在停止消费者线程
        producer.terminate(true);
        consumer.terminate();
    }


    public void init() {
        producer.start();
        consumer.start();
    }

    public static void main(String[] args) throws InterruptedException {
        SomeService ss = new SomeService();
        ss.init();
        Thread.sleep(500);
        ss.shutdown();
    }
}
