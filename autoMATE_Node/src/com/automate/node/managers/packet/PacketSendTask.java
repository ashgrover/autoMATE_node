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
			writer.println(packet);
			writer.println('\0');
			writer.flush();
			System.out.println("\n------------------------------------------------------------\n"
					+ "Packet Sent:\n------------------------------------------------------------\n" + packet);
			packetManager.onPacketSent(packetId, socket);
		} catch(IOException e) {
			packetManager.onSendIoException(packetId);
		} catch(Exception e){
			packetManager.onSendError(packetId);
		}
	}

}
