package cn.xuesran.inaction.design.chapter08.example;

import cn.xuesran.inaction.design.chapter08.ActiveObjectProxy;
import cn.xuesran.inaction.design.util.Debug;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <pre>类名: SampleClientOfReusableActiveObject</pre>
 * <pre>描述: TODO</pre>
 * <pre>日期: 2018/12/31 15:50</pre>
 * <pre>作者: xueshun</pre>
 */
public class SampleClientOfReusableActiveObject {
    public static void main(String[] args)
            throws InterruptedException, ExecutionException {

        SampleActiveObject sao = ActiveObjectProxy.newInstance(
                SampleActiveObject.class, new SampleActiveObjectImpl(),
                Executors.newCachedThreadPool());

        Future<String> ft = null;

        Debug.info("Before calling active object");
        try {
            ft = sao.process("Something", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 模拟其他操作的时间消耗
        Thread.sleep(40);

        Debug.info(ft.get());
    }
}
