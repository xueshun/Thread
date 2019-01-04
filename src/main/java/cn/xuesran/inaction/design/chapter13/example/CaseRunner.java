package cn.xuesran.inaction.design.chapter13.example;

import cn.xuesran.inaction.design.util.Tools;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

public class CaseRunner {

    public static void main(String[] args) throws IOException {
        Properties config = Tools.loadProperties(
                CaseRunner.class.getPackage().getName().replaceAll("\\.", "/")
                        + "/conf.properties");

        DataSyncTask dst;
        // dst = new DataSyncTask(config);
        // 运行本程序前，请根据实际情况修改以下方法中有关数据库连接和FTP账户的信息
        dst = new DataSyncTask(config) {
            {
                System.setProperty("ftp.client.impl",
                        "io.github.viscent.mtpattern.ch6.promise.example.FakeFTPUploader");
            }

            @Override
            protected RecordSource makeRecordSource(Properties config)
                    throws Exception {
                return new FakeRecordSource();
            }

        };

        dst.run();

    }

    // 模拟从数据库中读取数据
    private static class FakeRecordSource implements RecordSource {
        private final Scanner scanner;
        private final GZIPInputStream gis;

        public FakeRecordSource() throws IOException {
            this.gis = new GZIPInputStream(
                    new BufferedInputStream(CaseRunner.class
                            .getResourceAsStream(
                                    "/data/ch13/subscriptions.csv.gz")));
            this.scanner = new Scanner(gis, "UTF-8");
        }

        @Override
        public void close() throws IOException {
            try {
                scanner.close();
            } finally {
                gis.close();
            }
        }

        @Override
        public boolean hasNext() {
            return scanner.hasNextLine();
        }

        @Override
        public Record next() {
            String line = scanner.nextLine();
            Record record = null;
            try {
                record = Record.parseCsv(line);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return record;
        }
    }

}