package cn.xuesran.inaction.design.chapter08.example;

import cn.xuesran.inaction.design.util.Debug;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class MMSDeliveryServlet extends HttpServlet {
    private static final long serialVersionUID = 4042279802462332556L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 将请求中的数据解析为内部对象。
        MMSDeliverRequest mmsDeliverReq = this.parseRequest(req.getInputStream());
        Recipient shortNumberRecipient = mmsDeliverReq.getRecipient();
        Recipient originalNumberRecipient = null;


        try {
            // 将接收方短号转换为长号
            originalNumberRecipient = convertShortNumber(shortNumberRecipient);
        } catch (SQLException e) {
            // 接收方短号转换为长号时发生数据库异常，触发请求消息的缓存
            AsyncRequestPersistence.getInstance().store(mmsDeliverReq);
            // 省略其他代码

            resp.setStatus(202);
        }
        super.doPost(req, resp);

        Debug.info(String.valueOf(originalNumberRecipient));
    }


    /**
     * @param reqInputStream
     * @return
     */
    private MMSDeliverRequest parseRequest(InputStream reqInputStream) {
        MMSDeliverRequest mmsDeliverReq = new MMSDeliverRequest();
        // 省略其他代码
        return mmsDeliverReq;
    }

    /**
     * 将接收方短号转换为长号
     *
     * @param shortNumberRecipient 短号码
     * @return
     * @throws SQLException
     */
    private Recipient convertShortNumber(Recipient shortNumberRecipient) throws SQLException {
        Recipient recipent = null;
        // 省略其他代码
        return recipent;
    }
}
