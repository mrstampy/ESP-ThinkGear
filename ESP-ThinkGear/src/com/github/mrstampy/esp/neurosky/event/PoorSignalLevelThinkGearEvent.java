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
 * The current poor signal level value.
 * 
 * @author burton
 */
public class PoorSignalLevelThinkGearEvent extends AbstractThinkGearEvent {
	private static final long serialVersionUID = -3183510611030991676L;

	private static final int NOT_TOUCHING_SKIN = 200;

	private int poorSignalLevel;

	public PoorSignalLevelThinkGearEvent() {
		super(EventType.poorSignalLevel);
	}

	/**
	 * Returns the signal level [0, 200]. The greater the value, the more noise is
	 * detected in the signal. 200 is a special value that means that the
	 * ThinkGear contacts are not touching the skin.
	 * 
	 * @return the poor signal level value
	 */
	public int getPoorSignalLevel() {
		return poorSignalLevel;
	}

	public void setPoorSignalLevel(int poorSignalLevel) {
		this.poorSignalLevel = poorSignalLevel;
	}

	public boolean isTouchingSkin() {
		return !isNotTouchingSkin();
	}

	public boolean isNotTouchingSkin() {
		return getPoorSignalLevel() == NOT_TOUCHING_SKIN;
	}

}
