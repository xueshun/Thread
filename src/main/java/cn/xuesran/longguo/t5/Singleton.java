package cn.xuesran.longguo.t5;

/**
 * 单例模式 饱汉模式
 * 不存在线程安全的问题
 */
public class Singleton {
    private Singleton() {
    }

    private static Singleton instance = new Singleton();

    public static Singleton getInstance() {
        return instance;
    }

    //单例模式  线程安全
    //1 多线程的环境下
    //2 必须有共享资源
    //3 对资源进行非原子性的操作
}
