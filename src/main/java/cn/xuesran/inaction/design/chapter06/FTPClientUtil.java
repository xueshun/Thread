package cn.xuesran.inaction.design.chapter06;


import cn.xuesran.core.chapter03.ThreadLocal11.Run;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>类名: FTPClientUtil</pre>
 * <pre>描述: FTP 客户端工具 模式角色：Promise.Result</pre>
 * <pre>日期: 2018/12/29 17:36</pre>
 * <pre>作者: xueshun</pre>
 */
public class FTPClientUtil implements FTPUploader {

    final FTPClient ftp = new FTPClient();
    final Map<String, Boolean> dirCreateMap = new HashMap<>();

    @Override
    public void init(String ftpServer, String userName, String password, String saverDir) throws Exception {
        FTPClientConfig config = new FTPClientConfig();
        ftp.configure(config);

        int reply;
        ftp.connect(ftpServer);
        reply = ftp.getReplyCode();

        System.out.println(ftp.getReply());
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new RuntimeException("FTP server refused connection");
        }

        // 登陆操作
        boolean isOk = ftp.login(userName, password);
        if (isOk) {
            System.out.println(ftp.getReplyString());
        } else {
            throw new RuntimeException("Failed to login." + ftp.getReplyString());
        }

        // 创建目录
        reply = ftp.cwd(saverDir);
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new RuntimeException("Failed to change working directory.reply:" + reply);
        } else {

            System.out.println(ftp.getReplyString());
        }

        ftp.setFileType(FTP.ASCII_FILE_TYPE);
    }

    @Override
    public void upload(File file) throws Exception {
        InputStream dataIn = new BufferedInputStream(new FileInputStream(file), 1024 * 8);
        boolean isOK;
        String dirName = file.getParentFile().getName();
        String fileName = dirName + '/' + file.getName();
        ByteArrayInputStream checkFileInputStream = new ByteArrayInputStream("".getBytes());

        try {
            if (!dirCreateMap.containsKey(dirName)) {
                ftp.makeDirectory(dirName);
                dirCreateMap.put(dirName, null);
            }

            try {
                isOK = ftp.storeFile(fileName, dataIn);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload " + file, e);
            }
            if (isOK) {
                ftp.storeFile(fileName + ".c", checkFileInputStream);

            } else {

                throw new RuntimeException(
                        "Failed to upload " + file + ",reply:"
                                + ftp.getReplyString());
            }
        } finally {
            dataIn.close();
        }

    }

    @Override
    public void disconnect() {
        if (ftp.isConnected()) {
            try {
                ftp.disconnect();
            } catch (IOException ioe) {
                // 什么也不做
            }
        }
        // 省略与清单6-2中相同的代码
    }

}
