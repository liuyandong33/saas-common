package build.dream.common.domains.admin;

import build.dream.common.basic.BasicDomain;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class JavaOperation extends BasicDomain {
    /**
     * JVM启动时申请的初始Heap值，默认为操作系统物理内存的1/64但小于1G。默认当空余堆内存大于70%时，JVM会减小heap的大小到-Xms指定的大小，
     * 可通过-XX:MaxHeapFreeRation=来指定这个比列。Server端JVM最好将-Xms和-Xmx设为相同值，避免每次垃圾回收完成后JVM重新分配内存；开发测试机JVM可以保留默认值。(例如：-Xms4g)
     */
    private String xms;

    /**
     * JVM可申请的最大Heap值，默认值为物理内存的1/4但小于1G，默认当空余堆内存小于40%时，JVM会增大Heap到-Xmx指定的大小，可通过-XX:MinHeapFreeRation=来指定这个比列。
     * 最佳设值应该视物理内存大小及计算机内其他内存开销而定。(例如：-Xmx4g)
     */
    private String xmx;

    /**
     * Java Heap Young区大小。整个堆大小=年轻代大小 + 年老代大小 + 持久代大小(相对于HotSpot 类型的虚拟机来说)。持久代一般固定大小为64m，
     * 所以增大年轻代后，将会减小年老代大小。此值对系统性能影响较大，Sun官方推荐配置为整个堆的3/8。(例如：-Xmn2g)
     */
    private String xmn;

    /**
     * Java每个线程的Stack大小。JDK5.0以后每个线程堆栈大小为1M，以前每个线程堆栈大小为256K。根据应用的线程所需内存大小进行调整。
     * 在相同物理内存下，减小这个值能生成更多的线程。但是操作系统对一个进程内的线程数还是有限制的，不能无限生成，经验值在3000~5000左右。(例如：-Xss1024K)
     */
    private String xss;

    /**
     * 持久代（方法区）的初始内存大小。（例如：-XX:PermSize=64m）
     */
    private String xxPermSize;

    /**
     * 持久代（方法区）的最大内存大小。（例如：-XX:MaxPermSize=512m）
     */
    private String xxMaxPermSize;

    /**
     * 串行（SerialGC）是jvm的默认GC方式，一般适用于小型应用和单处理器，算法比较简单，GC效率也较高，但可能会给应用带来停顿。
     */
    private boolean xxUseSerialGc;

    /**
     * 并行（ParallelGC）是指多个线程并行执行GC，一般适用于多处理器系统中，可以提高GC的效率，但算法复杂，系统消耗较大。（配合使用：-XX:ParallelGCThreads=8，并行收集器的线程数，此值最好配置与处理器数目相等）
     */
    private boolean xxUseParallelGc;

    /**
     * 并行收集器的线程数，此值最好配置与处理器数目相等
     */
    private String xxParallelGcThreads;

    /**
     * 设置年轻代为并行收集，JKD5.0以上，JVM会根据系统配置自行设置，所以无需设置此值。
     */
    private boolean xxUseParNewGc;

    /**
     * 设置年老代为并行收集，JKD6.0出现的参数选项。
     */
    private boolean xxUseParallelOldGc;

    /**
     * 并发（ConcMarkSweepGC）是指GC运行时，对应用程序运行几乎没有影响（也会造成停顿，不过很小而已），GC和app两者的线程在并发执行，这样可以最大限度不影响app的运行。
     */
    private boolean xxUseConcMarkSweepGc;

    /**
     * 在Full GC的时候，对老年代进行压缩整理。因为CMS是不会移动内存的，因此非常容易产生内存碎片。因此增加这个参数就可以在FullGC后对内存进行压缩整理，消除内存碎片。当然这个操作也有一定缺点，就是会增加CPU开销与GC时间，所以可以通过-XX:CMSFullGCsBeforeCompaction=3 这个参数来控制多少次Full GC以后进行一次碎片整理。
     */
    private boolean xxUseCmsCompactAtFullCollection;

    /**
     * 代表老年代使用空间达到80%后，就进行Full GC。CMS收集器在进行垃圾收集时，和应用程序一起工作，所以，不能等到老年代几乎完全被填满了再进行收集，这样会影响并发的应用线程的空间使用，从而再次触发不必要的Full GC。
     */
    private String xxCmsInitiatingOccupancyFraction;

    /**
     * 垃圾的最大年龄，代表对象在Survivor区经过10次复制以后才进入老年代。如果设置为0，则年轻代对象不经过Survivor区，直接进入老年代。
     */
    private String xxMaxTenuringThreshold;

    /**
     *
     */
    private String xxMaxHeapFreeRation;

    /**
     *
     */
    private String xxMinHeapFreeRation;

    public String getXms() {
        return xms;
    }

    public void setXms(String xms) {
        this.xms = xms;
    }

    public String getXmx() {
        return xmx;
    }

    public void setXmx(String xmx) {
        this.xmx = xmx;
    }

    public String getXmn() {
        return xmn;
    }

    public void setXmn(String xmn) {
        this.xmn = xmn;
    }

    public String getXss() {
        return xss;
    }

    public void setXss(String xss) {
        this.xss = xss;
    }

    public String getXxPermSize() {
        return xxPermSize;
    }

    public void setXxPermSize(String xxPermSize) {
        this.xxPermSize = xxPermSize;
    }

    public String getXxMaxPermSize() {
        return xxMaxPermSize;
    }

    public void setXxMaxPermSize(String xxMaxPermSize) {
        this.xxMaxPermSize = xxMaxPermSize;
    }

    public boolean isXxUseSerialGc() {
        return xxUseSerialGc;
    }

    public void setXxUseSerialGc(boolean xxUseSerialGc) {
        this.xxUseSerialGc = xxUseSerialGc;
    }

    public boolean isXxUseParallelGc() {
        return xxUseParallelGc;
    }

    public void setXxUseParallelGc(boolean xxUseParallelGc) {
        this.xxUseParallelGc = xxUseParallelGc;
    }

    public String getXxParallelGcThreads() {
        return xxParallelGcThreads;
    }

    public void setXxParallelGcThreads(String xxParallelGcThreads) {
        this.xxParallelGcThreads = xxParallelGcThreads;
    }

    public boolean isXxUseParNewGc() {
        return xxUseParNewGc;
    }

    public void setXxUseParNewGc(boolean xxUseParNewGc) {
        this.xxUseParNewGc = xxUseParNewGc;
    }

    public boolean isXxUseParallelOldGc() {
        return xxUseParallelOldGc;
    }

    public void setXxUseParallelOldGc(boolean xxUseParallelOldGc) {
        this.xxUseParallelOldGc = xxUseParallelOldGc;
    }

    public boolean isXxUseConcMarkSweepGc() {
        return xxUseConcMarkSweepGc;
    }

    public void setXxUseConcMarkSweepGc(boolean xxUseConcMarkSweepGc) {
        this.xxUseConcMarkSweepGc = xxUseConcMarkSweepGc;
    }

    public boolean isXxUseCmsCompactAtFullCollection() {
        return xxUseCmsCompactAtFullCollection;
    }

    public void setXxUseCmsCompactAtFullCollection(boolean xxUseCmsCompactAtFullCollection) {
        this.xxUseCmsCompactAtFullCollection = xxUseCmsCompactAtFullCollection;
    }

    public String getXxCmsInitiatingOccupancyFraction() {
        return xxCmsInitiatingOccupancyFraction;
    }

    public void setXxCmsInitiatingOccupancyFraction(String xxCmsInitiatingOccupancyFraction) {
        this.xxCmsInitiatingOccupancyFraction = xxCmsInitiatingOccupancyFraction;
    }

    public String getXxMaxTenuringThreshold() {
        return xxMaxTenuringThreshold;
    }

    public void setXxMaxTenuringThreshold(String xxMaxTenuringThreshold) {
        this.xxMaxTenuringThreshold = xxMaxTenuringThreshold;
    }

    public String getXxMaxHeapFreeRation() {
        return xxMaxHeapFreeRation;
    }

    public void setXxMaxHeapFreeRation(String xxMaxHeapFreeRation) {
        this.xxMaxHeapFreeRation = xxMaxHeapFreeRation;
    }

    public String getXxMinHeapFreeRation() {
        return xxMinHeapFreeRation;
    }

    public void setXxMinHeapFreeRation(String xxMinHeapFreeRation) {
        this.xxMinHeapFreeRation = xxMinHeapFreeRation;
    }

    public String buildJavaOpts() {
        List<String> javaOpts = new ArrayList<String>();
        if (StringUtils.isNotBlank(xms)) {
            javaOpts.add("-Xms" + xms);
        }

        if (StringUtils.isNotBlank(xmx)) {
            javaOpts.add("-Xmx" + xmx);
        }

        if (StringUtils.isNotBlank(xmn)) {
            javaOpts.add("-Xmn" + xmn);
        }

        if (StringUtils.isNotBlank(xss)) {
            javaOpts.add("-Xss" + xss);
        }

        if (StringUtils.isNotBlank(xxPermSize)) {
            javaOpts.add("-XX:PermSize" + xxPermSize);
        }

        if (StringUtils.isNotBlank(xxMaxPermSize)) {
            javaOpts.add("-XX:MaxPermSize=" + xxMaxPermSize);
        }

        if (xxUseSerialGc) {
            javaOpts.add("-XX:+UseSerialGC");
        }

        if (xxUseParallelGc) {
            javaOpts.add("-XX:+UseParallelGC");
        }

        if (StringUtils.isNotBlank(xxParallelGcThreads)) {
            javaOpts.add("-XX:ParallelGCThreads=" + xxParallelGcThreads);
        }

        if (xxUseParNewGc) {
            javaOpts.add("-XX:+UseParNewGC");
        }

        if (xxUseParallelOldGc) {
            javaOpts.add("-XX:+UseParallelOldGC");
        }

        if (xxUseConcMarkSweepGc) {
            javaOpts.add("-XX:+UseConcMarkSweepGC");
        }

        if (xxUseCmsCompactAtFullCollection) {
            javaOpts.add("-XX:+UseCMSCompactAtFullCollection");
        }

        if (StringUtils.isNotBlank(xxCmsInitiatingOccupancyFraction)) {
            javaOpts.add("-XX:+CMSInitiatingOccupancyFraction=" + xxCmsInitiatingOccupancyFraction);
        }

        if (StringUtils.isNotBlank(xxMaxTenuringThreshold)) {
            javaOpts.add("-XX:+MaxTenuringThreshold=" + xxMaxTenuringThreshold);
        }

        if (StringUtils.isNotBlank(xxMaxHeapFreeRation)) {
            javaOpts.add("-XX:MaxHeapFreeRation=" + xxMaxHeapFreeRation);
        }

        if (StringUtils.isNotBlank(xxMinHeapFreeRation)) {
            javaOpts.add("-XX:MinHeapFreeRation=" + xxMinHeapFreeRation);
        }
        return StringUtils.join(javaOpts, " ");
    }
}
