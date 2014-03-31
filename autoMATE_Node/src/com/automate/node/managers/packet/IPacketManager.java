package com.automate.node.managers.packet;

import java.net.Socket;

import com.automate.node.managers.IManager;

public interface IPacketManager extends IManager<PacketListener>, PacketListener {

	public long sendPacket(String packet);
	
	public void setReceiveSocket(Socket receiveSocket);
	
}
