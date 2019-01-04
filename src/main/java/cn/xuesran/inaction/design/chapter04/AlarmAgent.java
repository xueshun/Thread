package cn.xuesran.inaction.design.chapter04;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class AlarmAgent {

    /**
     * 用户记录AlarmAgent是否连接上告警服务器
     */
    private volatile boolean connectedToServer = false;

    private final Predicate agentConnected = new Predicate() {
        @Override
        public boolean evaluate() {
            return connectedToServer;
        }
    };

    /**
     * 模式角色：GuarededSuspension.Blocker
     */
    private final Blocker blocker = new ConditionVarBlocker();

    /**
     * 心跳定时器
     */
    private final Timer heartbearTime = new Timer(true);


    /**
     * 发送信息
     *
     * @param alarm
     * @throws Exception
     */
    public void sendAlarm(final AlarmInfo alarm) throws Exception {
        GuardedAction<Void> guardedAction = new GuardedAction<Void>(agentConnected) {
            @Override
            public Void call() throws Exception {
                doSendAlarm(alarm);
                return null;
            }
        };
    }

    /**
     * 发送信息
     *
     * @param alarm
     */
    private void doSendAlarm(AlarmInfo alarm) {

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建连接
     */
    protected void onConnected() {
        try {
            blocker.signalAfter(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    connectedToServer = true;
                    return Boolean.TRUE;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnected() {
        this.onDisconnected();
    }

    /**
     * 断开连接
     */
    protected void onDisconnected() {
        connectedToServer = false;
    }

    /**
     * 负责与告警服务器建立网络连接
     */
    private class ConnectingTask implements Runnable {

        @Override
        public void run() {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 心跳检测任务
     */
    private class HeartbeatTask extends TimerTask {

        @Override
        public void run() {
            if (!testConnection()) {
                reconnect();
            }
        }

        private boolean testConnection() {

            return true;
        }

        private void reconnect() {
            ConnectingTask connectingTask = new ConnectingTask();

            connectingTask.run();
        }
    }

    /**
     * 初始化
     */
    public void init() {
        Thread connectingThread = new Thread(new ConnectingTask());

        connectingThread.start();

        heartbearTime.schedule(new HeartbeatTask(), 60000, 2000);
    }
}
