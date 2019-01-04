package cn.xuesran.inaction.design.chapter07;

/**
 * <pre>类名: Channel</pre>
 * <pre>描述: 对通道参与者进行抽象</pre>
 * <pre>日期: 2018/12/30 16:53</pre>
 * <pre>作者: xueshun</pre>
 */
public interface Channel<P> {

    /**
     * 从通道中取出一个“产品”
     *
     * @return “产品”
     * @throws InterruptedException
     */
    P take() throws InterruptedException;

    /**
     * 往通道中存储一个“产品”
     *
     * @param product
     * @throws InterruptedException
     */
    void put(P product) throws InterruptedException;
}
