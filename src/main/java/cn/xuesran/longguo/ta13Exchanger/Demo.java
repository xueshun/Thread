package cn.xuesran.longguo.ta13Exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * @Author xueshun
 * @Create 2018-03-17 15:08
 */
public class Demo {

    public void a(Exchanger<String> exchanger) {
        System.out.println("a 方法执行.....");

        try {
            System.out.println("a 线程正在抓取数据....");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("a 线程抓取到数据....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String res = "123456";

        try {
            System.out.println();
            exchanger.exchange(res);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void b(Exchanger<String> exchanger) {
        System.out.println("b 方法开始执行....");

        try {
            System.out.println("b 方法开始抓取数据....");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("b 方法抓取数据结束....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String res = "123456";

        try {
            String value = exchanger.exchange(res);
            System.out.println("开始进行对比....");
            System.out.println("比对结果为：" + value.equals(res));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Demo d = new Demo();
        Exchanger<String> exchanger = new Exchanger<String>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                d.a(exchanger);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                d.b(exchanger);
            }
        }).start();
    }
}
