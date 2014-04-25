/*
 * ESP-ThinkGear Copyright (C) 2014 Burton Alexander
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * 
 */
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
