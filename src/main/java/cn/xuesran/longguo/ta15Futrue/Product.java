package cn.xuesran.longguo.ta15Futrue;

import java.util.concurrent.TimeUnit;

/**
 * @Author xueshun
 * @Create 2018-03-19 13:52
 */
public class Product {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Product(int id, String name) {
        System.out.println("开始制作" + name);

        //制作的过程需要耗时
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.id = id;
        this.name = name;
        System.out.println(name + "制作完毕");
    }
}
