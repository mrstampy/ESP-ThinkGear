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
 * The current strength of the blink signal.
 * 
 * @author burton
 */
public class BlinkStrengthThinkGearEvent extends AbstractThinkGearEvent {
	private static final long serialVersionUID = -7822399592077342790L;
	
	private int blinkStrength;

	public BlinkStrengthThinkGearEvent() {
		super(EventType.blinkStrength);
	}

	/**
	 * Unsigned one byte value reports the intensity of the user's most recent eye
	 * blink. Its value ranges from 1 to 255 and it is reported whenever an eye
	 * blink is detected. The value indicates the relative intensity of the blink,
	 * and has no units.
	 * 
	 * @return the blink strength
	 */
	public int getBlinkStrength() {
		return blinkStrength;
	}

	public void setBlinkStrength(int blinkStrength) {
		this.blinkStrength = blinkStrength;
	}

}
