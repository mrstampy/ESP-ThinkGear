package com.github.mrstampy.esp.neurosky;

import java.util.concurrent.ArrayBlockingQueue;

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
