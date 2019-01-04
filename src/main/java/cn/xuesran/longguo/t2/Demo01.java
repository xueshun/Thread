package cn.xuesran.longguo.t2;

public class Demo01 extends Thread {

    public Demo01(String name) {
        super(name);
    }

    @Override
    public void run() {
        while (!interrupted()) {
            System.out.println(getName() + "线程执行了..");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        Demo01 d1 = new Demo01("first");
        Demo01 d2 = new Demo01("second");


        d1.start();
        d2.start();

        d1.interrupt();
        try {
            Thread.sleep(100);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

