package com.automate.node.status.deprecated;

import java.util.List;

import com.automate.protocol.models.Status;
import com.automate.node.utilities.FanGpioUtility;

public class StatusManager implements IStatusManager{

	private FanGpioUtility gpioUtil;
	
	@Override
	public List<Status<?>> getDeviceStatuses() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
