package com.uptc.sistemasdistribuidos.test;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

public class CircuitBreakerConfigTest {

	/**
	 * Directly copy key and values from README.md
	 */
	@Test
	public void testDefaults() {
		CircuitBreakerConfig config = new CircuitBreakerConfig();
		assertEquals(config.getName(), "");
		assertTrue(config.getFailureRateThreshold() == 50f);
		assertTrue(config.getSlowCallRateThreshold() == 100f);
		assertEquals(config.getSlowCallDurationThreshold(), 60000);
		assertEquals(config.getPermittedNumberOfCallsInHalfOpenState(), 10);
		assertEquals(config.getSlidingWindowSize(), 100);
		assertEquals(config.getMinimumNumberOfCalls(), 10);
		assertEquals(config.getWaitDurationInOpenState(), 60000);
		assertEquals(config.getMaxDurationOpenInHalfOpenState(), 120000);
		String str = config.toString() + ",";
		assertTrue(str.indexOf("name:,") != -1);
		assertTrue(str.indexOf("failureRateThreshold:50.0,") != -1);
		assertTrue(str.indexOf("slowCallRateThreshold:100.0,") != -1);
		assertTrue(str.indexOf("slowCallDurationThreshold:60000,") != -1);
		assertTrue(str.indexOf("permittedNumberOfCallsInHalfOpenState:10,") != -1);
		assertTrue(str.indexOf("slidingWindowSize:100,") != -1);
		assertTrue(str.indexOf("minimumNumberOfCalls:10,") != -1);
		assertTrue(str.indexOf("waitDurationInOpenState:60000,") != -1);
		assertTrue(str.indexOf("maxDurationOpenInHalfOpenState:120000,") != -1);
		assertEquals(TestUtils.countChars(str, ','), 9); //9 variables
	}
	
	@Test
	public void testConfigFromProperties() throws Exception {
		Properties props = new Properties();
		InputStream is = CircuitBreakerConfigTest.class.getResourceAsStream("/configTest.config");
		props.load(is);
		is.close();
		CircuitBreakerConfig config = new CircuitBreakerConfig(props);
		System.out.println("round 1: load from properties");
		for(int i = 0; i<1; i++) {
			assertEquals(config.getName(), "TEST");
			assertTrue(config.getFailureRateThreshold() == 6f);
			assertTrue(config.getSlowCallRateThreshold() == 7f);
			assertEquals(config.getSlowCallDurationThreshold(), 4);
			assertEquals(config.getPermittedNumberOfCallsInHalfOpenState(), 3);
			assertEquals(config.getSlidingWindowSize(), 2);
			assertEquals(config.getMinimumNumberOfCalls(), 5);
			assertEquals(config.getWaitDurationInOpenState(), 8);
			assertEquals(config.getMaxDurationOpenInHalfOpenState(), 9);
			CircuitBreaker breaker = new CircuitBreaker(config);
			config = breaker.getCircuitBreakerConfig();
			System.out.println("Round 2: from circuit breaker, check clone is ok");
		}
	}
	
	@Test
	public void testConfigFromPropertiesWithPrefix() throws Exception {
		Properties props = new Properties();
		InputStream is = CircuitBreakerConfigTest.class.getResourceAsStream("/configTest.config");
		props.load(is);
		is.close();
		CircuitBreakerConfig config = new CircuitBreakerConfig("PREFIX.", props);
		assertEquals(config.getName(), "TEST_PREFIX");
		assertTrue(config.getFailureRateThreshold() == 60f);
		assertTrue(config.getSlowCallRateThreshold() == 70f);
		assertEquals(config.getSlowCallDurationThreshold(), 40);
		assertEquals(config.getPermittedNumberOfCallsInHalfOpenState(), 30);
		assertEquals(config.getSlidingWindowSize(), 20);
		assertEquals(config.getMinimumNumberOfCalls(), 50);
		assertEquals(config.getWaitDurationInOpenState(), 80);
		assertEquals(config.getMaxDurationOpenInHalfOpenState(), 90);
	}

}
