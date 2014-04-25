package com.github.mrstampy.esp.neurosky.subscription;

import java.io.Serializable;

/**
 * Class to encapsulate a message to the Neurosky socket/headset.
 * 
 * @author burton
 */
public class NeuroskyMessage implements Serializable {
	private static final long serialVersionUID = -6853991506409118547L;

	private final String message;

	public NeuroskyMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
