package io.github.xiaobaxi.trace.core.utils;

import java.util.Random;

/**
 * 
 * @author fangzhibin
 *
 */
public class SimpleUtils {

	private static final String LOCAL_ADDR = "127.0.0.1";

	private final Random random = new Random();

	private static final class SimpleUtilsHolder {
		private static final SimpleUtils INSTANCE = new SimpleUtils();
	}

	public static SimpleUtils getInstance() {
		return SimpleUtilsHolder.INSTANCE;
	}

	private SimpleUtils() {
	}

	public long getId() {
		return random.nextLong();
	}

	public Integer ip2Num(String ipStr) {
		if (ipStr == null || "".equals(ipStr)) {
			return -1;
		}

		if (ipStr.contains(":")) {
			ipStr = LOCAL_ADDR;
		}

		String[] ips = ipStr.split("\\.");

		return (Integer.parseInt(ips[0]) << 24) + (Integer.parseInt(ips[1]) << 16) + (Integer.parseInt(ips[2]) << 8)
				+ Integer.parseInt(ips[3]);
	}

}
