package com.github.mrstampy.esp.neurosky.subscription;

import com.github.mrstampy.esp.mutliconnectionsocket.subscription.MultiConnectionSubscriptionRequest;
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
