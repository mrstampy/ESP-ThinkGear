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
