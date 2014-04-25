package com.github.mrstampy.esp.neurosky.event;

/**
 * The current poor signal level value.
 * 
 * @author burton
 */
public class PoorSignalLevelThinkGearEvent extends AbstractThinkGearEvent {
	private static final long serialVersionUID = -3183510611030991676L;

	private static final int NOT_TOUCHING_SKIN = 200;

	private int poorSignalLevel;

	public PoorSignalLevelThinkGearEvent() {
		super(EventType.poorSignalLevel);
	}

	/**
	 * Returns the signal level [0, 200]. The greater the value, the more noise is
	 * detected in the signal. 200 is a special value that means that the
	 * ThinkGear contacts are not touching the skin.
	 * 
	 * @return the poor signal level value
	 */
	public int getPoorSignalLevel() {
		return poorSignalLevel;
	}

	public void setPoorSignalLevel(int poorSignalLevel) {
		this.poorSignalLevel = poorSignalLevel;
	}

	public boolean isTouchingSkin() {
		return !isNotTouchingSkin();
	}

	public boolean isNotTouchingSkin() {
		return getPoorSignalLevel() == NOT_TOUCHING_SKIN;
	}

}
