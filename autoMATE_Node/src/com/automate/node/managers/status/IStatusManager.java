package com.automate.node.managers.status;

import java.util.List; 

import com.automate.node.managers.IManager;
import com.automate.node.managers.message.MessageListener;
import com.automate.protocol.models.Status;

public interface IStatusManager extends IManager<StatusListener>, MessageListener, StatusListener {
	
	List<Status<?>> getStatuses();
	
}
