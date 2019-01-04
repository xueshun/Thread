package cn.xuesran.core.chapter04.conditionTestManyToMany;

public class MyThreadB extends Thread {

    private Myservice myservice;

    public MyThreadB(Myservice myservice) {
        this.myservice = myservice;
    }

    @Override
    public void run() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            myservice.get();
        }
    }
}
