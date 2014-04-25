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

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Sample buffer for ThinkGear raw data with a capacity of 512 data points.
 * 
 * @author burton
 * 
 */
public class SampleBuffer implements ThinkGearConstants {

	private ArrayBlockingQueue<Double> queue = new ArrayBlockingQueue<Double>(FFT_SIZE);

	public void addSample(double sample) {
		if (queue.remainingCapacity() == 0) queue.remove();

		queue.add(sample);
	}

	public double[] getSnapshot() {
		Double[] snap = queue.toArray(new Double[] {});

		double[] shot = new double[FFT_SIZE];

		for (int i = 0; i < snap.length; i++) {
			shot[i] = snap[i];
		}

		return shot;
	}
}
