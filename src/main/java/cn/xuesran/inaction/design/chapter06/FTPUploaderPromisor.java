package cn.xuesran.inaction.design.chapter06;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * <pre>类名: FTPUploaderPromisor</pre>
 * <pre>描述: 模式角色 Promise.Promisor</pre>
 * <pre>日期: 2018/12/29 19:14</pre>
 * <pre>作者: xueshun</pre>
 */
public class FTPUploaderPromisor {

    /**
     * 模式角色：Promise.Promisor.compute
     */
    public static Future<FTPUploader> newFTPUploaderPromise(String ftpServer, String ftpUserName, String password, String serverDir) {
        Executor helperExecutor = command -> {
            Thread t = new Thread(command);
            t.start();
        };
        return newFTPUploaderPromise(ftpServer, ftpUserName, password, serverDir, helperExecutor);
    }

    /**
     * 模式角色：Promise.Promisor.compute
     */
    private static Future<FTPUploader> newFTPUploaderPromise(String ftpServer, String ftpUserName, String password, String serverDir, Executor helperExecutor) {
        Callable<FTPUploader> callable = () -> {
            String implClazz = System.getProperty("ftp.client.impl");
            if (null == implClazz) {
                implClazz = "cn.xuesran.inaction.design.chapter06" + ".FTPClientUtil";

            }
            FTPUploader ftpUploader;
            ftpUploader = (FTPUploader) Class.forName(implClazz).newInstance();
            ftpUploader.init(ftpServer, ftpUserName, password, serverDir);
            return ftpUploader;
        };

        // task相当于模式角色：Promise.Promise
        final FutureTask<FTPUploader> task = new FutureTask<>(callable);
        helperExecutor.execute(task);
        return task;
    }
}
