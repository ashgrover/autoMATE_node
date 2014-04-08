package com.automate.node.managers.discovery;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.automate.node.bluetooth.BluetoothInterface;
import com.automate.node.managers.IListener;
import com.automate.node.managers.ManagerBase;

public class DiscoveryManager extends ManagerBase<DiscoveryListener> implements IDiscoveryManager {

	private long nodeId;
	private String password;
	private BluetoothInterface bluetooth;
	private String ssid;
	private String username;
	private String passphrase;

	public DiscoveryManager(long nodeId, String password, BluetoothInterface bluetooth) {
		super(DiscoveryListener.class);
		this.nodeId = nodeId;
		this.password = password;
		this.bluetooth = bluetooth;
	}

	@Override
	public void onBroadcastSent() {
		synchronized (mListeners) {
			for(DiscoveryListener listener : mListeners) {
				try {
					listener.onBroadcastSent();
				} catch(RuntimeException e) {
					System.out.println("Error notifying listener.");
				}
			}
		}
		System.out.println("onBroadcastSent");
	}

	@Override
	public void onPairRequest() {
		synchronized (mListeners) {
			for(DiscoveryListener listener : mListeners) {
				try {
					listener.onPairRequest();
				} catch(RuntimeException e) {
					System.out.println("Error notifying listener.");
				}
			}
		}
		System.out.println("onPairRequest");
	}

	@Override
	public void onDeviceInformationSent() {
		synchronized (mListeners) {
			for(DiscoveryListener listener : mListeners) {
				try {
					listener.onDeviceInformationSent();
				} catch(RuntimeException e) {
					System.out.println("Error notifying listener.");
				}
			}
		}
		System.out.println("onDeviceInformationSent");
	}

	@Override
	public void onPairFailed() {
		synchronized (mListeners) {
			for(DiscoveryListener listener : mListeners) {
				try {
					listener.onPairFailed();
				} catch(RuntimeException e) {
					System.out.println("Error notifying listener.");
				}
			}
		}
		System.out.println("onPairFailed");
	}
	
	/* (non-Javadoc)
	 * @see com.automate.node.managers.discovery.DiscoveryListener#onWifiCredsProvided(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onWifiCredsProvided(String ssid, String username, String passphrase) {
		writeWifiCredentials(ssid, username, passphrase);
		this.ssid = ssid;
		this.username = username;
		this.passphrase = passphrase;
		synchronized (mListeners) {
			for(DiscoveryListener listener : mListeners) {
				try {
					listener.onWifiCredsProvided(ssid, username, passphrase);
				} catch(RuntimeException e) {
					System.out.println("Error notifying listener.");
				}
			}
		}
		System.out.println("onWifiCredsProvided");
	}

	@Override
	public void onPairSuccess(long nodeId, String password) {
		bluetooth.turnOffBluetooth();
		writeCredentials(nodeId, password);
		synchronized (mListeners) {
			for(DiscoveryListener listener : mListeners) {
				try {
					listener.onPairSuccess(nodeId, password);
				} catch(RuntimeException e) {
					System.out.println("Error notifying listener.");
				}
			}
		}
		System.out.println("onPairSuccess");
	}

	@Override
	public void onBind(Class<? extends IListener> listenerClass) {}
	@Override
	public void onUnbind(Class<? extends IListener> listenerClass) {}

	@Override
	public void sendBroadcast() {
		this.bluetooth.turnOnBluetooth(this);
	}

	@Override
	protected void unbindSelf() {}
	@Override
	protected void bindSelf() {}

	@Override
	protected void setupInitialState() {
		if(nodeId == -1L || password == null) {
			sendBroadcast();
		}
	}

	@Override
	protected void teardown() {

	}

	@Override
	protected void performInitialUpdate(DiscoveryListener listener) {
		if(this.ssid != null && this.username != null && this.passphrase != null) {
			listener.onWifiCredsProvided(ssid, username, passphrase);
		}
		if(nodeId > 0 && password != null) {
			listener.onPairSuccess(nodeId, password);
		}
	}

	private void writeWifiCredentials(String ssid, String username, String passphrase) {
		try {
			File outfile = new File("resources/wifi.properties");
			PrintWriter writer = new PrintWriter(outfile);
			writer.println("wifi.SSID=" + ssid);
			writer.println("wifi.username=" + username);
			writer.println("wifi.passphrase=" + passphrase);
			writer.close();
			outfile = new File("scripts/wpa.conf");
			writer = new PrintWriter(outfile);
			writer.println("ctrl_interface=/var/run/wpa_supplicant");
			writer.println("ctrl_interface_group=root");
			writer.println("");
			writer.println("network={");
			writer.println("	ssid=\"" + ssid + "\"");
			writer.println("	key_mgmt=WPA-EAP");
			writer.println("	identity=\"" + username + "\"");
			writer.println("	password=\"" + passphrase + "\"");
			writer.println("	pairwise=TKIP CCMP");
			writer.println("	auth_alg=OPEN");
			writer.println("}");
			writer.println("");
			writer.close();
		} catch (IOException e) {
			System.out.println("Error writing wifi credentials to disk.");
		}
	}

	private void writeCredentials(long nodeId, String password) {
		try {
			File outfile = new File("resources/credentials.properties");
			PrintWriter writer = new PrintWriter(outfile);
			writer.println("credentials.nodeId=" + nodeId);
			writer.println("credentials.password=" + password);
			writer.close();
		} catch (IOException e) {
			System.out.println("Error writing credentials to disk.");
		}
	}

}
