package thread;

import org.junit.Test;

public class ThreadAvailableProcessorsTest {

    /**
     * 线程池设置多大比较合理
     * IO密集型=2Ncpu（可以测试后自己控制大小，2Ncpu一般没问题）（常出现于线程中：数据库数据交互、文件上传下载、网络数据传输等等）
     * 计算密集型=Ncpu（常出现于线程中：复杂算法）
     * 线程数=Ncpu/（1-阻塞系数）,阻塞系数=w/(w+c)，即阻塞系数=阻塞时间/（阻塞时间+计算时间）
     * Nthreads = NCPU * UCPU * (1 + W/C)
     * 其中：
     * ❑NCPU是处理器的核的数目，可以通过Runtime.getRuntime().availableProcessors()得到
     * ❑UCPU是期望的CPU利用率（该值应该介于0和1之间）
     * ❑W/C是等待时间与计算时间的比率
     */
    @Test
    public void testAvailableProcessors() {
        int num = Runtime.getRuntime().availableProcessors();
        System.out.println("availableProcessors : "  + num);
//        比如4核的处理器NCPU是4，你的程序计算一个方法需要5秒钟，整个程序运行也就需要5秒钟，那么W/C比率应该是100，
//        NCPU利用率希望是100%那么也就是1，总体程序最佳的线程数应该是4*1*(1+100）=404个线程数，但实际操作中，设置404个线程明显不能带来性能的优势，这么多线程数只会增加上下文来回切换带来更严重的性能问题。
//        如果你的程序是计算密集型的并且没有IO操作，那么建议线程数设置为cpu核数+1，减少上下文切换。
//        如果你的程序是IO密集型的（包括网络连接等待），那么可以按照 Nthreads = NCPU * UCPU * (1 + W/C) 计算线程数.
    }
}
