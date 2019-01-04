package cn.xuesran.inaction.design.chapter13.example;

import java.io.IOException;

public interface RecordSource {

    void close() throws IOException, IOException;

    boolean hasNext();

    Record next();

}