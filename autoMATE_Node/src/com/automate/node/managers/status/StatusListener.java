package com.automate.node.managers.status;

import java.util.List;

import com.automate.node.managers.IListener;
import com.automate.protocol.models.Status;

public interface StatusListener extends IListener {

	public void onStatusUpdateRequested();
	
	public void onStatusUpdateSent(List<Status<?>> statuses);
	
}
