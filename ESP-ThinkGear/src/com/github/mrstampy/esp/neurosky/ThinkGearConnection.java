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
package com.github.mrstampy.esp.neurosky;

import java.io.IOException;

import com.github.mrstampy.esp.dsp.AbstractDSPValues;
import com.github.mrstampy.esp.dsp.EspSignalUtilities;
import com.github.mrstampy.esp.dsp.lab.AbstractRawEspConnection;
import com.github.mrstampy.esp.dsp.lab.RawEspConnection;
import com.github.mrstampy.esp.multiconnectionsocket.MultiConnectionSocketException;
import com.github.mrstampy.esp.neurosky.dsp.ThinkGearSignalUtilities;
import com.github.mrstampy.esp.neurosky.subscription.ThinkGearEventListenerAdapter;

/**
 * {@link RawEspConnection} implementation for ThinkGear
 * 
 * @author burton
 *
 */
public class ThinkGearConnection extends AbstractRawEspConnection<MultiConnectionThinkGearSocket> {

	private MultiConnectionThinkGearSocket thinkgear;

	private ThinkGearEventListenerAdapter listener = new ThinkGearEventListenerAdapter();

	public ThinkGearConnection() throws IOException {
		this(false);
	}

	public ThinkGearConnection(boolean broadcast) throws IOException {
		super();
		thinkgear = new MultiConnectionThinkGearSocket("localhost", broadcast);
		thinkgear.setRawData(true);
	}

	public void start() throws MultiConnectionSocketException {
		getSocket().addListener(listener);

		super.start();
	}

	public void stop() {
		try {
			super.stop();
		} finally {
			getSocket().removeListener(listener);
		}
	}

	@Override
	protected MultiConnectionThinkGearSocket getSocket() {
		return thinkgear;
	}

	@Override
	public EspSignalUtilities getUtilities() {
		return new ThinkGearSignalUtilities();
	}

	@Override
	public AbstractDSPValues getDSPValues() {
		return ThinkGearDSPValues.getInstance();
	}

	@Override
	public double[][] getCurrent() {
		return listener.getCurrentSecondOfSampledData();
	}

	@Override
	public double[][] getCurrent(int numSamples) {
		return listener.getCurrentSecondOfSampledData(numSamples);
	}

	@Override
	public String getName() {
		return "ESP ThinkGear";
	}

	@Override
	public double[][] getCurrentFor(int channelNumber) {
		return getCurrent();
	}

	@Override
	public double[][] getCurrentFor(int numSamples, int channelNumber) {
		return getCurrent(numSamples);
	}

}
