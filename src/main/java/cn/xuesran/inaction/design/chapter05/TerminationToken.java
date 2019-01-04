package cn.xuesran.inaction.design.chapter05;

import java.lang.ref.WeakReference;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>类名: TerminationToken</pre>
 * <pre>描述: 线程停止标志</pre>
 * <pre>日期: 2018/12/29 11:12</pre>
 * <pre>作者: xueshun</pre>
 */
public class TerminationToken {

    /**
     * 使用volitile修饰，以保证无需显示锁的情况下
     */
    protected volatile boolean toShutdown = false;
    public final AtomicInteger reservations = new AtomicInteger(0);

    /**
     * 在多个可停止线程实例共享一个TerminationToken实例的情况下，该队列用于记录那些
     * 共享TerminationToken实例的可停止线程，以便尽可能减少锁的使用的情况，实现这些线程的停止
     */
    private final Queue<WeakReference<Terminatable>> coordinatedThreads;

    public TerminationToken() {
        this.coordinatedThreads = new ConcurrentLinkedQueue<WeakReference<Terminatable>>();
    }

    /**
     * 获取toShutdown
     *
     * @return toShutdown
     */
    public boolean isToShutdown() {
        return toShutdown;
    }

    /**
     * 设置toShutdown
     *
     * @param toShutdown toShutdown
     */
    public void setToShutdown(boolean toShutdown) {
        this.toShutdown = toShutdown;
    }

    protected void register(Terminatable thread) {
        coordinatedThreads.add(new WeakReference<>(thread));
    }

    /**
     * 通知TerminationToken实例：共享该实例的所有可停止线程中的一个线程，停止了，
     * 以便其停止其他未被停止的线程
     *
     * @param thread 已停止的线程
     */
    protected void notifyThreadTermination(Terminatable thread) {
        WeakReference<Terminatable> wrThread;
        Terminatable otherThread;
        while (null != (wrThread = coordinatedThreads.poll())) {
            otherThread = wrThread.get();
            if (null != otherThread && otherThread != thread) {
                otherThread.terminate();
            }
        }
    }
}
