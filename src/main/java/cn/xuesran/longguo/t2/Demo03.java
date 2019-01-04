package cn.xuesran.longguo.t2;

/**
 * 匿名内部类创建线程
 */
public class Demo03 {
    public static void main(String[] args) {

        // extend Thread 方式
        /* new Thread(){
            @Override
            public void run() {
                System.out.println("Thread start");
            }
        }.start();*/

        // 实现Runnable 方式
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread start");
            }
        }){}.start();*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("runnable");
            }
        }) {
            @Override
            public void run() {
                //这个会执行时因为 Thread 类中的run() target ==null
                System.out.println("thread");
            }
        }.start();
    }
}
