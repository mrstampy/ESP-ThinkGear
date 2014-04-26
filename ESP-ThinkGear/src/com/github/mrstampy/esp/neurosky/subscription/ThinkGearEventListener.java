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
package com.github.mrstampy.esp.neurosky.subscription;

import java.util.EventListener;

import com.github.mrstampy.esp.multiconnectionsocket.event.AbstractMultiConnectionEvent;
import com.github.mrstampy.esp.neurosky.event.AbstractThinkGearEvent;
import com.github.mrstampy.esp.neurosky.event.EventType;

/**
 * Implemented by subscribers interested in receiving
 * {@link AbstractThinkGearEvent} updates.
 * 
 * @author burton
 * @see ThinkGearEventListenerAdapter
 * @see ThinkGearSocketConnector
 */
public interface ThinkGearEventListener extends EventListener {

	void thinkGearEventPerformed(AbstractMultiConnectionEvent<EventType> e);
}
