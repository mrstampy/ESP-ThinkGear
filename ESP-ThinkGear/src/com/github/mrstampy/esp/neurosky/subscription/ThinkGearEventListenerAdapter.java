package com.github.mrstampy.esp.neurosky.subscription;

import com.github.mrstampy.esp.mutliconnectionsocket.event.AbstractMultiConnectionEvent;
import com.github.mrstampy.esp.neurosky.ThinkGearConstants;
import com.github.mrstampy.esp.neurosky.dsp.ThinkGearSignalAggregator;
import com.github.mrstampy.esp.neurosky.event.AbstractThinkGearEvent;
import com.github.mrstampy.esp.neurosky.event.BlinkStrengthThinkGearEvent;
import com.github.mrstampy.esp.neurosky.event.EEGPowerThinkGearEvent;
import com.github.mrstampy.esp.neurosky.event.ESenseThinkGearEvent;
import com.github.mrstampy.esp.neurosky.event.EventType;
import com.github.mrstampy.esp.neurosky.event.PoorSignalLevelThinkGearEvent;
import com.github.mrstampy.esp.neurosky.event.RawEEGThinkGearEvent;

/**
 * Convenience class that converts the {@link AbstractThinkGearEvent} to its
 * concrete subclass for processing. Override the protected methods in
 * subclasses as appropriate.
 * 
 * @author burton
 * @see ThinkGearSocketConnector
 */
public class ThinkGearEventListenerAdapter implements ThinkGearEventListener {
	protected ThinkGearSignalAggregator aggregator = new ThinkGearSignalAggregator();

	@Override
	public final void thinkGearEventPerformed(AbstractMultiConnectionEvent<EventType> e) {
		EventType eventType = e.getEventType();

		switch (eventType) {
		case blinkStrength:
			blinkStrengthEventPerformed((BlinkStrengthThinkGearEvent) e);
			break;
		case eSense:
			eSenseEventPerformed((ESenseThinkGearEvent) e);
			break;
		case eegPower:
			eegPowerEventPerformed((EEGPowerThinkGearEvent) e);
			break;
		case poorSignalLevel:
			poorSignalEventPerformed((PoorSignalLevelThinkGearEvent) e);
			break;
		case rawEeg:
			rawEEGEventPerformed((RawEEGThinkGearEvent) e);
			break;
		default:
			break;
		}
	}

	/**
	 * If raw data acquisition is enable this method returns the current seonds'
	 * worth of samples in a {@link ThinkGearConstants#SAMPLE_RATE} x
	 * {@link ThinkGearConstants#FFT_SIZE} array.
	 * 
	 * @return
	 * @see ThinkGearSignalAggregator
	 */
	public double[][] getCurrentSecondOfSampledData() {
		return aggregator.getCurrentSecondOfSampledData();
	}

	/**
	 * If raw data acquisition is enable this method returns the current seonds'
	 * worth of samples in a numSamples x {@link ThinkGearConstants#FFT_SIZE}
	 * array.
	 * 
	 * @return
	 * @see ThinkGearSignalAggregator
	 */
	public double[][] getCurrentSecondOfSampledData(int numSamples) {
		return aggregator.getCurrentSecondOfSampledData(numSamples);
	}

	protected void rawEEGEventPerformed(RawEEGThinkGearEvent e) {
		aggregator.addSample(e.getRawData());
	}

	protected void poorSignalEventPerformed(PoorSignalLevelThinkGearEvent e) {
		// blank
	}

	protected void eegPowerEventPerformed(EEGPowerThinkGearEvent e) {
		// blank
	}

	protected void eSenseEventPerformed(ESenseThinkGearEvent e) {
		// blank
	}

	protected void blinkStrengthEventPerformed(BlinkStrengthThinkGearEvent e) {
		// blank
	}

}
