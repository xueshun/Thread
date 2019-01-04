package cn.xuesran.inaction.design.chapter12.example.testdatagen;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.SimpleTimeZone;
import java.util.concurrent.atomic.AtomicLong;

public class TestingDataGen {

    private static final String TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final SimpleDateFormat SDF_LONG;
    private final Logger logger = new Logger();

    static {
        SimpleTimeZone stz = new SimpleTimeZone(0, "UTC");
        SDF_LONG = new SimpleDateFormat(TIME_STAMP_FORMAT);
        SDF_LONG.setTimeZone(stz);
    }

    public static void main(String[] args) {
        TestingDataGen me = new TestingDataGen();
        Map<String, Map<String, Integer>> delayConf = createDefaultConf();
        int argc = args.length;
        int duration;// 模拟系统流量的时间跨度（发起请求到停止发起请求的持续时间）
        int overrallTPS;// 模拟系统的整体TPS（Transaction per Second）
        overrallTPS = (argc >= 1) ? Integer.valueOf(args[0]) : 100 * 3;
        duration = (argc >= 2) ? Integer.valueOf(args[1]) : 60;
        // 持续时间最短为1分钟（因为日志文件的文件名就是精确到分钟）
        me.generate(overrallTPS, duration, delayConf);

    }

    private static Map<String, Map<String, Integer>> createDefaultConf() {
        Map<String, Map<String, Integer>> delayConf = new HashMap<String, Map<String, Integer>>();

        Map<String, Integer> smsConf = new HashMap<String, Integer>();
        smsConf.put("NIG", 150);
        smsConf.put("ESB", 50);
        delayConf.put("Sms", smsConf);

        Map<String, Integer> locationConf = new HashMap<String, Integer>();
        locationConf.put("NIG", 500);
        locationConf.put("ESB", 20);
        delayConf.put("Location", locationConf);

        Map<String, Integer> chargingConf = new HashMap<String, Integer>();
        chargingConf.put("BSS", 1200);
        chargingConf.put("ESB", 50);
        delayConf.put("Charging", chargingConf);
        return delayConf;
    }

    public void generate(int overallTPS, int duration/* minutes */,
                         Map<String, Map<String, Integer>> respDelayConf) {
        if (duration < 1) {
            throw new IllegalArgumentException(
                    "duration must be greater than 1!");
        }

        doGenerate(overallTPS, duration, respDelayConf);
    }

    public void doGenerate(int overallTPS, int duration/* minutes */,
                           Map<String, Map<String, Integer>> respDelayConf) {

        int count = 0;
        RequestFactory rf;
        SimulatedRequest req;
        int factoryIndex = 0;
        Random rnd = new SecureRandom();

        long requestCount = overallTPS * (duration * 60);

        final int maxRequestPerTimeslice = (overallTPS / 3)
                * AbstractRequestFactory.STAT_INTERVAL;
        final RequestFactory[] factories = new RequestFactory[]{
                new SmsRequestFactory(respDelayConf.get("Sms"),
                        maxRequestPerTimeslice),
                new ChargingRequestFactory(respDelayConf.get("Charging"),
                        maxRequestPerTimeslice),
                new LocationRequestFactory(respDelayConf.get("Location"),
                        maxRequestPerTimeslice)};

        Map<String, AtomicLong> requestCounter = new HashMap<String, AtomicLong>();
        AtomicLong reqCount;
        for (int i = 0; i < requestCount; i++) {

            factoryIndex = rnd.nextInt(factories.length);
            rf = factories[factoryIndex];

            req = rf.newRequest();

            req.printLogs(logger);

            reqCount = requestCounter.get(req.getInterfaceName());
            if (null == reqCount) {
                reqCount = new AtomicLong(0);
                requestCounter.put(req.getInterfaceName(), reqCount);
            }

            reqCount.incrementAndGet();

            count++;
        }
        System.out.println("Log files:" + Logger.LOG_FILE_BASE_DIR);
        System.out.println("Total request count:" + count);
        System.out.println(requestCounter);
    }

}