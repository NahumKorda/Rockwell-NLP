/*
 *
 * Copyright 2020 IT Consulting AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.itcag.util;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * <p>Provides access to the system variables that provide data about system resource usage.</p>
 */
public final class SystemStats {

    /**
     * @return Number between 0.00 and 1.00 indicating CPU use percentage.
     */
    public final static Double getCpuUsage() {

        try {

            MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
            ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
            AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });
        
            if (list.isEmpty()) return Double.NaN;

            // usually takes a couple of seconds before we get real values
            for (int i = 0; i < 5; i++) {
                
                Attribute att = (Attribute) list.get(0);
                Double value = (Double) att.getValue();

                if (value != -1.0) {
                    BigDecimal retVal = new BigDecimal(value);
                    retVal = retVal.setScale(2, RoundingMode.HALF_UP);
                    return retVal.doubleValue();
                }

                Thread.sleep(1000);
                
            }

            return Double.NaN;

        } catch (Exception ex) {
            return Double.NaN;
        }

    }

    /**
     * @return Double number indicating total memory allocated to JVM.
     */
    public final static double getJVMMemory() {
        return (double) Runtime.getRuntime().maxMemory() / (1024 * 1024);
    }
    
    /**
     * @return Double number indicating available JVM memory in MB.
     */
    public final static double getFreeJVMMemory() {
        return (double) Runtime.getRuntime().freeMemory() / (1024 * 1024);
    }
    
    /**
     * @return Number between 0.00 and 1.00 indicating JVM memory use percentage.
     */
    public final static Double getJVMMemoryUsage() {

        double max = (double) Runtime.getRuntime().maxMemory() / (1024 * 1024);
        double free = (double) Runtime.getRuntime().freeMemory() / (1024 * 1024);
        
        double used = max - free;
        
        BigDecimal retVal = new BigDecimal((double) used / max);
        retVal = retVal.setScale(2, RoundingMode.HALF_UP);
        
        return retVal.doubleValue();

    }
    
    /**
     * @return Double number indicating total machine memory.
     */
    public final static double getMachineMemory() {
        OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return (double) bean.getTotalPhysicalMemorySize() / (1024 * 1024);
    }
    
    /**
     * @return Double number indicating available machine memory.
     */
    public final static double getFreeMachineMemory() {
        OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return (double) bean.getFreePhysicalMemorySize() / (1024 * 1024);
    }
    
    /**
     * @return Number between 0.00 and 1.00 indicating machine memory use percentage.
     */
    public final static Double getMachineMemoryUsage() {

        OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double max = (double) bean.getTotalPhysicalMemorySize() / (1024 * 1024);
        double free = (double) bean.getFreePhysicalMemorySize() / (1024 * 1024);
        
        double used = max - free;
        
        BigDecimal retVal = new BigDecimal((double) used / max);
        retVal = retVal.setScale(2, RoundingMode.HALF_UP);
        
        return retVal.doubleValue();

    }

}
