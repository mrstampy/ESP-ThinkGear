package com.github.mrstampy.esp.neurosky.dsp;

import com.github.mrstampy.esp.dsp.RawSignalAggregator;
import com.github.mrstampy.esp.neurosky.ThinkGearConstants;

public class ThinkGearSignalAggregator extends RawSignalAggregator implements ThinkGearConstants {

	public ThinkGearSignalAggregator() {
		super((int) SAMPLE_RATE);
	}

}
