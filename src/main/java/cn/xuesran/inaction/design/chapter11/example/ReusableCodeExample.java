package cn.xuesran.inaction.design.chapter11.example;

import cn.xuesran.inaction.design.chapter11.AbstractSerializer;
import cn.xuesran.inaction.design.chapter11.TaskProcessor;
import cn.xuesran.inaction.design.util.Debug;
import cn.xuesran.inaction.design.util.Tools;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;


/**
 * 可重用代码示例
 *
 * @author xueshun
 */
public class ReusableCodeExample {

    public static void main(String[] args)
            throws InterruptedException, ExecutionException {
        SomeService ss = new SomeService();
        ss.init();
        Future<String> result = ss.doSomething("Serial Thread Confinement", 1);
        // 模拟执行其他操作
        Tools.randomPause(100, 50);

        Debug.info(result.get());

        ss.shutdown();

    }

    private static class Task {
        public final String message;
        public final int id;

        public Task(String message, int id) {
            this.message = message;
            this.id = id;
        }
    }

    static class SomeService extends AbstractSerializer<Task, String> {

        public SomeService() {
            super(
                    new ArrayBlockingQueue<Runnable>(100),
                    new TaskProcessor<Task, String>() {

                        @Override
                        public String doProcess(Task task) throws Exception {
                            Debug.info("[" + task.id + "]:" + task.message);
                            return task.message + " accepted.";
                        }

                    });
        }

        @Override
        protected Task makeTask(Object... params) {
            String message = (String) params[0];
            int id = (Integer) params[1];

            return new Task(message, id);
        }

        public Future<String> doSomething(String message, int id)
                throws InterruptedException {
            Future<String> result = null;
            result = service(message, id);
            return result;
        }

    }

}