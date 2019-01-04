package cn.xuesran.core.chapter04.conditionTestManyToMany;

public class Run {
    public static void main(String[] args) {
        Myservice myservice = new Myservice();
        MyThreadA[] threadA = new MyThreadA[10];
        MyThreadB[] threadB = new MyThreadB[10];
        for (int i = 0; i < 10; i++) {
            threadA[i] = new MyThreadA(myservice);
            threadB[i] = new MyThreadB(myservice);
            threadA[i].start();
            threadB[i].start();
        }
    }
}
