package cn.xuesran.inaction.design.chapter05.example;

import cn.xuesran.inaction.design.chapter04.AlarmAgent;
import cn.xuesran.inaction.design.chapter04.AlarmInfo;
import cn.xuesran.inaction.design.chapter05.AbstractTerminatableThread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AlarmSendingThread extends AbstractTerminatableThread {

    private final AlarmAgent alarmAgent = new AlarmAgent();

    //告警队列
    private final BlockingQueue<AlarmInfo> alarmQueue;
    private final ConcurrentMap<String, AtomicInteger> submittedAlarmRegistry;

    public AlarmSendingThread() {
        alarmQueue = new ArrayBlockingQueue<AlarmInfo>(100);
        submittedAlarmRegistry = new ConcurrentHashMap<String, AtomicInteger>();
        alarmAgent.init();
    }


    @Override
    protected void doRun() throws Exception {
        AlarmInfo alarm;
        alarm = alarmQueue.take();
        terminationToken.reservations.decrementAndGet();

        try {
            alarmAgent.sendAlarm(alarm);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (AlarmType.RESUME == alarm.getType()) {
            String key = AlarmType.FAULT.toString() + ":" + alarm.getId() + "@" + alarm.getExtraInfo();
            submittedAlarmRegistry.remove(key);
        }
    }

    public int sendAlarm(final AlarmInfo alarmInfo) {
        AlarmType type = alarmInfo.getType();
        String id = alarmInfo.getId();
        String extraInfo = alarmInfo.getExtraInfo();

        if (terminationToken.isToShutdown()) {
            //记录警告
            System.err.println("rejected alarm:" + id + "," + extraInfo);
            return -1;
        }
        int duplicateSubmissionCount = 0;
        try {
            AtomicInteger prevSubmittedCounter;
            prevSubmittedCounter = submittedAlarmRegistry.putIfAbsent(type.toString() + ":" + "@" + extraInfo, new AtomicInteger(0));
            if (null == prevSubmittedCounter) {
                terminationToken.reservations.incrementAndGet();
                alarmQueue.put(alarmInfo);
            } else {
                duplicateSubmissionCount = prevSubmittedCounter.incrementAndGet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return duplicateSubmissionCount;
    }

    @Override
    protected void doCleanUp(Exception cause) {
        if (null != cause && !(cause instanceof InterruptedException)) {
            cause.printStackTrace();
        }
        alarmAgent.disconnected();
    }
}
