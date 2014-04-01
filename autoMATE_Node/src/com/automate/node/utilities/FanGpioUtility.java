package com.automate.node.utilities;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;


/**
 * Class for GPIO control of Raspberry Pi (used in this project as fan controller)
 * @author Ian Kabeary
 * @version 1.0
 */
public class FanGpioUtility implements FanGpioInterface {
	// create gpio controller
	private GpioController gpioController = GpioFactory.getInstance();
	
	private GpioPinDigitalOutput pin4Slow = null;
	private GpioPinDigitalOutput pin0Medium = null;
	private GpioPinDigitalOutput pin5Fast = null;
	
	/**
	 * Constructor. Initializes GpioController and initializes all used pins to LOW.
	 */
	public FanGpioUtility() {
		
		this.gpioController = GpioFactory.getInstance();
		
		this.pin4Slow = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Slow", PinState.LOW);
		this.pin0Medium = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Medium", PinState.LOW);
		this.pin5Fast = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Fast", PinState.LOW);
		
	}
    
	/**
	 * Sets provided GpioPinDigitalOutput pin to HIGH.
	 * @param pinToSet
	 * @return success(?) as boolean
	 */
    private boolean setPinHigh(GpioPinDigitalOutput pinToSet) {
    	
    	try {
    		pinToSet.high();
    	} catch (Exception e) {
    		System.err.println("[!] Error setting pin " + pinToSet + " to high! " + e.getMessage());
    		return false;
    	}
    	return true;
    }
    
    /**
	 * Sets provided GpioPinDigitalOutput pin to LOW.
	 * @param pinToSet
	 * @return success(?) as boolean
	 */
    private boolean setPinLow(GpioPinDigitalOutput pinToSet) {
    	
    	try {
    		pinToSet.low(); 
    	} catch (Exception e) {
    		System.err.println("[!] Error setting pin " + pinToSet + " to low! " + e.getMessage());
    		return false;
    	}
    	return true;
    }
    
    /**
	 * Returns state of provided GpioPinDigitalOutput pin as boolean, High=True, Low=False
	 * @param pinToSet
	 * @return success(?) as boolean
	 */
    private boolean getPinState(GpioPinDigitalOutput pinToGet) {
    	
    	if (pinToGet.isHigh()) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /* (non-Javadoc)
	 * @see com.automate.node.utilities.FanGpioInterface#getFanSpeed()
	 */
    @Override
	public int getFanSpeed() {
    	
    	if (   getPinState(this.pin4Slow) == true 
    		&& getPinState(this.pin0Medium) == false
    		&& getPinState(this.pin5Fast) == false) {
    		return 1;
    	} else if (getPinState(this.pin4Slow) == false
        		&& getPinState(this.pin0Medium) == true
        		&& getPinState(this.pin5Fast) == false) {
        	return 2;
        } else if (getPinState(this.pin4Slow) == false 
        		&& getPinState(this.pin0Medium) == false
        		&& getPinState(this.pin5Fast) == true) {
        	return 3;
        } else if (getPinState(this.pin4Slow) == false 
				&& getPinState(this.pin0Medium) == false
				&& getPinState(this.pin5Fast) == false) {
        	return 0;
        } else {
        	return -1;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.automate.node.utilities.FanGpioInterface#setSpeedOff()
	 */
    @Override
	public boolean setSpeedOff() {
    	
    	try {
    		setAllPinsLow();
    	} catch (Exception e) {
    		System.err.println("[!] Error setting speed to off! " + e.getMessage() );
    		return false;
    	}
    	return true;
    }
    
    /* (non-Javadoc)
	 * @see com.automate.node.utilities.FanGpioInterface#setSpeedSlow()
	 */
    @Override
	public boolean setSpeedSlow() {
    	
    	try {
    		setAllPinsLow();
    		setPinHigh(this.pin4Slow);
    	} catch (Exception e) {
    		System.err.println("[!] Error setting speed slow! " + e.getMessage() );
    		return false;
    	}
    	return true;
    }
    
    /* (non-Javadoc)
	 * @see com.automate.node.utilities.FanGpioInterface#setSpeedMedium()
	 */
    @Override
	public boolean setSpeedMedium() {
    	
    	try {
    		setAllPinsLow();
    		setPinHigh(this.pin0Medium);
    	} catch (Exception e) {
    		System.err.println("[!] Error setting speed medium! " + e.getMessage() );
    		return false;
    	}
    	return true;
    }
    
    /* (non-Javadoc)
	 * @see com.automate.node.utilities.FanGpioInterface#setSpeedFast()
	 */
    @Override
	public boolean setSpeedFast() {
    	
    	try {
    		setAllPinsLow();
    		setPinHigh(this.pin5Fast);
    	} catch (Exception e) {
    		System.err.println("[!] Error setting speed fast! " + e.getMessage() );
    		return false;
    	}
    	return true;
    }

    /**
     * Sets all used pins to LOW.
     * @return success(?) as boolean
     */
	private void setAllPinsLow() {
		
		setPinLow(this.pin4Slow);
		setPinLow(this.pin0Medium);
		setPinLow(this.pin5Fast);
	}
    
}


//public static void main(String[] args) throws InterruptedException {
//
//System.out.println("<--Pi4J--> GPIO Control Example ... started.");
//
//// create gpio controller
//final GpioController gpio = GpioFactory.getInstance();
//
//// provision gpio pin #01 as an output pin and turn on
//final GpioPinDigitalOutput pin4_slow = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Slow", PinState.LOW);
//final GpioPinDigitalOutput pin0_medium = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Medium", PinState.LOW);
//final GpioPinDigitalOutput pin5_fast = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Fast", PinState.LOW);
//System.out.println("--> GPIO state should be: OFF");
//
//
//char input = 0;
//
//try {
//
//	while (input != 'q') {
//	
//		try {        
//			input = (char)System.in.read();
//	        } catch (IOException e) {
//			// cool
//		}
//	
//		if (input == '1') {
//			System.out.println("Selecting SLOW");
//			pin5_fast.low();
//			pin0_medium.low();
//			pin4_slow.high();
//		} else if (input == '2') {
//			System.out.println("Selecting MEDIUM");
//			pin5_fast.low();
//			pin4_slow.low();
//			pin0_medium.high();
//		} else if (input == '3') {
//			System.out.println("Selecting JET ENGINE");
//			pin4_slow.low();
//			pin0_medium.low();
//			pin5_fast.high();
//		} else {
//			continue;
//		}
//		System.out.println("Pin 4 (slow) : " + pin4_slow.getState().toString());
//		System.out.println("Pin 0 (medium) : " + pin0_medium.getState().toString());
//		System.out.println("Pin 5 (fast) : " + pin5_fast.getState().toString()); 
//	}
//} finally {
//	pin4_slow.low();
//	pin0_medium.low();
//	pin5_fast.low();
//}
//  System.out.println("--> GPIO state should be: OFF");
//
//  // stop all GPIO activity/threads by shutting down the GPIO controller
//  // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
//  gpio.shutdown();
//}