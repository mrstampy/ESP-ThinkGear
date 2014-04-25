package com.github.mrstampy.esp.neurosky.event;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * delta (0.5 - 2.75Hz)
 * theta (3.5 - 6.75Hz)
 * low-alpha (7.5 - 9.25Hz)
 * high-alpha (10 - 11.75Hz)
 * low-beta (13 - 16.75Hz)
 * high-beta (18 - 29.75Hz)
 * low-gamma (31 - 39.75Hz)
 * high-gamma (41 - 49.75Hz)
 * 
 * This class encapsulates the ranges measured by the Neurosky signal processor, 
 * and the ranges in between.
 * 
 * @author burton
 *
 */
public enum EEGFrequencyRange {
	
	SUB_DELTA(0, 0.49, false, "Sub Delta"),
	DELTA(0.5, 2.75, true, "Delta"),
	DELTA_PLUS(2.76, 3.49, false, "Delta Plus"),
	THETA(3.5, 6.75, true, "Theta"),
	THETA_PLUS(6.76, 7.49, false, "Theta Plus"),
	LOW_ALPHA(7.5, 9.25, true, "Low Alpha"),
	LOW_ALPHA_PLUS(9.26, 9.99, false, "Low Alpha Plus"),
	HIGH_ALPHA(10, 11.75, true, "High Alpha"),
	HIGH_ALPHA_PLUS(11.76, 12.99, false, "High Alpha Plus"),
	LOW_BETA(13, 16.75, true, "Low Beta"),
	LOW_BETA_PLUS(16.76, 17.99, false, "Low Beta Plus"),
	HIGH_BETA(18, 29.75, true, "High Beta"),
	HIGH_BETA_PLUS(29.76, 30.99, false, "High Beta Plus"),
	LOW_GAMMA(31, 39.75, true, "Low Gamma"),
	LOW_GAMMA_PLUS(39.76, 40.99, false, "Low Gamma Plus"),
	HIGH_GAMMA(41, 49.75, true, "High Gamma");

	double low, high;
	boolean measurableRange;
	String desc;
	EEGFrequencyRange(double low, double high, boolean measurableRange, String desc) {
		this.low = low;
		this.high = high;
		this.measurableRange = measurableRange;
		this.desc = desc;
	}
	
	/**
	 * Returns the low end of the range.
	 * 
	 * @return
	 */
	public double getLow() {
		return low;
	}
	
	/**
	 * Returns the high end of the range.
	 * 
	 * @return
	 */
	public double getHigh() {
		return high;
	}

	/**
	 * Returns true if the range is measurable.
	 * 
	 * @return
	 */
	public boolean isMeasurableRange() {
		return measurableRange;
	}

	/**
	 * Returns a description of the range.
	 * 
	 * @return
	 */
	public String getDesc() {
		return desc;
	}

	public static EEGFrequencyRange getFrequencyRange(double value) {
		double rounded = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
		
		for(EEGFrequencyRange eegPower : values()) {
			if(eegPower.getLow() <= rounded && eegPower.getHigh() >= rounded) return eegPower;
		}
		
		return null;
	}

}
