package com.automate.node.status.deprecated;

import java.util.List;
import com.automate.protocol.models.Status;
public interface IStatusManager {

	public List<Status<?>> getDeviceStatuses();
}
