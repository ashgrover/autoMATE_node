package com.automate.node.managers.status;

import java.util.List;

import com.automate.node.managers.IManager;
import com.automate.node.managers.connection.ConnectionListener;
import com.automate.node.managers.message.MessageListener;
import com.automate.protocol.models.Status;

public interface IStatusManager extends IManager<StatusListener>, MessageListener, ConnectionListener, StatusListener {
	
	List<Status<?>> getStatuses();
	
}
