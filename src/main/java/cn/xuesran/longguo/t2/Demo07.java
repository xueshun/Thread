package cn.xuesran.longguo.t2;

import java.util.Arrays;
import java.util.List;

/**
 * jdk 1.8 提供的lambda进行并行计算
 */
public class Demo07 {

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(10, 20, 30, 40);
        int res = new Demo07().add(values);
        System.out.println("计算结果为：" + res);

    }

    public int add(List<Integer> values) {
        return values.parallelStream().mapToInt(i -> i * 2).sum();
    }
}
