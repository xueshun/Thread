package cn.xuesran.longguo.t2;

import java.util.Timer;
import java.util.TimerTask;

public class Demo05 {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("timertask is run");
            }
        }, 0, 1000);
    }
}
