package com.automate.node.managers.packet;

import com.automate.node.managers.IListener;

public interface PacketListener extends IListener {
	
	public void onPacketReceived(String packet);

	public void onEmptyPacketReceived();

	public void onReceiveIoException();

	public void onNoSocketProvided();

	public void onReceiveError();

	public void onPacketSent(int packetId);

	public void onSendIoException(int packetId);

	public void onSendNoServerAddress(int packetId);

	public void onSendNoServerPort(int packetId);

	public void onSendError(int packetId);
	
}
