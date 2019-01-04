package cn.xuesran.inaction.design.chapter08.example;

import java.util.concurrent.Future;

/**
 * <pre>类名: SampleActiveObject</pre>
 * <pre>描述: TODO</pre>
 * <pre>日期: 2018/12/31 15:43</pre>
 * <pre>作者: xueshun</pre>
 */
public interface SampleActiveObject {
    public Future<String> process(String arg, int i);
}
