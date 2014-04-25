package com.github.mrstampy.esp.neurosky;

public interface ThinkGearConstants {

	public static final double SAMPLE_RATE = 100;
	public static final double SAMPLE_SLEEP = 1000 / SAMPLE_RATE;

	public static final int FFT_SIZE = 512;
	
	public static final double LOWEST_SIGNAL_VAL = -32768;
	public static final double HIGHEST_SIGNAL_VAL = 32767;
	
	public static final int NEUROSOCKET_PORT = 13854;

	/**
	 * System property 'acquire.raw.data' to control whether raw data is obtained
	 * from the device. Defaults to false.
	 */
	public static final String RAW_DATA_KEY = "acquire.raw.data";

	public static final String LOCAL_HOST = "127.0.0.1";

	// ESense
	public static final String MEDITATION = "meditation";
	public static final String ATTENTION = "attention";

	// EEG Power
	public static final String HIGH_GAMMA = "highGamma";
	public static final String LOW_GAMMA = "lowGamma";
	public static final String HIGH_BETA = "highBeta";
	public static final String LOW_BETA = "lowBeta";
	public static final String HIGH_ALPHA = "highAlpha";
	public static final String LOW_ALPHA = "lowAlpha";
	public static final String THETA = "theta";
	public static final String DELTA = "delta";
	
	/**
	 * Set this as a system property on startup ie. -Dsend.neurosky.messages=true
	 * to enable remote programs to send messages to the device. Defaults to
	 * false, will cause a disconnection if the attempt is made when false.
	 */
	public static final String SEND_NEUROSKY_MESSAGES = "send.neurosky.messages";

	/**
	 * System property key to control whether broadcasting has been enabled. False
	 * by default. Use '-Dbroadcast.messages=true' to enable socket broadcasting.
	 */
	public static final String BROADCAST_MESSAGES = "broadcast.messages";
	
}
