package cn.xuesran.inaction.design.chapter08.example;

import cn.xuesran.inaction.design.util.Debug;

/**
 * <pre>类名: SampleActiveObjectImpl</pre>
 * <pre>描述: TODO</pre>
 * <pre>日期: 2018/12/31 15:49</pre>
 * <pre>作者: xueshun</pre>
 */
public class SampleActiveObjectImpl {
    public String doProcess(String arg, int i) {
        Debug.info("doProcess start");
        try {
            // 模拟一个比较耗时的操作
            Thread.sleep(500);
        } catch (InterruptedException e) {
            ;
        }
        return arg + "-" + i;
    }
}
