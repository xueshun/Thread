package cn.xuesran.longguo.lambda;

/**
 * 使用lambda来实现Runnable接口
 */
public class Demo02 {
    public static void main(String[] args) {
        // 使用匿名内部类
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello world");
            }
        }).start();

        //使用lambda expression
        new Thread(() -> System.out.println("Lambda Hello world")).start();

        Runnable race1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("hello world!");
            }
        };

        Runnable race2 = () -> System.out.println("hello world!!");

        race1.run();
        race2.run();
    }
}
