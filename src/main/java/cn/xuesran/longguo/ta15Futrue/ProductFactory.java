package cn.xuesran.longguo.ta15Futrue;

import java.util.Random;

/**
 * @Author xueshun
 * @Create 2018-03-19 13:52
 */
public class ProductFactory {

    public Future createProduct(String name) {
        Future f = new Future();
        System.out.println("下单成功了，您可以去做其他的事情了....");

        //创建一个线程去制作蛋糕
        new Thread(new Runnable() {
            @Override
            public void run() {
                Product p = new Product(new Random().nextInt(), name);
                f.setProduct(p);
            }
        }).start();
        return f;
    }
}
