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
