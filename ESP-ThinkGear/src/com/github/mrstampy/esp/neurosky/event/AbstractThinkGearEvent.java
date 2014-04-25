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

import com.github.mrstampy.esp.mutliconnectionsocket.event.AbstractMultiConnectionEvent;

/**
 * Abstract event for Neurosky output
 * 
 * @author burton
 */
public abstract class AbstractThinkGearEvent extends AbstractMultiConnectionEvent<EventType> {
	private static final long serialVersionUID = 8617707735332644041L;

	protected AbstractThinkGearEvent(EventType type) {
		super(type);
	}

}
