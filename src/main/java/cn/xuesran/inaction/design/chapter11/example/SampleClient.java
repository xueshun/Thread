package cn.xuesran.inaction.design.chapter11.example;

import cn.xuesran.inaction.design.util.Tools;

public class SampleClient {
    private static final MessageFileDownloader DOWNLOADER;

    static {

        // 请根据实际情况修改构造器MessageFileDownloader的参数
        MessageFileDownloader mfd = null;
        try {
            mfd = new MessageFileDownloader(
                    Tools.getWorkingDir("ch11"),
                    "192.168.174.6",
                    "root",
                    "123123",
                    "/root/messages/");
            mfd.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DOWNLOADER = mfd;
    }

    public static void main(String[] args) {
        DOWNLOADER.downloadFile("abc.xml");
        DOWNLOADER.downloadFile("123.xml");
        DOWNLOADER.downloadFile("xyz.xml");

        // 执行其他操作
        Tools.randomPause(80000, 40000);
        DOWNLOADER.shutdown();
    }

}