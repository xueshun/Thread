package cn.xuesran.inaction.design.chapter08.example;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>类名: MMSDeliverRequest</pre>
 * <pre>描述: 彩信下发请求</pre>
 * <pre>日期: 2018/12/31 16:03</pre>
 * <pre>作者: xueshun</pre>
 */
public class MMSDeliverRequest implements Serializable {
    private static final long serialVersionUID = -2234377687492585149L;

    private String transactionID;
    private String messageType = "Delivery.req";
    private String senderAddress;

    /**
     * 彩信消息接收方
     */
    private Recipient recipient = new Recipient();
    private String subject;
    private Attachment attachment = new Attachment();

    private long expiry;
    private Date timeStamp;

    /**
     * 获取transactionID
     *
     * @return transactionID
     */
    public String getTransactionID() {
        return transactionID;
    }

    /**
     * 设置transactionID
     *
     * @param transactionID transactionID
     */
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    /**
     * 获取messageType
     *
     * @return messageType
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * 设置messageType
     *
     * @param messageType messageType
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * 获取senderAddress
     *
     * @return senderAddress
     */
    public String getSenderAddress() {
        return senderAddress;
    }

    /**
     * 设置senderAddress
     *
     * @param senderAddress senderAddress
     */
    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    /**
     * 获取recipient
     *
     * @return recipient
     */
    public Recipient getRecipient() {
        return recipient;
    }

    /**
     * 设置recipient
     *
     * @param recipient recipient
     */
    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    /**
     * 获取subject
     *
     * @return subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 设置subject
     *
     * @param subject subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 获取attachment
     *
     * @return attachment
     */
    public Attachment getAttachment() {
        return attachment;
    }

    /**
     * 设置attachment
     *
     * @param attachment attachment
     */
    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    /**
     * 获取expiry
     *
     * @return expiry
     */
    public long getExpiry() {
        return expiry;
    }

    /**
     * 设置expiry
     *
     * @param expiry expiry
     */
    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    /**
     * 获取timeStamp
     *
     * @return timeStamp
     */
    public Date getTimeStamp() {
        return timeStamp;
    }

    /**
     * 设置timeStamp
     *
     * @param timeStamp timeStamp
     */
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
