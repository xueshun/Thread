package cn.xuesran.inaction.design.chapter07;

import java.util.concurrent.BlockingQueue;

/**
 * <pre>类名: BlockingQueueChannel</pre>
 * <pre>描述: 基于阻塞队列的通道实现</pre>
 * <pre>日期: 2018/12/30 16:55</pre>
 * <pre>作者: xueshun</pre>
 */
public class BlockingQueueChannel<P> implements Channel<P> {

    private final BlockingQueue<P> queue;

    public BlockingQueueChannel(BlockingQueue<P> queue) {
        this.queue = queue;
    }

    @Override
    public P take() throws InterruptedException {
        return queue.take();
    }

    @Override
    public void put(P product) throws InterruptedException {
        queue.put(product);
    }
}
