package com.uptc.sistemasdistribuidos.test;

interface BreakerStateInterface {
	boolean isClosedForThisCall();
    void callFailed(long callDuration);
    void callSucceeded(long callDuration);
    BreakerStateType getBreakerStateType();
}