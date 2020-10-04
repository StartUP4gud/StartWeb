package org.am.web.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.am.web.utils.NFProperties;

public class RealtimeForwarder {

	private static final int PORT = NFProperties.WS_FORWARDER_PORT;
	
	private static InetAddress nodeUDPServer = null;
	private static DatagramSocket dgramSocket = null;
	
	static  {
		try {
			nodeUDPServer = InetAddress.getLoopbackAddress();
			dgramSocket = new DatagramSocket();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public static void forward(String data) {

		byte[] dataBytes = data.getBytes();
				
		DatagramPacket dgramPacket =  new DatagramPacket(dataBytes, dataBytes.length, nodeUDPServer, PORT); 
		try {
			
			System.out.println("Sending to node: " + data);
			
			dgramSocket.send(dgramPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}

	} 
}
