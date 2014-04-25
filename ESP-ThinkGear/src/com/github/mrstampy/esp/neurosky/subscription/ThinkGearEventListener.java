package com.github.mrstampy.esp.neurosky.subscription;

import java.util.EventListener;

import com.github.mrstampy.esp.mutliconnectionsocket.event.AbstractMultiConnectionEvent;
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
