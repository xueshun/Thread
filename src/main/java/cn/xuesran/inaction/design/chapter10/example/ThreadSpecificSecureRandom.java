package cn.xuesran.inaction.design.chapter10.example;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * <pre>类名: ThreadSpecificSecureRandom</pre>
 * <pre>描述: 线程特有SecureRandom</pre>
 * <pre>日期: 2019/1/1 14:58</pre>
 * <pre>作者: xueshun</pre>
 */
public class ThreadSpecificSecureRandom {

    /**
     * 该类的唯一实例
     */
    public static final ThreadSpecificSecureRandom INSTANCE = new ThreadSpecificSecureRandom();

    /**
     * SECURE_RANDOM相当于模式角色：ThreadSpecificStorage.TSObjectProxy。
     * SecureRandom相当于模式角色：ThreadSpecificStorage.TSObject。
     */
    private static final ThreadLocal<SecureRandom> SECURE_RANDOM =
            ThreadLocal.withInitial(() -> {
                SecureRandom srnd;
                try {
                    srnd = SecureRandom.getInstance("SHA1PRNG");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    srnd = new SecureRandom();
                }
                return srnd;
            });

    private ThreadSpecificSecureRandom() {
    }

    public int nextInt(int upperBound) {
        SecureRandom secureRnd = SECURE_RANDOM.get();
        System.out.println(Thread.currentThread().getName() + "" + secureRnd.nextInt());
        return secureRnd.nextInt(upperBound);
    }

    public void setSeed(long seed) {
        SecureRandom secureRnd = SECURE_RANDOM.get();
        secureRnd.setSeed(seed);
    }
}
