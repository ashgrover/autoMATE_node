package com.automate.node.managers.packet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class PacketSendTask implements Runnable {

	private String packet;
	private int packetId;
	private String serverAddress;
	private int serverPort;
	private IPacketManager packetManager;

	public PacketSendTask(String packet, int packetId, String serverAddress, int serverPort, IPacketManager packetManager) {
		this.packet = packet;
		this.packetId = packetId;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.packetManager = packetManager;
	}

	@Override
	public void run() {
		try{
			if(serverAddress == null || serverAddress.isEmpty()) {
				packetManager.onSendNoServerAddress(packetId);
			}
			Socket socket = new Socket(serverAddress, serverPort);
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			System.out.println("Sending packet:\n" + packet);
			writer.print(packet);
			writer.print('\0');
			socket.close();
			packetManager.onPacketSent(packetId);
		} catch(IOException e) {
			packetManager.onSendIoException(packetId);
		} catch(Exception e){
			packetManager.onSendError(packetId);
		}
	}

}
