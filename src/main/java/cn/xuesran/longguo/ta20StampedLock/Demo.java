package cn.xuesran.longguo.ta20StampedLock;

import java.util.concurrent.locks.StampedLock;

/**
 * @Author xueshun
 * @Create 2018-03-20 20:05
 */
public class Demo {

    private int balance;

    private StampedLock lock = new StampedLock();


    public void conditionReadWrite(int value) {

        //首先要判断balance的值是否符合更新的条件
        long stamp = lock.readLock();
        while (balance > 0) {
            try {
                long writeStamp = lock.tryConvertToWriteLock(stamp);
                if (writeStamp != 0) {
                    stamp = writeStamp;
                    balance += value;
                    break;
                } else {
                    //没有转成写锁，这里需要首先释放读锁，然后再拿到读锁
                    lock.unlockRead(stamp);
                    //获取写锁
                    stamp = lock.writeLock();
                }
            } finally {
                lock.unlock(stamp);
            }
        }
    }

    /**
     * 获取乐观锁的操作
     */
    public void optimisticRead() {
        long stamp = lock.tryOptimisticRead();
        int c = balance;
        //如果这里出现了写操作，一次要判断
        if (lock.validate(stamp)) {
            //要重新读取
            long readStamp = lock.readLock();
            c = balance;
            stamp = readStamp;
        }
        lock.unlockRead(stamp);
    }

    /**
     * 读操作
     */
    public void read() {
        long stamp = lock.readLock();
        lock.tryReadLock();
        //lock.tryOptimisticRead();
        int c = balance;
        //做一些其他的操作
        lock.unlockRead(stamp);
    }

    /**
     * 写操作
     *
     * @param value
     */
    public void write(int value) {
        long stamp = lock.writeLock();
        balance += value;
        lock.unlockWrite(stamp);
    }
}
