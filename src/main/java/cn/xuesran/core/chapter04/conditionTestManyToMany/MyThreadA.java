package cn.xuesran.core.chapter04.conditionTestManyToMany;

public class MyThreadA extends Thread {
    private Myservice myservice;

    public MyThreadA(Myservice myservice) {
        super();
        this.myservice = myservice;
    }

    @Override
    public void run() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            myservice.set();
        }
    }
}
