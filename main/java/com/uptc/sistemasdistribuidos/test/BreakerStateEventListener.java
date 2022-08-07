package com.uptc.sistemasdistribuidos.test;

public interface BreakerStateEventListener {
	public void onCircuitBreakerStateChangeEvent(CircuitBreakerStateChangeEvent e);
}
