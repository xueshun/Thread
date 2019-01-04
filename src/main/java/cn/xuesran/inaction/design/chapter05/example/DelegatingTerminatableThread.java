package cn.xuesran.inaction.design.chapter05.example;

import cn.xuesran.inaction.design.chapter05.AbstractTerminatableThread;

public class DelegatingTerminatableThread extends AbstractTerminatableThread {
    private final Runnable task;

    public DelegatingTerminatableThread(Runnable task) {
        this.task = task;
    }

    @Override
    protected void doRun() throws Exception {
        this.task.run();
    }

    public static AbstractTerminatableThread of(Runnable task) {
        DelegatingTerminatableThread ret = new DelegatingTerminatableThread(
                task);
        return ret;
    }
}