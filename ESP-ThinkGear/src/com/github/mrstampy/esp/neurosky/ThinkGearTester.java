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

import com.github.mrstampy.esp.multiconnectionsocket.AbstractSocketConnector;
import com.github.mrstampy.esp.multiconnectionsocket.MultiConnectionSocketException;
import com.github.mrstampy.esp.neurosky.event.EventType;
import com.github.mrstampy.esp.neurosky.subscription.ThinkGearEventListenerAdapter;
import com.github.mrstampy.esp.neurosky.subscription.ThinkGearSocketConnector;

/**
 * Main class to demonstrate the use of local and remote notifications from the
 * {@link MultiConnectionThinkGearSocket}.
 * 
 * @author burton
 * 
 */
public class ThinkGearTester {

	/**
	 * No args == {@link #testLocalAggregation()}, any args ==
	 * {@link #testRemoteAggregation()}
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			testLocalAggregation();
		} else {
			testRemoteAggregation();
		}
	}

	/**
	 * Demonstrates local raw data acquisition
	 * 
	 * @throws IOException
	 * @throws MultiConnectionSocketException
	 * @throws InterruptedException
	 */
	protected static void testLocalAggregation() throws IOException, MultiConnectionSocketException, InterruptedException {
		System.out.println("Local Aggregation");
		MultiConnectionThinkGearSocket thinkGearSocket = new MultiConnectionThinkGearSocket("localhost");
		thinkGearSocket.setRawData(true);
		ThinkGearEventListenerAdapter adapter = new ThinkGearEventListenerAdapter();
		thinkGearSocket.addListener(adapter);

		thinkGearSocket.start();

		printSampleLengths(adapter, thinkGearSocket);
	}

	/**
	 * Connects to the {@link MultiConnectionThinkGearSocket} on the default port
	 * (12345) to receive raw data events.
	 * 
	 * @throws Exception
	 * @see {@link AbstractSocketConnector#SOCKET_BROADCASTER_KEY}
	 */
	protected static void testRemoteAggregation() throws Exception {
		System.out.println("Remote Aggregation");
		MultiConnectionThinkGearSocket thinkGearSocket = new MultiConnectionThinkGearSocket("localhost", true);
		thinkGearSocket.setRawData(true);

		ThinkGearSocketConnector connector = new ThinkGearSocketConnector("localhost");
		ThinkGearEventListenerAdapter adapter = new ThinkGearEventListenerAdapter();
		connector.addThinkGearEventListener(adapter);

		connector.connect();
		connector.subscribe(EventType.rawEeg);

		thinkGearSocket.start();

		printSampleLengths(adapter, thinkGearSocket);
	}

	private static void printSampleLengths(ThinkGearEventListenerAdapter adapter, MultiConnectionThinkGearSocket socket)
			throws InterruptedException {
		boolean tuning = false;
		int cntr = 0;
		while (true) {
			Thread.sleep(1000);
			cntr++;
			if (!tuning && cntr > 4) {
				tuning = true;
				socket.tune();
			}
			double[][] sampled = adapter.getCurrentSecondOfSampledData();

			int length = sampled.length;
			if (length > 0) {
				System.out.println(length);
				System.out.println(sampled[0].length);
			}
		}
	}

}
