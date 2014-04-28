package com.github.mrstampy.esp.neurosky;

import com.github.mrstampy.esp.dsp.AbstractDSPValues;

public class ThinkGearDSPValues extends AbstractDSPValues implements ThinkGearConstants {
	private static final ThinkGearDSPValues instance = new ThinkGearDSPValues();

	public static ThinkGearDSPValues getInstance() {
		return instance;
	}

	private ThinkGearDSPValues() {
		super();
	}

	@Override
	protected void initialize() {
		setSampleRate(SAMPLE_RATE);
		setSampleSize(FFT_SIZE);
	}

	public void setSampleSize(int sampleSize) {
		if (sampleSize > FFT_SIZE) {
			throw new IllegalArgumentException("Sample size must be <= 512: " + sampleSize);
		}
	}

}
