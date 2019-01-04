package cn.xuesran.inaction.design.chapter11.example;

import cn.xuesran.inaction.design.util.Debug;
import cn.xuesran.inaction.design.util.Tools;
import org.apache.commons.net.ftp.FTPClient;

/**
 * <pre>类名: FakeWorkerThread</pre>
 * <pre>描述: 对WorerThread进一步封装</pre>
 * <pre>日期: 2019/1/2 10:46</pre>
 * <pre>作者: xueshun</pre>
 */
public class FakeWorkerThread extends WorkerThread {

    /**
     * 构造器
     *
     * @param outputDir      保存路径
     * @param ftpServer      服务器IP
     * @param userName       服务器用户名称
     * @param password       服务器用户密码
     * @param servWorkingDir 下载保存路径
     * @throws Exception
     */
    public FakeWorkerThread(String outputDir,
                            String ftpServer,
                            String userName,
                            String password,
                            String servWorkingDir) throws Exception {
        super(outputDir, ftpServer, userName, password, servWorkingDir);
    }

    /**
     * 初始化FTP客户端
     *
     * @param ftpServer ftp服务器
     * @param userName  用户名称
     * @param password  服务器密码
     * @return
     * @throws Exception
     */
    @Override
    protected FTPClient initFTPClient(String ftpServer, String userName,
                                      String password) throws Exception {
        FTPClient ftpClient = new FTPClient();
        return ftpClient;
    }

    /**
     * 下载过程
     *
     * @throws Exception
     */
    @Override
    protected void doRun() throws Exception {
        String file = workQueue.take();
        try {
            Debug.info("Download file %s", file);
            Tools.randomPause(80, 50);
        } finally {
            terminationToken.reservations.decrementAndGet();
        }

    }

}