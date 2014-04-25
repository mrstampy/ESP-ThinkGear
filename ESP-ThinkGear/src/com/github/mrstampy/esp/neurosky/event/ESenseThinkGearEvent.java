package com.github.mrstampy.esp.neurosky.event;

/**
 * The current ESense signals.
 * 
 * @author burton
 */
public class ESenseThinkGearEvent extends AbstractThinkGearEvent {
	private static final long serialVersionUID = -4608249038627465552L;
	
	private int attention;
	private int meditation;

	public ESenseThinkGearEvent() {
		super(EventType.eSense);
	}

	/**
	 * Returns the current attention level [0, 100].
	 * 
	 * <pre>
	 * Values in [1, 20] are considered strongly lowered. 
	 * Values in [20, 40] are considered reduced levels. 
	 * Values in [40, 60] are considered neutral. 
	 * Values in [60, 80] are considered slightly elevated. 
	 * Values in [80, 100] are considered elevated.
	 * </pre>
	 */
	public int getAttention() {
		return attention;
	}

	/**
	 * Returns the current meditation level [0, 100]. The interpretation of the
	 * values is the same as for the attention level.
	 * 
	 * @return the meditation strength
	 */
	public int getMeditation() {
		return meditation;
	}

	public void setAttention(int attention) {
		this.attention = attention;
	}

	public void setMeditation(int meditation) {
		this.meditation = meditation;
	}

}
