package com.github.mrstampy.esp.neurosky;

import com.github.mrstampy.esp.neurosky.subscription.ThinkGearEventListenerAdapter;

public class ThinkGearTester {

	public static void main(String[] args) throws Exception {
		MultiConnectionThinkGearSocket thinkGearSocket = new MultiConnectionThinkGearSocket("localhost");
		thinkGearSocket.setRawData(true);
		ThinkGearEventListenerAdapter adapter = new ThinkGearEventListenerAdapter();
		thinkGearSocket.addListener(adapter);

		thinkGearSocket.start();

		while (true) {
			Thread.sleep(1000);
			double[][] sampled = adapter.getCurrentSecondOfSampledData();

			int length = sampled.length;
			if (length > 0) {
				System.out.println(length);
				System.out.println(sampled[0].length);
			}
		}
	}

}
