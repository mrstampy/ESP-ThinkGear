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
package com.github.mrstampy.esp.neurosky;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mrstampy.esp.multiconnectionsocket.AbstractSubscriptionHandlerAdapter;
import com.github.mrstampy.esp.neurosky.event.AbstractThinkGearEvent;
import com.github.mrstampy.esp.neurosky.event.EventType;
import com.github.mrstampy.esp.neurosky.subscription.NeuroskyMessage;
import com.github.mrstampy.esp.neurosky.subscription.ThinkGearSocketConnector;
import com.github.mrstampy.esp.neurosky.subscription.ThinkGearSubscriptionRequest;

/**
 * This class is used by the {@link MultiConnectionThinkGearSocket} as the
 * adapter for {@link AbstractThinkGearEvent} broadcasting.
 * 
 * @author burton
 * @see MultiConnectionThinkGearSocket
 * @see ThinkGearSocketConnector
 */
public class SubscriptionHandlerAdapter extends
		AbstractSubscriptionHandlerAdapter<EventType, MultiConnectionThinkGearSocket, ThinkGearSubscriptionRequest> {
	
	private static final Logger log = LoggerFactory.getLogger(SubscriptionHandlerAdapter.class);

	private final boolean sendNeuroskyMessages;

	public SubscriptionHandlerAdapter(MultiConnectionThinkGearSocket socket, boolean canSendNeuroskyMessages) {
		super(socket);

		this.sendNeuroskyMessages = canSendNeuroskyMessages;
	}

	/**
	 * Overridden method to process subscription requests and Neurosky messages
	 * 
	 * @see ThinkGearSubscriptionRequest
	 * @see NeuroskyMessage
	 */
	public void messageReceived(IoSession session, Object message) throws Exception {
		if (message instanceof ThinkGearSubscriptionRequest) {
			subscribe(session, (ThinkGearSubscriptionRequest) message);
		} else if (message instanceof NeuroskyMessage) {
			sendNeuroskyMessage((NeuroskyMessage) message, session);
		}
	}

	public boolean canSendNeuroskyMessages() {
		return sendNeuroskyMessages;
	}

	private void sendNeuroskyMessage(NeuroskyMessage message, IoSession session) {
		if (sendNeuroskyMessages) {
			getSocket().sendMessage(message.getMessage());
		} else {
			log.error("Attempted to send message to Neurosky when functionality disabled, disconnecting: {}",
					message.getMessage());
			session.close(true);
		}
	}

}
