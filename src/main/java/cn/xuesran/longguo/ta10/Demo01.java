package cn.xuesran.longguo.ta10;

import com.google.common.collect.Lists;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author xueshun
 * @Create 2018-03-16 17:47
 */
public class Demo01 {

    private int[] nums;

    public Demo01(int line) {
        nums = new int[line];
    }

    public void calc(String line, int index) {
        String[] nus = line.split(","); // 切分出每个值
        int total = 0;
        for (String num : nus) {
            total += Integer.parseInt(num);
        }
        nums[index] = total; // 把计算的结果放到数组中指定的位置
        System.out.println(Thread.currentThread().getName() + " 执行计算任务... " + line + " 结果为：" + total);
    }

    public void sum() {
        System.out.println("汇总线程开始执行... ");
        int total = 0;
        for (int i = 0; i < nums.length; i++) {
            total += nums[i];
        }
        System.out.println("最终的结果为：" + total);
    }

    public static void main(String[] args) {

        List<String> contents = readFile();
        int lineCount = contents.size();

        Demo01 d = new Demo01(lineCount);
        for (int i = 0; i < lineCount; i++) {
            final int j = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    d.calc(contents.get(j), j);
                }
            }).start();
        }

        while (Thread.activeCount() > 1) {

        }

        d.sum();
    }

    private static List<String> readFile() {
        List<String> contents = new ArrayList<>();
        String line = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("G:\\nums.txt"));
            while ((line = br.readLine()) != null) {
                contents.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return contents;
    }

}

