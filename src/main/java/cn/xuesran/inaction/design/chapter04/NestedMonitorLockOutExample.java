package cn.xuesran.inaction.design.chapter04;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class NestedMonitorLockOutExample {

    public static void main(String[] args) {
        final Helper helper = new Helper();

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                String result;
                result = helper.xGuarededMethod("test");
            }

        });
        t.start();

        final Timer timer = new Timer();

        // 延迟50ms调用helper.stateChanged方法
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                helper.xStateChanged();
                timer.cancel();
            }

        }, 50, 10);

    }

    private static class Helper {
        private volatile boolean isStateOK = false;
        private final Predicate stateBeOK = new Predicate() {

            @Override
            public boolean evaluate() {
                return isStateOK;
            }

        };

        private final Blocker blocker = new ConditionVarBlocker();

        public synchronized String xGuarededMethod(final String message) {
            GuardedAction<String> ga = new GuardedAction<String>(stateBeOK) {

                @Override
                public String call() throws Exception {
                    return message + "->received.";
                }

            };
            String result = null;
            try {
                result = blocker.callWithGurad(ga);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        public synchronized void xStateChanged() {
            try {
                blocker.signalAfter(new Callable<Boolean>() {

                    @Override
                    public Boolean call() throws Exception {
                        isStateOK = true;
                        return Boolean.TRUE;
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
