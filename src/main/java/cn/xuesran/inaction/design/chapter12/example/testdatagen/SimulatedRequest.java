package cn.xuesran.inaction.design.chapter12.example.testdatagen;

public interface SimulatedRequest {
    public void printLogs(Logger logger);

    public String getInterfaceName();
}