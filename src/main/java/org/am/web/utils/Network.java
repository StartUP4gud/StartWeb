package org.am.web.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.am.web.bean.NetworkInterface;

public class Network {
	
	public static ArrayList<NetworkInterface> getInterfaceDetails(){ 
		try{
			ArrayList<NetworkInterface> list = new ArrayList<NetworkInterface>();		 

			ArrayList<HashMap<String, String>> interfaceList= printHardwareAddresses();

			for(HashMap<String, String> iface:interfaceList){
				for(String ifaceName : iface.keySet()){

					NetworkInterface rBean = new NetworkInterface();
					rBean.interface_name = ifaceName;
					rBean.mac = iface.get(ifaceName).toUpperCase().replaceAll(":", "-");
					rBean = getNetplanConf(rBean);

					if(rBean.interface_name.equals("lo")){
						continue;
					}
					list.add(rBean);
				}
			}
			return list;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	private static ArrayList<HashMap<String, String>> printHardwareAddresses() throws SocketException {
	   
		ArrayList<HashMap<String, String>> data=new ArrayList<HashMap<String, String>>();
	 
	        List<String> devices = new ArrayList<>();
	        Pattern pattern = Pattern.compile("^ *(.*):");
	        try (FileReader reader = new FileReader("/proc/net/dev")) {
	            BufferedReader in = new BufferedReader(reader);
	            String line = null;
	            while( (line = in.readLine()) != null) {
	                Matcher m = pattern.matcher(line);
	                if (m.find()) {
	                    devices.add(m.group(1));
	                } 
	            } 
	        } catch (IOException e) {
	            e.printStackTrace();
	        } 
	 
	        for (String device : devices) {
	            try (FileReader reader = new FileReader("/sys/class/net/" + device + "/address")) {
	                BufferedReader in = new BufferedReader(reader);
	                String addr = in.readLine();
	                HashMap<String, String> data2= new HashMap<String, String>();
	                data2.put(device, addr);
	                data.add(data2);	                
	                
	            } catch (IOException e) {
	                e.printStackTrace();
	            } 
	        }
	        return data;
	}
	
	private static NetworkInterface getNetplanConf(NetworkInterface rBean) {
		try {
			FileReader reader = new FileReader("/etc/netplan/" + rBean.interface_name + ".yaml");
			BufferedReader in = new BufferedReader(reader);
			String line = null;
			boolean nameserverFlag = false;
            while( (line = in.readLine()) != null) {
            	line = line.trim();
            	
            	if(line.startsWith("addresses")) {
            	
            		String temp[] =line.split(":");
            		String address = temp[1].trim();
            		address = address.replace("[", "");
            		address = address.replace("]", "");
            		if(nameserverFlag == true) {
            			rBean.dns_nameserver = address;
            		} else {
            			rBean.ip = address;
            		}
            		            		
            	} else if(line.startsWith("gateway")) {
            		
            		String temp[] =line.split(":");
            		String address = temp[1].trim();
            		rBean.gateway = address;
            	
            	} else if(line.startsWith("nameservers")) {
            		nameserverFlag = true;
            	}
            } 
            in.close();
		} catch (Exception e) {
            e.printStackTrace();
        } 
		
		try {
			String ipAddress = rBean.ip.split("/")[0].trim();
			String hostname = InetAddress.getLocalHost().getHostName();
	    	
			InetAddress host = InetAddress.getByName(hostname);
	        String hostIP = host.getHostAddress();
	        
	        System.out.println(hostname + " " + hostIP + "> " + ipAddress);
	        
	        if(hostIP.equals(ipAddress)) {
	        	rBean.mangmt = true;
	        } else {
	        	rBean.mangmt = false;
	        }
		} catch (Exception e) {
            e.printStackTrace();

            rBean.mangmt = false;
        } 
		try {
			String inputInterface = NFProperties.getProperty("input.interface","");				
			if(inputInterface.trim().equals(rBean.interface_name)){
				rBean.span = true;
			} else {
				rBean.span = false;
			}
		} catch (Exception e) {
            e.printStackTrace();
            rBean.span = false;
        } 
		
		try {
			java.net.NetworkInterface netint = java.net.NetworkInterface.getByName(rBean.interface_name);
			rBean.status = netint.isUp();		
			
		}  catch (Exception e) {
            e.printStackTrace();
        } 
		
		return rBean;
	}
}
