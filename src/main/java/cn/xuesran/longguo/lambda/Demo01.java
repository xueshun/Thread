package cn.xuesran.longguo.lambda;

import java.util.Arrays;
import java.util.List;

public class Demo01 {
    public static void main(String[] args) {
        String[] atp = {"Rafael Nadal", "Novak Djokovic",
                "Stanislas Wawrinka",
                "David Ferrer", "Roger Federer",
                "Andy Murray", "Tomas Berdych",
                "Juan Martin Del Potro"};
        List<String> players = Arrays.asList(atp);


        //之前的循环方式
       /* for (String player :players) {
            System.out.println(player+";");
        }*/

        //lambda 表达式以及函数操作
        // players.forEach((player)-> System.out.println(player+";"));

        players.forEach(System.out::println);
    }
}
