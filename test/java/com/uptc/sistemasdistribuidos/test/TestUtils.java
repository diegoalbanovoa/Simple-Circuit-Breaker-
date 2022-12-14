package com.uptc.sistemasdistribuidos.test;

import static org.junit.Assert.assertEquals;

public class TestUtils {

	public static void sleep(long ms) {
		if(ms <= 0)
			return;
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isCountStatsEqual(CountStats expectedCS, CountStats actualCS) {
		if(expectedCS.callCount != actualCS.callCount ||
				expectedCS.failureCallCount != actualCS.failureCallCount ||
				expectedCS.slowCallDurationCount != actualCS.slowCallDurationCount) {
    		System.out.println("expected callCount: " + expectedCS.callCount + ", failureCallCount: " + expectedCS.failureCallCount + ", slowCallDurationCount: " + expectedCS.slowCallDurationCount);
    		System.out.println("actual   callCount: " + actualCS.callCount + ", failureCallCount: " + actualCS.failureCallCount + ", slowCallDurationCount: " + actualCS.slowCallDurationCount);
			return false;
		}
		return true;
	}
	
	/**
	 * The code will lock the circuitBreaker for the duration of the validation
	 * @param circuitBreaker
	 * @return
	 */
	public static boolean validateAggregatedCountStatsMatches(CircuitBreaker circuitBreaker) {
		synchronized(circuitBreaker) {
			assertEquals(circuitBreaker.getBreakerState().getBreakerStateType(), BreakerStateType.CLOSED);
			BreakerClosedState breakerClosedState = (BreakerClosedState)circuitBreaker.getBreakerState();
			CountStats agrregatedCountStats = breakerClosedState.calculateAggregatedCountStatsForUnitTest();
			return isCountStatsEqual(agrregatedCountStats, breakerClosedState.getCountStats());
		}
	}
	
	/**
	 * The code will lock the circuitBreaker for the duration of the validation
	 * @param circuitBreaker
	 * @return
	 */
	public static boolean validateAggregatedCountStatsMatches(CircuitBreaker circuitBreaker,
			int expectedCallCount, int expectedFailureCallCount, int expectedSlowCallDurationCount) {
		synchronized(circuitBreaker) {
			assertEquals(circuitBreaker.getBreakerState().getBreakerStateType(), BreakerStateType.CLOSED);
			BreakerClosedState breakerClosedState = (BreakerClosedState)circuitBreaker.getBreakerState();
			//step 1: ensure that sum(all buckets) = current countStats
			CountStats agrregatedCountStats = breakerClosedState.calculateAggregatedCountStatsForUnitTest();
			boolean checkAggregated = isCountStatsEqual(agrregatedCountStats, breakerClosedState.getCountStats());
			if(!checkAggregated) {
				System.out.println("sum(all buckets) is different from current countStats");
				return false;
			}
			//step 2: all passed values match current countStats
			CountStats expectedCountStats = new CountStats();
			expectedCountStats.callCount = expectedCallCount;
			expectedCountStats.failureCallCount = expectedFailureCallCount;
			expectedCountStats.slowCallDurationCount = expectedSlowCallDurationCount;
			return isCountStatsEqual(expectedCountStats, breakerClosedState.getCountStats());
		}
	}
	
	public static int countChars(String s, char c) {
		if(s == null)
			return 0;
		int l = s.length();
		int cnt = 0;
		for(int i = 0; i<l; i++)
			if(s.charAt(i) == c)
				cnt++;
		return cnt;
	}
	
	public static void outputJVMInfo() {
		System.out.println("java.vendor:          " + System.getProperty("java.vendor"));
		System.out.println("java.version:         " + System.getProperty("java.version"));
		System.out.println("java.runtime.version: " + System.getProperty("java.runtime.version"));
	}
}