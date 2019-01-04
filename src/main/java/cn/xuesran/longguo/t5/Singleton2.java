package cn.xuesran.longguo.t5;

/**
 * 单例模式  懒汉模式
 */
public class Singleton2 {

    private Singleton2() {
    }

    private static Singleton2 instance;

    /**
     * 这在 instance = new Singleton2(); 存在线程安全行的问题
     * @return
     */
    /*public  static Singleton2 getInstance(){
        if(instance==null){
            instance = new Singleton2();
        }
        return  instance;
    }*/

    /**
     *  偏向锁： 单线程
     *  轻量级锁： 自旋
     *     所以可以看出这样写性能不好
     *     while(true)
     * @return
     */
    /*public  static synchronized Singleton2 getInstance(){
        if(instance==null){
            instance = new Singleton2();
        }
        return  instance;
    }*/


    /**
     *  这样写要考虑一下 指令重排序的问题
     *
     *        1. 申请一块内存空间
     *        2. 在这块空间里面实例化对象
     *        3. instance 引用指向这块空间地址
     *
     *   正常的初始化时这样的。
     *      但是有时候可能会
     *          2 - 1 -3 执行
     * @return
     */
   /* public  static Singleton2 getInstance(){
        if(instance==null){
            synchronized (Singleton2.class){
                instance = new Singleton2();
            }
        }
        return  instance;
    }*/

    /**
     * 双重检查加锁
     *
     * @return
     */
    public static Singleton2 getInstance() {
        if (instance == null) {
            synchronized (Singleton2.class) {
                if (instance == null) {
                    instance = new Singleton2();
                }
            }
        }
        return instance;
    }
}
