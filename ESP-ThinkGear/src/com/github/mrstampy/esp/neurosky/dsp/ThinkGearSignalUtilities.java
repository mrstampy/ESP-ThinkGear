package com.github.mrstampy.esp.neurosky.dsp;

import java.math.BigDecimal;

import com.github.mrstampy.esp.dsp.EspSignalUtilities;
import com.github.mrstampy.esp.neurosky.ThinkGearConstants;

import ddf.minim.analysis.HammingWindow;

public class ThinkGearSignalUtilities extends EspSignalUtilities implements ThinkGearConstants {

	private static final BigDecimal SIGNAL_BREADTH = new BigDecimal(HIGHEST_SIGNAL_VAL - LOWEST_SIGNAL_VAL);

	public ThinkGearSignalUtilities() {
		super(new HammingWindow());
	}

	@Override
	protected int getFFTSize() {
		return FFT_SIZE;
	}

	@Override
	protected double getSampleRate() {
		return SAMPLE_RATE;
	}

	@Override
	protected BigDecimal getRawSignalBreadth() {
		return SIGNAL_BREADTH;
	}

}
