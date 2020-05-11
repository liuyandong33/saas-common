package build.dream.common.utils;

import org.springframework.util.ReflectionUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JvmUtils {
    private static MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private static RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    private static Map<String, Method> methodMap = new HashMap<String, Method>();

    public static int getProcessId() {
        String name = getName();
        return Integer.parseInt(name.substring(0, name.indexOf("@")));
    }

    public static String getName() {
        return runtimeMXBean.getName();
    }

    public static String getVmName() {
        return runtimeMXBean.getVmName();
    }

    public static String getVmVendor() {
        return runtimeMXBean.getVmVendor();
    }

    public static String getVmVersion() {
        return runtimeMXBean.getVmVersion();
    }

    public static String getSpecName() {
        return runtimeMXBean.getSpecName();
    }

    public static String getSpecVendor() {
        return runtimeMXBean.getSpecVendor();
    }

    public static String getSpecVersion() {
        return runtimeMXBean.getSpecVersion();
    }

    public static String getManagementSpecVersion() {
        return runtimeMXBean.getManagementSpecVersion();
    }

    public static String getClassPath() {
        return runtimeMXBean.getClassPath();
    }

    public static String getLibraryPath() {
        return runtimeMXBean.getLibraryPath();
    }

    public static boolean isBootClassPathSupported() {
        return runtimeMXBean.isBootClassPathSupported();
    }

    public static String getBootClassPath() {
        return runtimeMXBean.getBootClassPath();
    }

    public static List<String> getInputArguments() {
        return runtimeMXBean.getInputArguments();
    }

    public static long getUptime() {
        return runtimeMXBean.getUptime();
    }

    public static long getStartTime() {
        return runtimeMXBean.getStartTime();
    }

    public static Map<String, String> getSystemProperties() {
        return runtimeMXBean.getSystemProperties();
    }

    public static MemoryUsage getHeapMemoryUsage() {
        return memoryMXBean.getHeapMemoryUsage();
    }

    public static MemoryUsage getNonHeapMemoryUsage() {
        return memoryMXBean.getNonHeapMemoryUsage();
    }

    public static boolean isVerbose() {
        return memoryMXBean.isVerbose();
    }

    public static void setVerbose(boolean value) {
        memoryMXBean.setVerbose(value);
    }

    public static void gc() {
        memoryMXBean.gc();
    }

    public static Object call(String methodName) {
        Method method = methodMap.get(methodName);
        if (Objects.isNull(method)) {
            method = ReflectionUtils.findMethod(JvmUtils.class, methodName);
            methodMap.put(methodName, method);
        }
        ValidateUtils.notNull(method, "方法不存在！");
        return ReflectionUtils.invokeMethod(method, null);
    }
}