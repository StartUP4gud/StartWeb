package org.am.web.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.am.web.service.StatsWriter;
import org.am.web.utils.NFProperties;
import org.apache.camel.Exchange;

public class UDPHandler {
	
	static final int PORT = NFProperties.UDP_LISTENER_PORT;
	static final String IP = NFProperties.UDP_LISTENER_IP;
	
	public void trigger(Exchange exchange){ 
		try {
			InetAddress address = InetAddress.getByName(IP);
			DatagramSocket dSocket = new DatagramSocket(PORT, address);
			
			System.out.println("Listening on UDP Socket: " + IP + ":" + PORT);
			
			byte[] dataBytes = null; 
			DatagramPacket packet = null;
			while(true) {
				try {
					dataBytes = new byte[4096];
					packet = new DatagramPacket(dataBytes, dataBytes.length);
					dSocket.receive(packet);
					
					String data = new String(dataBytes).trim();
					
					StatsWriter.handleData(data);
					
					RealtimeForwarder.forward(data);
					
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
