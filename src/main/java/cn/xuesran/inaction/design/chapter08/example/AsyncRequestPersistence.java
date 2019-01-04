package cn.xuesran.inaction.design.chapter08.example;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

/**
 * <pre>类名: AsyncRequestPersistence</pre>
 * <pre>描述: 模式角色：ActiveObject.Proxy</pre>
 * <pre>日期: 2018/12/31 16:16</pre>
 * <pre>作者: xueshun</pre>
 */
public class AsyncRequestPersistence implements RequestPersistence {
    private static final long ONE_MINUTE_IN_SECONDS = 60;
    final static Logger logger = Logger.getLogger(AsyncRequestPersistence.class);

    final AtomicLong taskTimeConsumedPerInterval = new AtomicLong(0);
    final AtomicInteger requestSubmittedPerInterval = new AtomicInteger(0);

    /**
     * 模式角色：ActiveObject.Servant
     */
    private final DiskbasedRequestPersistence delegate = new DiskbasedRequestPersistence();

    /**
     * 模式角色：ActiveObject.Scheduler
     */
    private final ThreadPoolExecutor scheduler;


    /**
     * 用于保存AsyncRequestPersistence的唯一实例
     */
    private static class InstanceHolder {
        final static RequestPersistence INSTANCE = new AsyncRequestPersistence();
    }

    /**
     * 私有构造器
     */
    public AsyncRequestPersistence() {
        scheduler = new ThreadPoolExecutor(
                1,
                3,
                60 * ONE_MINUTE_IN_SECONDS,
                TimeUnit.SECONDS,
                // 模式角色：ActiveObject.ActivationQueue
                new ArrayBlockingQueue<Runnable>(200),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t;
                        t = new Thread(r, "AsyncRequestPersistence");
                        return t;
                    }

                });
        // 拒绝策略
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 启动队列监控定时任务
        Timer monitorTimer = new Timer(true);
        monitorTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (logger.isInfoEnabled()) {
                    logger.info("task count:" + requestSubmittedPerInterval
                            + ",Queue size:" + scheduler.getQueue().size()
                            + ",taskTimeConsumedPerInterval:"
                            + taskTimeConsumedPerInterval.get() + " ms");
                }

                taskTimeConsumedPerInterval.set(0);
                requestSubmittedPerInterval.set(0);
            }
        }, 0, ONE_MINUTE_IN_SECONDS * 1000);
    }

    /**
     * 获取类AsyncRequestPersistence的唯一实例
     */
    public static RequestPersistence getInstance() {
        return InstanceHolder.INSTANCE;
    }


    @Override
    public void store(MMSDeliverRequest request) {
        Callable<Boolean> methodRequest = () -> {
            long start = System.currentTimeMillis();
            try {
                delegate.store(request);
            } finally {
                taskTimeConsumedPerInterval.addAndGet(System.currentTimeMillis() - start);
            }
            return Boolean.TRUE;
        };
        scheduler.submit(methodRequest);
        requestSubmittedPerInterval.incrementAndGet();
    }


    @Override
    public void close() throws IOException {
        scheduler.shutdown();
    }


}
