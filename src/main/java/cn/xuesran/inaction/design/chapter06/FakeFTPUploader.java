package cn.xuesran.inaction.design.chapter06;

import cn.xuesran.inaction.design.util.Debug;
import cn.xuesran.inaction.design.util.Tools;

import java.io.File;

public class FakeFTPUploader implements FTPUploader {

    @Override
    public void upload(File file) throws Exception {
        Debug.info("uploading %s", file);
        // 模拟文件上传所需的耗时
        Tools.randomPause(100, 50);
    }

    @Override
    public void disconnect() {
        // 什么也不做
    }

    @Override
    public void init(String ftpServer, String ftpUserName, String password,
                     String serverDir) throws Exception {
        // 什么也不做

    }
}