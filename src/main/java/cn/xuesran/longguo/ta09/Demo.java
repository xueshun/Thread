package cn.xuesran.longguo.ta09;

/**
 * @Author xueshun
 * @Create 2018-03-16 17:19
 */
public class Demo {

    private ThreadLocal<Integer> count = new ThreadLocal<Integer>() {
        /**
         * 初始化Count的值
         * @return
         */
        @Override
        protected Integer initialValue() {
            return new Integer(0);
        }
    };

    public Integer getNext() {
        Integer value = count.get();
        value++;
        count.set(value);
        return value;
    }

    public static void main(String[] args) {
        Demo d = new Demo();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(Thread.currentThread().getName() + " " + d.getNext());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(Thread.currentThread().getName() + " " + d.getNext());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
