package cn.xuesran.longguo.lambda;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 使用Lambda 排序集合
 */
public class Demo03 {

    public static void main(String[] args) {
        String[] players = {"Rafael Nadal", "Novak Djokovic",
                "Stanislas Wawrinka", "David Ferrer",
                "Roger Federer", "Andy Murray",
                "Tomas Berdych", "Juan Martin Del Potro",
                "Richard Gasquet", "John Isner"};

        // 使用匿名内部类根据name排序
        Arrays.sort(players, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return (s1.compareTo(s2));
            }
        });

        // 使用lambda expression 排序 players
        Comparator<String> sortByName = (String s1, String s2) -> (s1.compareTo(s2));
        Arrays.sort(players, sortByName);

        Arrays.sort(players, (String s1, String s2) -> (s1.compareTo(s2)));
    }
}
