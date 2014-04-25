package com.github.mrstampy.esp.neurosky;

import java.io.IOException;

import com.github.mrstampy.esp.mutliconnectionsocket.MultiConnectionSocketException;
import com.github.mrstampy.esp.neurosky.event.EventType;
import com.github.mrstampy.esp.neurosky.subscription.ThinkGearEventListenerAdapter;
import com.github.mrstampy.esp.neurosky.subscription.ThinkGearSocketConnector;

public class ThinkGearTester {

	public static void main(String[] args) throws Exception {
		if(args.length == 0) {
			testLocalAggregation();
		} else {
			testRemoteAggregation();
		}
	}

	private static void testLocalAggregation() throws IOException, MultiConnectionSocketException, InterruptedException {
		System.out.println("Local Aggregation");
		MultiConnectionThinkGearSocket thinkGearSocket = new MultiConnectionThinkGearSocket("localhost");
		thinkGearSocket.setRawData(true);
		ThinkGearEventListenerAdapter adapter = new ThinkGearEventListenerAdapter();
		thinkGearSocket.addListener(adapter);

		thinkGearSocket.start();

		printSampleLengths(adapter);
	}

	private static void testRemoteAggregation() throws Exception {
		System.out.println("Remote Aggregation");
		MultiConnectionThinkGearSocket thinkGearSocket = new MultiConnectionThinkGearSocket("localhost", true);
		thinkGearSocket.setRawData(true);

		ThinkGearSocketConnector connector = new ThinkGearSocketConnector("localhost");
		ThinkGearEventListenerAdapter adapter = new ThinkGearEventListenerAdapter();
		connector.addThinkGearEventListener(adapter);

		connector.connect();
		connector.subscribe(EventType.rawEeg);

		thinkGearSocket.start();

		printSampleLengths(adapter);
	}

	private static void printSampleLengths(ThinkGearEventListenerAdapter adapter) throws InterruptedException {
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
