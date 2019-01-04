package cn.xuesran.inaction.design.chapter12;

import java.util.concurrent.Callable;

/**
 * 重试信息
 *
 * @param <T> 子任务类型
 * @param <V> 子任务处理结果类型
 * @author xueshun
 */
public class RetryInfo<T, V> {
    public final T subTask;
    public final Callable<V> redoCommand;

    public RetryInfo(T subTask, Callable<V> redoCommand) {
        this.subTask = subTask;
        this.redoCommand = redoCommand;
    }
}