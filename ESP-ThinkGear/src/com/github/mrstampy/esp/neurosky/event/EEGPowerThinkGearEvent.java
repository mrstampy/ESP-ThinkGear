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
package com.github.mrstampy.esp.neurosky.event;

/**
 * The current EEG power signals.
 * 
 * <pre>
 * delta (0.5 - 2.75Hz)
 * theta (3.5 - 6.75Hz)
 * low-alpha (7.5 - 9.25Hz)
 * high-alpha (10 - 11.75Hz)
 * low-beta (13 - 16.75Hz)
 * high-beta (18 - 29.75Hz)
 * low-gamma (31 - 39.75Hz)
 * high-gamma (41 - 49.75Hz)
 * </pre>
 * 
 * These values have no units and therefore are only meaningful compared to each
 * other and to themselves, to consider relative quantity and temporal
 * fluctuations. Output at a frequency of 1Hz.
 * 
 * @author burton
 */
public class EEGPowerThinkGearEvent extends AbstractThinkGearEvent {
	private static final long serialVersionUID = -6099421377338886902L;
	
	private double delta;
	private double theta;
	private double lowAlpha;
	private double highAlpha;
	private double lowBeta;
	private double highBeta;
	private double lowGamma;
	private double highGamma;

	public EEGPowerThinkGearEvent() {
		super(EventType.eegPower);
	}

	public double getDelta() {
		return delta;
	}

	public double getTheta() {
		return theta;
	}

	public double getLowAlpha() {
		return lowAlpha;
	}

	public double getHighAlpha() {
		return highAlpha;
	}

	public double getLowBeta() {
		return lowBeta;
	}

	public double getHighBeta() {
		return highBeta;
	}

	public double getLowGamma() {
		return lowGamma;
	}

	public double getHighGamma() {
		return highGamma;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}

	public void setTheta(double theta) {
		this.theta = theta;
	}

	public void setLowAlpha(double lowAlpha) {
		this.lowAlpha = lowAlpha;
	}

	public void setHighAlpha(double highAlpha) {
		this.highAlpha = highAlpha;
	}

	public void setLowBeta(double lowBeta) {
		this.lowBeta = lowBeta;
	}

	public void setHighBeta(double highBeta) {
		this.highBeta = highBeta;
	}

	public void setLowGamma(double lowGamma) {
		this.lowGamma = lowGamma;
	}

	public void setHighGamma(double highGamma) {
		this.highGamma = highGamma;
	}

	/**
	 * Lowest (Theta) to highest (High Gamma)
	 * @param e
	 * @return
	 */
	public double[] getPowers() {
		//@formatter:off
		return new double[] { 
				getDelta(), 
				getTheta(), 
				getLowAlpha(), 
				getHighAlpha(), 
				getLowBeta(),
				getHighBeta(), 
				getLowGamma(), 
				getHighGamma() };
		//@formatter:on
	}

}
