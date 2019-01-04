package cn.xuesran.longguo.ta15Futrue;

/**
 * 制作蛋糕
 *
 * @Author xueshun
 * @Create 2018-03-19 13:51
 */
public class Demo {
    public static void main(String[] args) {
        ProductFactory pf = new ProductFactory();

        //下单
        Future f = pf.createProduct("芒果蛋糕");

        System.out.println("去上班了，下班来去蛋糕》》》》");

        System.out.println("下班了来去蛋糕" + f.getProduct());

    }
}
