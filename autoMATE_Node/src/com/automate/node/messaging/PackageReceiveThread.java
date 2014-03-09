package com.automate.node.messaging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.automate.node.messaging.HandleInputTask;

public class PackageReceiveThread extends Thread implements IPackageReceiveThread {
	
	private ServerSocket serverSocket;
	private ExecutorService threadpool;
	private boolean cancelled;
	private IMessageManager manager;
	private static final Logger logger = LogManager.getLogger();
	
	
	@Override
	public void setManager(IMessageManager manager) {
		// TODO Auto-generated method stub
		this.manager = manager;
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(6300);
			while(!cancelled) {
				HandleInputTask task = new HandleInputTask(manager, serverSocket.accept());
				threadpool.submit(task);
			}
			serverSocket.close();
		} catch (SocketException e) {
			logger.error("Error in PacketReceiveThread.", e);
		} catch (IOException e) {
			logger.error("Error in PacketReceiveThread.", e);
		} catch (RejectedExecutionException e) {
			logger.warn("Received package not processed.  PacketReceiveThread previously terminated.");
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.automate.server.messaging.IPacketReceiveThread#cancel()
	 */
	@Override
	public void cancel() {
		logger.info("Shutting down PacketReceiveThread.");
		this.cancelled = true;
		if(serverSocket != null && !serverSocket.isClosed()) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				logger.error("Error closing socket.", e);
			}
		}
		if(threadpool != null) {
			threadpool.shutdown();
		}
	}
	

}
