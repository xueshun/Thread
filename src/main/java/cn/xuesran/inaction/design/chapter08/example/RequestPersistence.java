package cn.xuesran.inaction.design.chapter08.example;

import java.io.Closeable;

/**
 * <pre>类名: RequestPersistence</pre>
 * <pre>描述: TODO</pre>
 * <pre>日期: 2018/12/31 16:02</pre>
 * <pre>作者: xueshun</pre>
 */
public interface RequestPersistence extends Closeable {
    void store(MMSDeliverRequest request);
}
