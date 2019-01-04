package cn.xuesran.inaction.design.chapter11.example;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <pre>类名: MessageFileDownloader</pre>
 * <pre>描述: 实现FTP文件下载。 模式角色：SerialThreadConfinement.Serializer</pre>
 * <pre>日期: 2019/1/2 10:24</pre>
 * <pre>作者: xueshun</pre>
 */
public class MessageFileDownloader {

    /**
     * 模式角色：SerialThreadConfinement.WorkerThread
     */
    private final WorkerThread workerThread;

    public MessageFileDownloader(String outputDir,
                                 final String ftpServer,
                                 final String userName,
                                 final String password,
                                 final String servWorkingDir) throws Exception {
        Path path = Paths.get(outputDir);
        if (!path.toFile().exists()) {
            Files.createDirectories(path);
        }
        // workerThread = new WorkerThread(outputDir, ftpServer, userName,
        // password, servWorkingDir);
        workerThread = new FakeWorkerThread(
                outputDir,
                ftpServer,
                userName,
                password,
                servWorkingDir);
    }

    public void init() {
        workerThread.start();
    }

    public void shutdown() {
        workerThread.terminate();
    }

    public void downloadFile(String file) {
        workerThread.download(file);
    }
}
