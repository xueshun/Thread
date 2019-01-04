package cn.xuesran.inaction.design.chapter10.example.memoryleak;

public class Counter {
    private int i = 0;

    public int getAndIncrement() {
        return (i++);
    }
}