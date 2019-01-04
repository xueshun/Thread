package cn.xuesran.longguo.ta07;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用Condition实现一个有界队列
 * 先进先出
 *
 * @Author xueshun
 * @Create 2018-03-16 13:50
 */
public class MyQueue<E> {
    private Object[] obj;

    /**
     * 队列的添加坐标
     */
    private int addIndex;
    /**
     * 队列的移除坐标
     */
    private int removeIndex;
    /**
     * 队列的长度
     */
    private int queueSize;

    public MyQueue(int count) {
        obj = new Object[count];
    }

    private Lock lock = new ReentrantLock();
    Condition addConditon = lock.newCondition();
    Condition removeCondition = lock.newCondition();

    /**
     * 添加数据
     *
     * @param e
     */
    public void add(E e) {
        lock.lock();
        while (queueSize == obj.length) {
            try {
                System.out.println("该队列已经添加满了........");
                addConditon.await();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        obj[addIndex] = e;
        System.out.println("该队列成功添加元素》》》：" + e);
        if (++addIndex == obj.length) {
            addIndex = 0;
        }

        queueSize++;
        removeCondition.signal();
        lock.unlock();
    }

    /**
     * 移除数据
     */
    public void remove() {
        lock.lock();

        while (queueSize == 0) {
            try {
                System.out.println("该队列处于空的状态......");
                removeCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("成功移除元素:" + obj[removeIndex].toString());
        obj[removeIndex] = null;

        if (++removeIndex == obj.length) {
            removeIndex = 0;
        }

        queueSize--;
        addConditon.signal();
        lock.unlock();
    }
}
