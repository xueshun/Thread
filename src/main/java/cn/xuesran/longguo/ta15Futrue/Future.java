package cn.xuesran.longguo.ta15Futrue;

/**
 * @Author xueshun
 * @Create 2018-03-19 13:51
 */
public class Future {

    private Product product;

    private boolean down;

    public synchronized void setProduct(Product product) {
        if (down) {
            return;
        }
        this.product = product;
        this.down = true;
        this.product = product;
        notifyAll();
    }

    public synchronized Product getProduct() {
        while (!down) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return product;
    }


}
