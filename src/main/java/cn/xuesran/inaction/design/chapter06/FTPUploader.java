package cn.xuesran.inaction.design.chapter06;

import java.io.File;

public interface FTPUploader {

    /**
     * 初始化FTP连接
     *
     * @param ftpServer   FTP服务Ip
     * @param ftpUserName 服务器用户名
     * @param password    服务器密码
     * @param serverDir   保存路径
     * @throws Exception
     */
    void init(String ftpServer, String ftpUserName, String password, String serverDir) throws Exception;

    /**
     * 上传
     *
     * @param file
     * @throws Exception
     */
    void upload(File file) throws Exception;

    /**
     * 断开FTP连接
     */
    void disconnect();
}
