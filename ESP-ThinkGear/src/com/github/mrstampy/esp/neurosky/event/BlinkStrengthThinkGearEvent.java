package com.github.mrstampy.esp.neurosky.event;

/**
 * The current strength of the blink signal.
 * 
 * @author burton
 */
public class BlinkStrengthThinkGearEvent extends AbstractThinkGearEvent {
	private static final long serialVersionUID = -7822399592077342790L;
	
	private int blinkStrength;

	public BlinkStrengthThinkGearEvent() {
		super(EventType.blinkStrength);
	}

	/**
	 * Unsigned one byte value reports the intensity of the user's most recent eye
	 * blink. Its value ranges from 1 to 255 and it is reported whenever an eye
	 * blink is detected. The value indicates the relative intensity of the blink,
	 * and has no units.
	 * 
	 * @return the blink strength
	 */
	public int getBlinkStrength() {
		return blinkStrength;
	}

	public void setBlinkStrength(int blinkStrength) {
		this.blinkStrength = blinkStrength;
	}

}
