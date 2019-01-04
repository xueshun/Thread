package cn.xuesran.inaction.design.chapter08.example;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * <pre>类名: Attachment</pre>
 * <pre>描述: 短信 - 附件</pre>
 * <pre>日期: 2018/12/31 16:07</pre>
 * <pre>作者: xueshun</pre>
 */
public class Attachment implements Serializable {
    private static final long serialVersionUID = -9055743828040733781L;
    private String contentType;
    private byte[] content = new byte[0];

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
