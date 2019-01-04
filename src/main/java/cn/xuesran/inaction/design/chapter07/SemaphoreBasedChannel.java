package cn.xuesran.inaction.design.chapter07;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * <pre>类名: SemaphoreBasedChannel</pre>
 * <pre>描述: 基于Semaphore的支持流量控制的通道实现</pre>
 * <pre>日期: 2018/12/30 16:58</pre>
 * <pre>作者: xueshun</pre>
 */
public class SemaphoreBasedChannel<P> implements Channel<P> {
    private final BlockingQueue<P> queue;
    private final Semaphore semaphore;

    /**
     * @param queue     阻塞队列，通常是一个无界阻塞队列
     * @param flowLimit 流量限制数
     */
    public SemaphoreBasedChannel(BlockingQueue<P> queue, int flowLimit) {
        this.queue = queue;
        this.semaphore = new Semaphore(flowLimit);
    }

    @Override
    public P take() throws InterruptedException {
        return queue.take();
    }

    @Override
    public void put(P product) throws InterruptedException {
        semaphore.acquire();
        try {
            queue.put(product);
        } catch (InterruptedException e) {
            semaphore.release();
        }
    }
}
