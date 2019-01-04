package cn.xuesran.inaction.design.chapter06;

import cn.xuesran.inaction.design.util.Tools;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * <pre>类名: DataSyncTask</pre>
 * <pre>描述: TODO</pre>
 * <pre>版权: 税友软件集团股份有限公司</pre>
 * <pre>日期: 2018/12/29 18:00</pre>
 * <pre>作者: xueshun</pre>
 */
public class DataSyncTask implements Runnable {
    private final Map<String, String> taskParameters;

    public DataSyncTask(Map<String, String> taskParameters) {
        this.taskParameters = taskParameters;
    }

    @Override
    public void run() {
        String ftpServer = taskParameters.get("server");
        String ftpUserName = taskParameters.get("userName");
        String password = taskParameters.get("password");
        String serverDir = taskParameters.get("serverDir");

        // 先开始初始化FTP客户端实例
        Future<FTPUploader> ftpClientUtilPromise = FTPUploaderPromisor.newFTPUploaderPromise(ftpServer, ftpUserName, password, serverDir);

        //查询数据生成本地文件
        generateFilesFromDB();
        FTPUploader ftpClientUtil = null;
        try {
            // 获取初始化完毕的FTP客户端实例
            ftpClientUtil = ftpClientUtilPromise.get();
        } catch (InterruptedException e) {
            ;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        //上传文件
        uploadFiles(ftpClientUtil);
    }

    /**
     * 从本地数据库中读取数据
     */
    private void generateFilesFromDB() {
        System.out.println("generating file form database...");
        // 模拟实际操作所需要的耗时
        Tools.randomPause(1000, 500);
    }

    private void uploadFiles(FTPUploader ftpClientUtil) {
        Set<File> files = retrieveGeneratedFiles();
        for (File file : files) {
            try {
                ftpClientUtil.upload(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected Set<File> retrieveGeneratedFiles() {
        Set<File> files = new HashSet<>();

        //模拟生成本地文件
        File currDir = new File(Tools.getWorkingDir("/production/thread/cn/xuesran/inaction/design/chapter06"));
        for (File f : currDir.listFiles(
                (dir, name) -> new File(dir, name).isFile()
                        && name.endsWith(".class"))) {
            files.add(f);
        }

        return files;
    }
}
