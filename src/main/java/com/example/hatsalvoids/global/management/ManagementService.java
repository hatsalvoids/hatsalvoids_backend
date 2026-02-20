package com.example.hatsalvoids.global.management;

import com.example.hatsalvoids.global.management.model.ServerStatusResponse;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.time.OffsetDateTime;
import java.util.function.Function;

@Service
public class ManagementService {

    public ServerStatusResponse getServerStatus() {
        Runtime runtime = Runtime.getRuntime();
        long heapUsed = runtime.totalMemory() - runtime.freeMemory();
        long heapMax = runtime.maxMemory();

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        int threadCount = threadMXBean.getThreadCount();

        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long uptimeMillis = runtimeMXBean.getUptime();

        double systemCpuUsage = readSystemCpuUsage();
        double processCpuUsage = readProcessCpuUsage();

        return new ServerStatusResponse(
                OffsetDateTime.now().toString(),
                systemCpuUsage,
                processCpuUsage,
                heapUsed,
                heapMax,
                threadCount,
                uptimeMillis
        );
    }

    private double readSystemCpuUsage() {
        return readCpuUsage(com.sun.management.OperatingSystemMXBean::getSystemCpuLoad);
    }

    private double readProcessCpuUsage() {
        return readCpuUsage(com.sun.management.OperatingSystemMXBean::getProcessCpuLoad);
    }

    private double readCpuUsage(Function<com.sun.management.OperatingSystemMXBean, Double> reader) {
        java.lang.management.OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        if (osBean instanceof com.sun.management.OperatingSystemMXBean casted) {
            Double value = reader.apply(casted);
            if (value == null || value < 0) {
                return 0.0;
            }
            return value;
        }
        return 0.0;
    }
}
