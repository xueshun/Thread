package cn.xuesran.inaction.design.chapter09.example;


import java.util.concurrent.*;

/**
 * <pre>类名: ThreadPoolDeadLockAvoidance</pre>
 * <pre>描述: TODO</pre>
 * <pre>日期: 2019/1/1 13:30</pre>
 * <pre>作者: xueshun</pre>
 */
public class ThreadPoolDeadLockAvoidance {

    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            1,
            1,
            60,
            TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    public static void main(String[] args) {
        ThreadPoolDeadLockAvoidance me = new ThreadPoolDeadLockAvoidance();
        me.test("<This will not deadlock>");
    }

    public void test(final String message) {
        Runnable taskA = new Runnable() {
            public void run() {
                System.out.println("Executing TaskA .....");

                Runnable taskB = new Runnable() {
                    public void run() {
                        System.out.println("TaskB processes " + message);
                    }
                };
                Future<?> result = threadPool.submit(taskB);

                try {
                    // 等待TaskB 执行结束才能执行TaskA,是的TaskA和TaskB成为有依赖关系的两个任务
                    result.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                System.out.println("TaskA Done.");
            }
        };
        threadPool.submit(taskA);
    }
}
