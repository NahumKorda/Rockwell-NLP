package com.itcag.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

/**
 * <p>This class systemouts messages, exceptions, system stats.</p>
 */
public final class Printer {

    /**
     * Systemout empty line.
     */
    public final static void print() {
        System.out.println();
    }
    
    /**
     * @param msg String to be systemouted.
     */
    public final static void print(String msg) {
        System.out.println(msg);
    }
    
    /**
     * @param msg String builder holding the string to be systemouted.
     */
    public final static void print(StringBuilder msg) {
        System.out.println(msg.toString());
    }

    /**
     * @param ex Exception to be systemouted.
     */
    public final static void printException(Exception ex) {
        print(ex.getClass().getName());
        print("\t" + ex.getMessage());
        print("Stack trace:" + "\n");
        LinkedList<StackTraceElement> trace = new LinkedList<>(Arrays.asList(ex.getStackTrace()));
        Collections.reverse(trace);
        trace.stream().forEach((elt) -> {
            print("\t" + elt.toString());
        });
        print("");
    }

    /**
     * Systemouts memory stats.
     */
    public final static void printMemoryInfo() {

        print("Runtime max:    " + format(Runtime.getRuntime().maxMemory()));
        print("Free:           " + format(Runtime.getRuntime().freeMemory()));

        
        MemoryMXBean m = ManagementFactory.getMemoryMXBean();

        print("Heap:           " + format(m.getHeapMemoryUsage().getMax()));
        print("Non-heap:       " + format(m.getNonHeapMemoryUsage().getMax()));

        for (MemoryPoolMXBean mp : ManagementFactory.getMemoryPoolMXBeans()) {
            print("Pool:           " + mp.getName() + " (type " + mp.getType() + ")" + " = " + format(mp.getUsage().getMax()));
        }

    }
    
    /**
     * Systemouts memory usage.
     */
    public final static void printMemoryUsage() {

        print("Runtime max:    " + format(Runtime.getRuntime().maxMemory()));
        print("Free:           " + format(Runtime.getRuntime().freeMemory()));

        
        MemoryMXBean m = ManagementFactory.getMemoryMXBean();

        print("Heap used:      " + format(m.getHeapMemoryUsage().getUsed()));
        print("Heap free:      " + format(m.getHeapMemoryUsage().getMax() - m.getHeapMemoryUsage().getUsed()));
        print("Non-heap used:  " + format(m.getNonHeapMemoryUsage().getUsed()));
        print("Non-heap free:  " + format(m.getNonHeapMemoryUsage().getMax() - m.getNonHeapMemoryUsage().getUsed()));

        for (MemoryPoolMXBean mp : ManagementFactory.getMemoryPoolMXBeans()) {
            print("Pool:           " + mp.getName() + " (type " + mp.getType() + ")" + " = " + format(mp.getUsage().getUsed()));
        }

    }
    
    private static String format(long s) {
        return String.format("%d (%.2f M)", s, (double)s / (1024 * 1024));
    }

}
