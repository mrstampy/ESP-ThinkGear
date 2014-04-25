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
