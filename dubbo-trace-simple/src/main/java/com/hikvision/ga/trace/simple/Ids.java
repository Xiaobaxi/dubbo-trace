package com.hikvision.ga.trace.simple;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author fangzhibin
 *
 */
public class Ids {

	public static final int TOTAL_BITS_LENGTH = 63;

	public static final int TIME_BITS_LENGTH = 41;

	public static final int NODE_BITS_LENGTH = 10;

	private static final int COUNT_BITS_LENGTH = 12;

	private static final long TIME_BITS_MASK = (1L << TIME_BITS_LENGTH) - 1L;

	private static final int TIME_BITS_SHIFT_SIZE = TOTAL_BITS_LENGTH - TIME_BITS_LENGTH;

	private static final int NODE_BITS_MASK = (1 << NODE_BITS_LENGTH) - 1;

	private static final int MAX_COUNTER = 1 << COUNT_BITS_LENGTH;

	private int nodeId;

	private AtomicInteger counter;

	private long lastMillisecond;

	private static Ids instance = new Ids();

	private Ids() {
		this.nodeId = new Random().nextInt(1023) + 1;
		this.counter = new AtomicInteger(0);
	}

	public static long get() {
		long id = 0;
		try {
			id = instance.nextTicket();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		if (id == 0) {
			try {
				Thread.sleep(3);
				id = instance.nextTicket();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (id == 0) {
			return System.currentTimeMillis() + (int) Math.random() * 10000;
		} else {
			return id;
		}
	}

	private synchronized long nextTicket() {
		long currentMillisecond = System.currentTimeMillis();
		if (currentMillisecond < lastMillisecond) {
			throw new RuntimeException("time is out of sync by " + (lastMillisecond - currentMillisecond) + "ms");
		}
		long ts = currentMillisecond & TIME_BITS_MASK;

		ts = ts << TIME_BITS_SHIFT_SIZE;

		if (currentMillisecond == lastMillisecond) {
			int count = counter.incrementAndGet();
			if (count >= MAX_COUNTER) {
				throw new RuntimeException("too much requests cause counter overflow");
			}
		} else {
			this.counter.set(0);
		}

		int node = (nodeId & NODE_BITS_MASK) << COUNT_BITS_LENGTH;

		lastMillisecond = currentMillisecond;
		return ts + node + counter.get();
	}

	public static long timeStartId(long timeMs) {
		long ts = timeMs & TIME_BITS_MASK;
		ts = ts << TIME_BITS_SHIFT_SIZE;
		return ts;
	}
}
