package cn.xuesran.inaction.design.chapter08.example;

import cn.xuesran.inaction.design.chapter05.AbstractTerminatableThread;
import cn.xuesran.inaction.design.chapter05.example.DelegatingTerminatableThread;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CaseRunner {

    public static void main(String[] args) {
        int numRequestThreads = Runtime.getRuntime().availableProcessors();
        int targetTPS = 50;
        /**
         *  单位：秒
         */
        float duration = 12;

        final RequestSender reqSender = new RequestSender(
                (int) duration * targetTPS);

        final Set<AbstractTerminatableThread> requestThreads = new HashSet<AbstractTerminatableThread>();

        AbstractTerminatableThread requestThread;
        for (int i = 0; i < numRequestThreads; i++) {
            requestThread = DelegatingTerminatableThread.of(reqSender);
            requestThreads.add(requestThread);
            requestThread.start();
        }

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Terminating worker threads....");
                for (AbstractTerminatableThread att : requestThreads) {
                    att.terminate(true);
                }
                reqSender.shutdown();
            }
        }, (long) duration * 1000L);

    }

    static class RequestSender implements Runnable {
        final AtomicInteger totalCount = new AtomicInteger();
        private final RequestPersistence persistence;
        private final Attachment attachment;

        public RequestSender(int n) {
            totalCount.set(n);
            persistence = AsyncRequestPersistence.getInstance();
            attachment = new Attachment();
            try {
                // 附件文件，可根据实际情况修改！
                URL url = CaseRunner.class.getClassLoader().getResource(
                        "cn/xuesran/inaction/design/chapter08/example/attachment.jpg");
                attachment.setContentType("image/jpeg");
                attachment.setContent(Files.readAllBytes(Paths.get(url.toURI())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            int remainingCount;
            while ((remainingCount = totalCount.getAndDecrement()) > 0) {
                MMSDeliverRequest request = new MMSDeliverRequest();
                request.setTransactionID(String.valueOf(remainingCount));
                request.setSenderAddress("13612345678");
                request.setTimeStamp(new Date());
                request.setExpiry((System.currentTimeMillis() + 3600000) / 1000);

                request.setSubject("Hi");
                request.getRecipient().addTo("776");
                request.setAttachment(attachment);
                persistence.store(request);
            }
        }

        public void shutdown() {
            try {
                persistence.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}