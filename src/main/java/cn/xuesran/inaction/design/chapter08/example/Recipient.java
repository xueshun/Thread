package cn.xuesran.inaction.design.chapter08.example;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * <pre>类名: Recipient</pre>
 * <pre>描述: 短信接收方</pre>
 * <pre>版权: 税友软件集团股份有限公司</pre>
 * <pre>日期: 2018/12/31 16:03</pre>
 * <pre>作者: xueshun</pre>
 */
public class Recipient implements Serializable {
    private static final long serialVersionUID = -1203145702038659875L;
    private Set<String> to = new HashSet<String>();

    public void addTo(String msisdn) {
        to.add(msisdn);
    }

    public Set<String> getToList() {
        return (Set<String>) Collections.unmodifiableCollection(to);
    }
}
