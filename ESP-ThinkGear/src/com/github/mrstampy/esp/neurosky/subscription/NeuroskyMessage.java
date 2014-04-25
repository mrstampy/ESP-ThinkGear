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
