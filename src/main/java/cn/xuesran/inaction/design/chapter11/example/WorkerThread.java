package cn.xuesran.inaction.design.chapter11.example;

import cn.xuesran.inaction.design.chapter05.AbstractTerminatableThread;
import cn.xuesran.inaction.design.util.Debug;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * <pre>类名: WorkerThread</pre>
 * <pre>描述: 工作线程</pre>
 * <pre>日期: 2019/1/2 10:26</pre>
 * <pre>作者: xueshun</pre>
 */
public class WorkerThread extends AbstractTerminatableThread {

    /**
     * 模式角色：SerialThreadConfinement.Queue
     */
    protected final BlockingQueue<String> workQueue;
    /**
     * FTPClient
     */
    private final FTPClient ftpClient;
    /**
     * 服务器下载目录
     */
    private final String outputDir;
    /**
     * 本地保存目录
     */
    private String servWorkingDir;

    public WorkerThread(String outputDir, final String ftpServer,
                        final String userName, final String password, String servWorkingDir)
            throws Exception {
        this.workQueue = new ArrayBlockingQueue<String>(100);
        this.outputDir = outputDir + '/';
        this.servWorkingDir = servWorkingDir;
        this.ftpClient = initFTPClient(ftpServer, userName, password);
    }


    /**
     * 初始化FTPClient
     *
     * @param ftpServer ftp服务器
     * @param userName  用户名称
     * @param password  服务器密码
     * @return
     * @throws Exception
     */
    protected FTPClient initFTPClient(String ftpServer, String userName, String password) throws Exception {
        FTPClient ftpClient = new FTPClient();

        FTPClientConfig config = new FTPClientConfig();
        ftpClient.configure(config);

        int reply;
        ftpClient.connect(ftpServer);

        System.out.print(ftpClient.getReplyString());

        reply = ftpClient.getReplyCode();

        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new RuntimeException("FTP server refused connection.");
        }

        boolean isOK = ftpClient.login(userName, password);
        if (isOK) {
            System.out.println(ftpClient.getReplyString());
        } else {
            throw new RuntimeException(
                    "Failed to login." + ftpClient.getReplyString());
        }

        reply = ftpClient.cwd(servWorkingDir);
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new RuntimeException(
                    "Failed to change working directory.reply:" + reply);
        } else {

            System.out.println(ftpClient.getReplyString());
        }

        ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
        return ftpClient;
    }

    /**
     * 下载
     *
     * @param file 文件名称
     */
    public void download(String file) {
        try {
            workQueue.put(file);
            terminationToken.reservations.incrementAndGet();
        } catch (InterruptedException e) {
            ;
        }
    }

    @Override
    protected void doRun() throws Exception {
        String file = workQueue.take();
        Debug.info("Downloading %s", file);
        boolean isOK;
        try (OutputStream os = new BufferedOutputStream(
                new FileOutputStream(outputDir + file))) {
            isOK = ftpClient.retrieveFile(file, os);
            if (!isOK) {
                Debug.error("Failed to download %s", file);
            }
        } finally {
            terminationToken.reservations.decrementAndGet();
        }
    }


    @Override
    protected void doCleanUp(Exception cause) {
        try {
            ftpClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
