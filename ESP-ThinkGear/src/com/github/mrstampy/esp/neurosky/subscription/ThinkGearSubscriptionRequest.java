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

import com.github.mrstampy.esp.multiconnectionsocket.subscription.MultiConnectionSubscriptionRequest;
import com.github.mrstampy.esp.neurosky.event.EventType;

/**
 * Class to encapsulate {@link EventType}s for which to receive updates.
 * 
 * @author burton
 * @see ThinkGearSocketConnector
 */
public class ThinkGearSubscriptionRequest implements MultiConnectionSubscriptionRequest<EventType> {
	private static final long serialVersionUID = -2725177289416323383L;
	
	private final EventType[] eventTypes;

	public ThinkGearSubscriptionRequest(EventType... eventTypes) {
		this.eventTypes = eventTypes;
	}

	public EventType[] getEventTypes() {
		return eventTypes;
	}

	@Override
	public boolean containsEventType(EventType eventType) {
		for (EventType et : getEventTypes()) {
			if (et.equals(eventType)) return true;
		}

		return false;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();

		boolean mt = true;
		for (EventType et : getEventTypes()) {
			if (!mt) builder.append(", ");
			builder.append(et);
			mt = false;
		}

		return builder.toString();
	}

}
