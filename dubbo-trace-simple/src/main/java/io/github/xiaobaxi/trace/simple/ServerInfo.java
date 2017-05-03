package com.hikvision.ga.trace.simple;

import java.lang.management.ManagementFactory;

/**
 * 
 * @author fangzhibin
 *
 */
public class ServerInfo {

	public static final int IP4 = Networks.ip2Num(Networks.getSiteIp());

	public static final int PID = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
}
