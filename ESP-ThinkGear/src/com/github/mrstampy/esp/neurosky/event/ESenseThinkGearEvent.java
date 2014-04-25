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
 * The current ESense signals.
 * 
 * @author burton
 */
public class ESenseThinkGearEvent extends AbstractThinkGearEvent {
	private static final long serialVersionUID = -4608249038627465552L;
	
	private int attention;
	private int meditation;

	public ESenseThinkGearEvent() {
		super(EventType.eSense);
	}

	/**
	 * Returns the current attention level [0, 100].
	 * 
	 * <pre>
	 * Values in [1, 20] are considered strongly lowered. 
	 * Values in [20, 40] are considered reduced levels. 
	 * Values in [40, 60] are considered neutral. 
	 * Values in [60, 80] are considered slightly elevated. 
	 * Values in [80, 100] are considered elevated.
	 * </pre>
	 */
	public int getAttention() {
		return attention;
	}

	/**
	 * Returns the current meditation level [0, 100]. The interpretation of the
	 * values is the same as for the attention level.
	 * 
	 * @return the meditation strength
	 */
	public int getMeditation() {
		return meditation;
	}

	public void setAttention(int attention) {
		this.attention = attention;
	}

	public void setMeditation(int meditation) {
		this.meditation = meditation;
	}

}
