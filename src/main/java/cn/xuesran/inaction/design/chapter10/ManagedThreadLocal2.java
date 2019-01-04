package cn.xuesran.inaction.design.chapter10;

import java.lang.ref.WeakReference;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ManagedThreadLocal2<T> extends ThreadLocal<T> {

    private static volatile Queue<WeakReference<ManagedThreadLocal2<?>>> instance = new ConcurrentLinkedQueue<WeakReference<ManagedThreadLocal2<?>>>();
    private volatile ThreadLocal<T> threadLocal;

    public ManagedThreadLocal2(final InitialValueProvider<T> ivp) {
        this.threadLocal = new ThreadLocal<T>() {
            @Override
            protected T initialValue() {
                return ivp.initialValue();
            }
        };
    }

    public static <T> ManagedThreadLocal2<T> newInstance(final InitialValueProvider<T> ivp) {
        ManagedThreadLocal2<T> mtl = new ManagedThreadLocal2<T>(ivp);

        //使用弱引用来引用ThreadLocalProxy实例，防止内存泄漏
        instance.add(new WeakReference<ManagedThreadLocal2<?>>(mtl));
        return mtl;
    }

    public static <T> ManagedThreadLocal2<T> newInstance() {
        return newInstance(new InitialValueProvider<T>());
    }

    @Override
    public T get() {
        return threadLocal.get();
    }

    @Override
    public void set(T value) {
        threadLocal.set(value);
    }

    @Override
    public void remove() {
        if (null != threadLocal) {
            threadLocal.remove();
            threadLocal = null;
        }
    }

    public static void removeAll() {
        WeakReference<ManagedThreadLocal2<?>> wrMtl;
        ManagedThreadLocal2<?> mtl;
        while (null != (wrMtl = instance.poll())) {
            mtl = wrMtl.get();
            if (null != mtl) {
                mtl.remove();
            }
        }
    }

    public static class InitialValueProvider<T> {
        protected T initialValue() {
            return null;
        }
    }
}
