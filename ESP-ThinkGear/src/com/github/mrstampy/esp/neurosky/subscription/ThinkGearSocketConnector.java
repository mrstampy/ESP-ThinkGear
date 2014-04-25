package com.github.mrstampy.esp.neurosky.subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mrstampy.esp.mutliconnectionsocket.AbstractSocketConnector;
import com.github.mrstampy.esp.mutliconnectionsocket.event.AbstractMultiConnectionEvent;
import com.github.mrstampy.esp.neurosky.MultiConnectionThinkGearSocket;
import com.github.mrstampy.esp.neurosky.event.AbstractThinkGearEvent;
import com.github.mrstampy.esp.neurosky.event.EventType;

/**
 * This class connects to the {@link MultiConnectionThinkGearSocket} and
 * receives {@link AbstractThinkGearEvent} updates. Many instances of this class
 * running in separate processes can connect to the single instance of
 * {@link MultiConnectionThinkGearSocket}, allowing separately running programs
 * to deal with Neurosky output simultaneously. <br>
 * <br>
 * Use the
 * {@link MultiConnectionThinkGearSocket#addListener(ThinkGearEventListener)}
 * method in preference to this class if running in the same JVM as the
 * {@link MultiConnectionThinkGearSocket}.
 * 
 * @author burton
 */
public class ThinkGearSocketConnector extends AbstractSocketConnector<EventType> {
	private static final Logger log = LoggerFactory.getLogger(ThinkGearSocketConnector.class);

	private List<ThinkGearEventListener> listeners = Collections
			.synchronizedList(new ArrayList<ThinkGearEventListener>());

	/**
	 * Instantiate with the name of the host that the
	 * {@link MultiConnectionThinkGearSocket} is running on.
	 * 
	 * @param socketBroadcasterHost
	 */
	public ThinkGearSocketConnector(String socketBroadcasterHost) {
		super(socketBroadcasterHost);
	}

	/**
	 * Adds a listener for {@link AbstractThinkGearEvent}s. Listeners do not
	 * receive events unless {@link #subscribe(EventType...)} or
	 * {@link #subscribeAll()} is also called.
	 * 
	 * @param listener
	 * @see ThinkGearSocketConnector
	 */
	public void addThinkGearEventListener(ThinkGearEventListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes the specified listener.
	 * 
	 * @param listener
	 */
	public void removeThinkGearEventListener(ThinkGearEventListener listener) {
		listeners.remove(listener);
	}

	public void clearThinkGearEventListeners() {
		listeners.clear();
	}

	/**
	 * Subscribes to the specified event types, all registered
	 * {@link ThinkGearEventListener}s will receive updates for these event types.
	 * Returns true if successful.
	 * 
	 * @param types
	 */
	public boolean subscribe(EventType... types) {
		ThinkGearSubscriptionRequest request = new ThinkGearSubscriptionRequest(types);

		return subscribe(request);
	}

	/**
	 * Subscribes to all {@link EventType}s, barring
	 * {@link EventType#signalProcessed}.
	 */
	public boolean subscribeAll() {
		return subscribe(EventType.values());
	}

	/**
	 * Sends the specified message to the Neurosky socket/headset. Returns true if
	 * successful.
	 * 
	 * @param message
	 */
	public boolean sendMessage(String message) {
		if (!isConnected()) {
			log.error("Cannot send message {}, not connected", message);
			return false;
		}

		NeuroskyMessage nm = new NeuroskyMessage(message);
		connector.broadcast(nm);

		log.info("Sent message {} to Neurosky", message);

		return true;
	}

	@Override
	protected void processEvent(AbstractMultiConnectionEvent<EventType> message) {
		for (ThinkGearEventListener l : listeners) {
			l.thinkGearEventPerformed(message);
		}
	}

}
