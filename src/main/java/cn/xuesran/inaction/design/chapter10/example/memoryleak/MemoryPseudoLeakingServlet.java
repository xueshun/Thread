package cn.xuesran.inaction.design.chapter10.example.memoryleak;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 伪内存泄漏
 */
@WebServlet("/pseudo-leak")
public class MemoryPseudoLeakingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    final static ThreadLocal<AtomicInteger> TL_COUNTER =
            new ThreadLocal<AtomicInteger>() {
                @Override
                protected AtomicInteger initialValue() {
                    return new AtomicInteger();
                }

            };

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        PrintWriter pwr = resp.getWriter();
        pwr.write(String.valueOf(TL_COUNTER.get().getAndIncrement()));
        pwr.close();

    }

}