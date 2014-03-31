package com.automate.node.managers.warning;

import com.automate.node.managers.IListener;

public interface WarningListener extends IListener {

	public void onWarningEmitted(String warning);
	
}
