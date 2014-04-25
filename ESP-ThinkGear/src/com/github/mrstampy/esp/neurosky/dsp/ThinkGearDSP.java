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

import java.util.Map;
import java.util.Map.Entry;

import com.github.mrstampy.esp.dsp.EspDSP;
import com.github.mrstampy.esp.dsp.EspSignalUtilities;
import com.github.mrstampy.esp.dsp.RawProcessedListener;
import com.github.mrstampy.esp.dsp.RawSignalAggregator;
import com.github.mrstampy.esp.neurosky.MultiConnectionThinkGearSocket;
import com.github.mrstampy.esp.neurosky.ThinkGearConstants;
import com.github.mrstampy.esp.neurosky.subscription.ThinkGearEventListenerAdapter;

/**
 * {@link EspDSP} default implementation for NeuroSky ThinkGear devices.
 * 
 * @author burton
 * 
 */
public class ThinkGearDSP extends EspDSP<MultiConnectionThinkGearSocket> implements ThinkGearConstants {
	private ThinkGearEventListenerAdapter adapter = new ThinkGearEventListenerAdapter();
	private ThinkGearSignalUtilities utilities = new ThinkGearSignalUtilities();

	protected ThinkGearDSP(MultiConnectionThinkGearSocket socket, double... frequencies) {
		super(socket, (int) SAMPLE_RATE, frequencies);

		socket.addListener(adapter);
	}

	@Override
	protected void destroyImpl() {
		socket.removeListener(adapter);
	}

	@Override
	protected RawSignalAggregator getAggregator() {
		return adapter.getAggregator();
	}

	@Override
	protected EspSignalUtilities getUtilities() {
		return utilities;
	}

	/**
	 * Main method to demonstrate {@link ThinkGearDSP} use.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String... args) throws Exception {
		MultiConnectionThinkGearSocket socket = new MultiConnectionThinkGearSocket();
		socket.setRawData(true);

		final ThinkGearDSP dsp = new ThinkGearDSP(socket, 3.6, 5.4, 7.83, 10.4);
		dsp.addProcessedListener(new RawProcessedListener() {

			@Override
			public void signalProcessed() {
				showValues(dsp.getSnapshot());
			}
		});

		socket.start();
	}

	private static void showValues(Map<Double, Double> snapshot) {
		for (Entry<Double, Double> entry : snapshot.entrySet()) {
			System.out.println("Frequency: " + entry.getKey() + ", value: " + entry.getValue());
		}
		System.out.println();
	}

}
