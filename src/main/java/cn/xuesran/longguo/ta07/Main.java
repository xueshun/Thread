package cn.xuesran.longguo.ta07;

/**
 * @Author xueshun
 * @Create 2018-03-16 14:05
 */
public class Main {

    public static void main(String[] args) {
        MyQueue queue = new MyQueue(5);
        Add a = new Add(queue);
        Remove r = new Remove(queue);
        new Thread(a).start();
        new Thread(r).start();
    }
}

/**
 *
 */
class Add implements Runnable {

    private MyQueue queue;

    public Add(MyQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            queue.add(i);
        }
    }
}

class Remove implements Runnable {
    private MyQueue queue;

    public Remove(MyQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            queue.remove();
        }
    }
}
