package com.github.mrstampy.esp.neurosky.event;

import com.github.mrstampy.esp.neurosky.ThinkGearConstants;

/**
 * The current raw EEG output, 512 data points sampled at a rate of 100Hz. Must
 * be aggregated and processed by interested subscribers.
 * 
 * @author burton
 */
public class RawEEGThinkGearEvent extends AbstractThinkGearEvent {
	private static final long serialVersionUID = 2172951607741290171L;

	private final double[] rawData;

	public RawEEGThinkGearEvent(double[] sample) {
		super(EventType.rawEeg);
		rawData = sample;
	}

	/**
	 * Returns the the current raw signal sample [-32768, 32767], sampled at a
	 * rate of {@link ThinkGearConstants#SAMPLE_RATE}
	 * 
	 * @return the raw data value
	 */
	public double[] getRawData() {
		return rawData;
	}

}
