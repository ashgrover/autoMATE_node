package com.automate.node.utilities;

/**
 * Check out this sick fan emulator. It give the real fan experience without the
 * clunky hardware!!!
 * @author jamie.bertram
 *
 */
public class MockFanGpio implements FanGpioInterface {

	private int fanSpeed;
	
	@Override
	public int getFanSpeed() {
		return fanSpeed;
	}

	@Override
	public boolean setSpeedOff() {
		setFanSpeed(0);
		return true;
	}

	@Override
	public boolean setSpeedSlow() {
		setFanSpeed(1);
		return true;
	}

	@Override
	public boolean setSpeedMedium() {
		setFanSpeed(2);
		return true;
	}

	@Override
	public boolean setSpeedFast() {
		setFanSpeed(3);
		return true;
	}
	
	private void setFanSpeed(int speed) {
		this.fanSpeed = speed;
		System.out.println("Setting fan speed to " + speed);
	}

}
