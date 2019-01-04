package cn.xuesran.inaction.design.chapter10.example;

import cn.xuesran.inaction.design.util.Debug;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程特有对象在一个具体的线程中 ，它是线程全局可见的。某个类的方法中设置的线程对于该方法调用的其他类的方法
 * 也是可见的。这就可以形成隐式传递参数的效果。
 * 即一个类的方法调用另一个类的方法时，前者向后者传递数据可以借助ThreadLocal而不必通过参数传递。
 */
public class ImplicitParameterPassing {

    public static void main(String[] args) throws InterruptedException {
        ClientThread thread;
        BusinessService bs = new BusinessService();
        for (int i = 0; i < 10; i++) {
            thread = new ClientThread("test", bs);
            thread.start();
            thread.join();
        }

    }

}

class ClientThread extends Thread {
    private final String message;
    private final BusinessService bs;
    private static final AtomicInteger SEQ = new AtomicInteger(0);

    public ClientThread(String message, BusinessService bs) {
        this.message = message;
        this.bs = bs;
    }

    @Override
    public void run() {
        Context.INSTANCE.setTransactionId(SEQ.getAndIncrement());
        bs.service(message);
    }

}

class Context {
    static final ThreadLocal<Integer> TS_OBJECT_PROXY = new ThreadLocal<Integer>();

    // Context类的唯一实例
    public static final Context INSTANCE = new Context();

    // 私有构造器
    private Context() {

    }

    public Integer getTransactionId() {
        return TS_OBJECT_PROXY.get();
    }

    public void setTransactionId(Integer transactionId) {
        TS_OBJECT_PROXY.set(transactionId);
    }

    public void reset() {
        TS_OBJECT_PROXY.remove();
    }

}

class BusinessService {

    public void service(String message) {
        int transactionId = Context.INSTANCE.getTransactionId();
        Debug.info("processing transaction " + transactionId + "'s message:" + message);
    }
}