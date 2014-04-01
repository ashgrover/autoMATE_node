package com.automate.node.utilities;

public interface FanGpioInterface {

	/**
	 * Returns integer representation of fan speed. 0=Off, 1=Slow, 2=Medium, 3=Fast, -1=Invalid state
	 * @return integer
	 */
	public abstract int getFanSpeed();

	/**
	 * Sets all pins to LOW.
	 * @return success(?) as boolean
	 */
	public abstract boolean setSpeedOff();

	/**
	 * Sets fan speed to slow after setting all pins LOW.
	 * @return success(?) as boolean
	 */
	public abstract boolean setSpeedSlow();

	/**
	 * Sets fan speed to medium after setting all pins LOW.
	 * @return success(?) as boolean
	 */
	public abstract boolean setSpeedMedium();

	/**
	 * Sets fan speed to fast after setting all pins LOW.
	 * @return success(?) as boolean
	 */
	public abstract boolean setSpeedFast();

}