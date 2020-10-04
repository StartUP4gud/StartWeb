package org.am.web.snmp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.am.web.snmp.bean.Interface;
import org.am.web.snmp.bean.Memory;
import org.am.web.snmp.bean.Storage;
import org.am.web.snmp.bean.oids.InterfaceOID;
import org.am.web.snmp.bean.oids.MemoryOID;
import org.am.web.snmp.bean.oids.ProcessorOID;
import org.am.web.utils.NFProperties;
import org.am.web.utils.Utils;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeUtils;
import org.snmp4j.util.TreeEvent;

public class SnmpResources {

	static String snmpAddress;
	static String snmpKey;
	static String version;

	public static HashMap<String, Long> oldDataIN1min = new HashMap<>();
	public static HashMap<String, Long> oldDataOUT1min = new HashMap<>();
	public static HashMap<String, Long> oldDataIN30min = new HashMap<>();
	public static HashMap<String, Long> oldDataOUT30min = new HashMap<>();

	static {
		try {
			snmpAddress = "127.0.0.1" + "/" + 161;
			snmpKey = NFProperties.SNMP_KEY;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void getProcessorCamel(long timestamp, String dir) {

		OID typeOid = new OID("1.3.6.1.2.1.25.3.2.1.2");
		OID LoadOid = new OID("1.3.6.1.2.1.25.3.3.1.2");

		TransportMapping transport;
		Snmp snmp;
		try {
			transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			transport.listen();

			CommunityTarget target = new CommunityTarget();
			target.setCommunity(new OctetString(snmpKey));
			target.setVersion(SnmpConstants.version2c);
			target.setAddress(new UdpAddress(snmpAddress));
			target.setTimeout(1500); 
			target.setRetries(2);
			HashMap<String, String> processorMap = new HashMap<String, String>();


			TreeUtils treeUtils= new TreeUtils(snmp ,new DefaultPDUFactory());
			List<TreeEvent> Pevents = treeUtils.getSubtree(target, typeOid);

			treeUtils.setMaxRepetitions(200);

			for (TreeEvent Pevent : Pevents) {
				VariableBinding[] pvbs = Pevent.getVariableBindings();
				if (pvbs == null || pvbs.length == 0) {
					//System.out.println("VarBinding: No result returned.");
				}

				for (VariableBinding pvb : pvbs) {

					String poid = pvb.getOid().toString();
					String pvalue = pvb.toValueString();

					if(poid.startsWith(ProcessorOID.type)){
						if(pvalue.equals(ProcessorOID.processorType)) {
							String temp[] = poid.split(ProcessorOID.type);
							processorMap.put(temp[1],pvalue);
						}
					}
				}
			}
			List<TreeEvent> Levents = treeUtils.getSubtree(target, LoadOid);
			treeUtils.setMaxRepetitions(200);

			for (TreeEvent Levent : Levents) {
				VariableBinding[] lvbs = Levent.getVariableBindings();

				for (VariableBinding lvb : lvbs) {
					String Loid = lvb.getOid().toString();
					String Lvalue = lvb.toValueString();
										
					if(Loid.startsWith(ProcessorOID.load))
					{
						String temp[] = Loid.split(ProcessorOID.load);
						if(processorMap.containsKey(temp[1])) {
							processorMap.put(temp[1], Lvalue);
						}
					}
				}	
			}  
			Set<String> s = processorMap.keySet();
			
			double load = 0;
			int index = 1;
			for(String key : s) {				
				load = Double.parseDouble(processorMap.get(key)); 
				String cpuIndex = "cpu" + index;
				if(dir.equals("month")) {
					
					StringBuilder data = new StringBuilder();
					data.append(timestamp).append(",").append(cpuIndex).append(",").append((long)load).append("\n");
	
					Utils.storeDataCsv(data.toString(), NFProperties.SECONDARY_STORAGE_PATH+"stats/"+ dir +"/cpu/"+Utils.getMonth()+"/0_0.csv");
				}
				StringBuilder data = new StringBuilder();
				data.append(timestamp).append(",").append(cpuIndex).append(",").append((long)load).append("\n");
	
				Utils.storeDataCsv(data.toString(), NFProperties.SECONDARY_STORAGE_PATH+"stats/"+ dir +"/cpu/0_0.csv");
				index++;
			}		
			try {
				snmp.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				transport.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
	}

	@SuppressWarnings("unchecked")
	public static void getStorageCamel(long timestamp, String dir){

		String oidString = MemoryOID.memoryOID;

		TransportMapping transport;
		Snmp snmp;
		try {
			transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			transport.listen();

			CommunityTarget target = new CommunityTarget();
			target.setCommunity(new OctetString(snmpKey));
			target.setVersion(SnmpConstants.version2c);
			target.setAddress(new UdpAddress(snmpAddress)); 
			target.setTimeout(3000);
			target.setRetries(1);

			PDU pdu = new PDU();
			pdu.setType(PDU.GETBULK);
			pdu.setMaxRepetitions(200);
			pdu.setNonRepeaters(0);
			pdu.add(new VariableBinding(new OID(oidString)));

			ResponseEvent responseEvent = snmp.send(pdu, target);
			PDU response = responseEvent.getResponse();

			if (response != null) {
				if (response.getErrorStatus() == PDU.noError) {
					HashMap<String, HashMap<String,String>> storageMap = new HashMap<String, HashMap<String,String>>();
					HashMap<String, HashMap<String,String>> memoryMap = new HashMap<String, HashMap<String,String>>();
					Vector<? extends VariableBinding> vbs = response.getVariableBindings();
					for (VariableBinding vb : vbs) {
						String oid = vb.getOid().toString();
						String value = vb.toValueString();

						if (oid.startsWith(MemoryOID.index)) {

							storageMap.put(value, new HashMap<String,String>());
							memoryMap.put(value, new HashMap<String,String>());
						} else if (oid.startsWith(MemoryOID.type)) {
							String temp[] = oid.split(MemoryOID.type);
							if (!value.equals(MemoryOID.storageType)) {
								storageMap.remove(temp[1]);
							}
							if (!value.equals(MemoryOID.physicalMemType)
									&& !value.equals(MemoryOID.otherMemoryType)) {
								memoryMap.remove(temp[1]);
							}
						} else if (oid.startsWith(MemoryOID.descr)) {
							String temp[] = oid.split(MemoryOID.descr);
							if (storageMap.containsKey(temp[1])) {
								HashMap<String,String> map = storageMap.get(temp[1]);
								map.put("path", value);
								storageMap.put(temp[1], map);
							} else if (memoryMap.containsKey(temp[1])) {
								HashMap<String,String> map = memoryMap.get(temp[1]);
								map.put("name", value);
								memoryMap.put(temp[1], map);
							}
						} else if (oid.startsWith(MemoryOID.allocUnit)) {
							String temp[] = oid.split(MemoryOID.allocUnit);
							if (storageMap.containsKey(temp[1])) {
								HashMap<String,String> map = storageMap.get(temp[1]);
								map.put("alloc", value);
								storageMap.put(temp[1], map);
							} else if (memoryMap.containsKey(temp[1])) {
								HashMap<String,String> map = memoryMap.get(temp[1]);
								map.put("alloc", value);
								memoryMap.put(temp[1], map);
							}
						} else if (oid.startsWith(MemoryOID.size)) {
							String temp[] = oid.split(MemoryOID.size);
							if (storageMap.containsKey(temp[1])) {
								HashMap<String,String> map = storageMap.get(temp[1]);
								map.put("total", value);
								storageMap.put(temp[1], map);
							} else if (memoryMap.containsKey(temp[1])) {
								HashMap<String,String> map = memoryMap.get(temp[1]);
								map.put("total", value);
								memoryMap.put(temp[1], map);
							}
						} else if (oid.startsWith(MemoryOID.used)) {
							String temp[] = oid.split(MemoryOID.used);
							if (storageMap.containsKey(temp[1])) {
								HashMap<String,String> map = storageMap.get(temp[1]);
								map.put("used", value);
								storageMap.put(temp[1], map);
							} else if (memoryMap.containsKey(temp[1])) {
								HashMap<String,String> map = memoryMap.get(temp[1]);
								map.put("used", value);
								memoryMap.put(temp[1], map);
							}
						}
					}
					Set<String> s = storageMap.keySet();
					for (String key : s) {
						HashMap<String,String> m = storageMap.get(key);
						String path = (String) m.get("path");
						if (path.startsWith("/run") || path.startsWith("/sys")) {
							continue;
						}
						Storage storage = new Storage();
						storage.path = path;
						storage.totalStorage = Long.parseLong((String) m
								.get("total"))
								* Long.parseLong((String) m.get("alloc"));
						storage.usedStorage = Long
								.parseLong((String) m.get("used"))
								* Long.parseLong((String) m.get("alloc"));
						storage.freeStorage = storage.totalStorage
								- storage.usedStorage;


						float ss = (storage.usedStorage*100)/storage.totalStorage;
						
						if(dir.equals("month")) {
							StringBuilder data = new StringBuilder();
							data.append(timestamp)
							.append(",").append(storage.path)
							.append(",").append((long)ss)
							.append("\n");
							Utils.storeDataCsv(data.toString(), NFProperties.SECONDARY_STORAGE_PATH+"stats/"+ dir +"/storage/"+Utils.getMonth()+"/0_0.csv");
						}
						StringBuilder data = new StringBuilder();
						data.append(timestamp).append(",").append(storage.path).append(",").append((long)ss).append("\n");

						Utils.storeDataCsv(data.toString(), NFProperties.SECONDARY_STORAGE_PATH+"stats/"+ dir +"/storage/0_0.csv");


					}

					Set<String> s1 = memoryMap.keySet();
					Memory memory = new Memory();
					for (String key : s1) {
						HashMap<String,String> m = memoryMap.get(key);
						String name = (String) m.get("name");
						if (name.equals("Physical memory")) {
							memory.totalphysicalMemory = Long.parseLong((String) m
									.get("total"))
									* Long.parseLong((String) m.get("alloc"));
							memory.usedPhysicalMemory = Long.parseLong((String) m
									.get("used"))
									* Long.parseLong((String) m.get("alloc"));
						} else if (name.equals("Cached memory")) {
							memory.usedCacheMemory = Long.parseLong((String) m
									.get("used"))
									* Long.parseLong((String) m.get("alloc"));
						} else if (name.equals("Memory buffers")) {
							memory.usedBufferMemory = Long.parseLong((String) m
									.get("used"))
									* Long.parseLong((String) m.get("alloc"));
						} else if (name.equals("Shared memory")) {
							memory.usedSharedMemory = Long.parseLong((String) m
									.get("used"))
									* Long.parseLong((String) m.get("alloc"));
						}
					}

					memory.freePhysicalMemory = memory.totalphysicalMemory - memory.usedPhysicalMemory;


					if(dir.equals("month")) {

						StringBuilder data = new StringBuilder();
						data.append(timestamp).append(",").append("Total_Memory").append(",").append(memory.totalphysicalMemory).append("\n")
						.append(timestamp).append(",").append("Used_Memory").append(",").append(memory.usedPhysicalMemory).append("\n")
						.append(timestamp).append(",").append("Cache_Memory").append(",").append(memory.usedCacheMemory).append("\n")
						.append(timestamp).append(",").append("Free_Memory").append(",").append(memory.freePhysicalMemory).append("\n");

						Utils.storeDataCsv(data.toString(), NFProperties.SECONDARY_STORAGE_PATH+"stats/"+ dir +"/memory/"+Utils.getMonth()+"/0_0.csv");
					}
					StringBuilder data = new StringBuilder();
					data.append(timestamp).append(",").append("Total_Memory").append(",").append(memory.totalphysicalMemory).append("\n")
					.append(timestamp).append(",").append("Used_Memory").append(",").append(memory.usedPhysicalMemory).append("\n")
					.append(timestamp).append(",").append("Cache_Memory").append(",").append(memory.usedCacheMemory).append("\n")
					.append(timestamp).append(",").append("Free_Memory").append(",").append(memory.freePhysicalMemory).append("\n");

					Utils.storeDataCsv(data.toString(), NFProperties.SECONDARY_STORAGE_PATH+"stats/"+ dir +"/memory/0_0.csv");

				}
			}
			try {
				snmp.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				transport.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void getInterfaceCamel(HashMap<String, Long> oldDataIN, HashMap<String, Long> oldDataOUT, int minute, String dir, long timestamp) {

		OID IOid = new OID("1.3.6.1.2.1.2.2.1");
		TransportMapping transport;
		Snmp snmp;

		try {
			transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			transport.listen();

			CommunityTarget target = new CommunityTarget();
			target.setCommunity(new OctetString(snmpKey));
			target.setVersion(SnmpConstants.version2c);
			target.setAddress(new UdpAddress(snmpAddress));
			target.setTimeout(3000);
			target.setRetries(1);

			HashMap<String, HashMap<String,String>> interfaceMap = new HashMap<String, HashMap<String,String>>();

			TreeUtils treeUtils= new TreeUtils(snmp ,new DefaultPDUFactory());
			List<TreeEvent> Ievents = treeUtils.getSubtree(target, IOid);
			treeUtils.setMaxRepetitions(200);

			for (TreeEvent Ievent : Ievents) {
				VariableBinding[] Ivbs = Ievent.getVariableBindings();

				for (VariableBinding Ivb : Ivbs) {
					String oid = Ivb.getOid().toString();
					String value = Ivb.toValueString();

					if (oid.startsWith(InterfaceOID.index)) {
						interfaceMap.put(value, new HashMap<String,String>());
					} else if (oid.startsWith(InterfaceOID.intType)) {
						String temp[] = oid.split(InterfaceOID.intType);
						if (interfaceMap.containsKey(temp[1])) {
							HashMap<String,String> map = interfaceMap.get(temp[1]);
							map.put("type", value);
							interfaceMap.put(temp[1], map);
						}
					} else if (oid.startsWith(InterfaceOID.macAddress)) {
						String temp[] = oid.split(InterfaceOID.macAddress);
						if (interfaceMap.containsKey(temp[1])) {
							HashMap<String,String> map = interfaceMap.get(temp[1]);
							map.put("mac", value);
							interfaceMap.put(temp[1], map);
						}
					} else if (oid.startsWith(InterfaceOID.status)) {
						String temp[] = oid.split(InterfaceOID.status);
						if (interfaceMap.containsKey(temp[1])) {
							HashMap<String,String> map = interfaceMap.get(temp[1]);
							if(Integer.parseInt(value)==1){
								map.put("status", "UP");
							}else
							{
								map.put("status", "DOWN");
							}
							interfaceMap.put(temp[1], map);
						}
					} else if (oid.startsWith(InterfaceOID.inUcastPkts)) {
						String temp[] = oid.split(InterfaceOID.inUcastPkts);
						if (interfaceMap.containsKey(temp[1])) {
							HashMap<String,String> map = interfaceMap.get(temp[1]);
							map.put("inUcastPkts", value);
							interfaceMap.put(temp[1], map);
						}
					} else if (oid.startsWith(InterfaceOID.outUcastPkts)) {
						String temp[] = oid.split(InterfaceOID.outUcastPkts);
						if (interfaceMap.containsKey(temp[1])) {
							HashMap<String,String> map = interfaceMap.get(temp[1]);
							map.put("outUcastPkts", value);
							interfaceMap.put(temp[1], map);
						}
					} else if (oid.startsWith(InterfaceOID.inNonUcastPkts)) {
						String temp[] = oid.split(InterfaceOID.inNonUcastPkts);
						if (interfaceMap.containsKey(temp[1])) {
							HashMap<String,String> map = interfaceMap.get(temp[1]);
							map.put("inNonUcastPkts", value);
							interfaceMap.put(temp[1], map);
						}
					} else if (oid.startsWith(InterfaceOID.outNonUcastPkts)) {
						String temp[] = oid.split(InterfaceOID.outNonUcastPkts);
						if (interfaceMap.containsKey(temp[1])) {
							HashMap<String,String> map = interfaceMap.get(temp[1]);
							map.put("outNonUcastPkts", value);
							interfaceMap.put(temp[1], map);
						}

					} 
				}
			}

			OID IOid2 = new OID("1.3.6.1.2.1.31.1.1.1");
			CommunityTarget target2 = new CommunityTarget();
			target2.setCommunity(new OctetString(snmpKey));
			target2.setVersion(SnmpConstants.version2c);
			target2.setAddress(new UdpAddress(snmpAddress));
			target2.setTimeout(3000);
			target2.setRetries(1);

			TreeUtils treeUtils2= new TreeUtils(snmp ,new DefaultPDUFactory());
			List<TreeEvent> Ievents2 = treeUtils2.getSubtree(target2, IOid2);
			treeUtils.setMaxRepetitions(200);

			for (TreeEvent Ievent : Ievents2) {
				VariableBinding[] Ivbs = Ievent.getVariableBindings();

				for (VariableBinding Ivb : Ivbs) {
					String oid = Ivb.getOid().toString();
					String value = Ivb.toValueString();

					if (oid.startsWith(InterfaceOID.inOctets64)) {
						String temp[] = oid.split(InterfaceOID.inOctets64);
						if (interfaceMap.containsKey(temp[1])) {
							HashMap<String,String> map = interfaceMap.get(temp[1]);
							map.put("inBytes", value);
							interfaceMap.put(temp[1], map);
						}
					} else if (oid.startsWith(InterfaceOID.outOctets64)) {
						String temp[] = oid.split(InterfaceOID.outOctets64);
						if (interfaceMap.containsKey(temp[1])) {
							HashMap<String,String> map = interfaceMap.get(temp[1]);
							map.put("outBytes", value);
							interfaceMap.put(temp[1], map);
						}

					} else if (oid.startsWith(InterfaceOID.interfaceName64)) {
						String temp[] = oid.split(InterfaceOID.interfaceName64);
						if (interfaceMap.containsKey(temp[1])) {
							HashMap<String,String> map = interfaceMap.get(temp[1]);
							map.put("name", value);
							interfaceMap.put(temp[1], map);
						}
					}
				}
			}

			HashMap<String, Object> nfMap = new HashMap<String, Object>();
			try {
				Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
				for (NetworkInterface netIf : Collections.list(nets)) {
					if (!netIf.isLoopback() && !netIf.isPointToPoint()) {
						String name = netIf.getName();

						String value = netIf.isUp() ? "UP" : "DOWN";

						Enumeration<InetAddress> ipList = netIf
								.getInetAddresses();
						for (InetAddress ip : Collections.list(ipList)) {
							if (!ip.isLinkLocalAddress()) {
								value = value + "," + ip.getHostAddress();
								break;
							}
						}
						nfMap.put(name, value);
					}

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			Set<String> s = interfaceMap.keySet();
			for (String key : s) {
				HashMap<String,String> m = interfaceMap.get(key);
				String name = (String) m.get("name");
				if (name.equals("lo")) {
					continue;
				}
				Interface interfaces = new Interface();
				interfaces.ifIndex = key;
				interfaces.name = name;
				interfaces.macAddress = (String) m.get("mac");
				interfaces.status=(String) m.get("status");

				interfaces.bytesIN = Long.parseLong((String) m
						.get("inBytes"));
				interfaces.bytesOUT = Long.parseLong((String) m
						.get("outBytes"));
				interfaces.bytesTotal = interfaces.bytesIN
						+ interfaces.bytesOUT;
				interfaces.pktsIN = Long.parseLong((String) m
						.get("inUcastPkts"))
						+ Long.parseLong((String) m.get("inNonUcastPkts"));
				interfaces.pktsOUT = Long.parseLong((String) m
						.get("outUcastPkts"))
						+ Long.parseLong((String) m.get("outNonUcastPkts"));
				interfaces.pktsTotal = interfaces.pktsIN
						+ interfaces.pktsOUT;

				try {
					String temp[] = ((String) nfMap.get(interfaces.name)).split(",");
					//interfaces.status1 = temp[0];
					interfaces.ip = temp[1];
				} catch (Exception ex) {
					//interfaces.status1 = "DOWN";
					interfaces.ip = "";
				}

				Long _oldDataIN = oldDataIN.get(name);

				if(_oldDataIN==null){
					_oldDataIN = (long)0;
				}

				long newBytesIN = interfaces.bytesIN;

				long bandwidthIN =0;
				if(_oldDataIN<=newBytesIN) {
					bandwidthIN = (newBytesIN - _oldDataIN)/(60 * minute);
				}else {
					if(oldDataIN.get(name+"band")!=null) {
						bandwidthIN = oldDataIN.get(name+"band");
					}else {
						bandwidthIN=0;
					}
				}
				oldDataIN.put(name, newBytesIN);
				oldDataIN.put(name+"band", bandwidthIN);

				Long _oldDataOUT = oldDataOUT.get(name);

				if(_oldDataOUT==null){
					_oldDataOUT = (long)0;
				}
				long newBytesOUT = interfaces.bytesOUT;
				long bandwidthOUT = 0;
				if(_oldDataOUT<=newBytesOUT) {
					bandwidthOUT = (newBytesOUT - _oldDataOUT)/(60 * minute);
				}else {
					if(oldDataOUT.get(name+"band")!=null) {
						bandwidthOUT = oldDataOUT.get(name+"band");
					}else {
						bandwidthOUT=0;
					}
				}
				oldDataOUT.put(name, newBytesOUT);
				oldDataOUT.put(name+"band", newBytesOUT);

				if(_oldDataIN==0){
					bandwidthIN =0; 
				}

				if(_oldDataOUT==0){
					bandwidthOUT =0; 
				}

				StringBuilder dataIN = new StringBuilder();
				dataIN.append(timestamp).append(",").append(interfaces.name).append(",").append(bandwidthIN*8).append("\n");

				StringBuilder dataOUT = new StringBuilder();
				dataOUT.append(timestamp).append(",").append(interfaces.name).append(",").append(bandwidthOUT*8).append("\n");

				if(dir.equals("month")) {
					Utils.storeDataCsv(dataIN.toString(), NFProperties.SECONDARY_STORAGE_PATH+"stats/"+dir+"/Interface_IN/"+Utils.getMonth()+"/0_0.csv");  
					Utils.storeDataCsv(dataOUT.toString(), NFProperties.SECONDARY_STORAGE_PATH+"stats/"+dir+"/Interface_OUT/"+Utils.getMonth()+"/0_0.csv");
				}else{
					Utils.storeDataCsv(dataIN.toString(), NFProperties.SECONDARY_STORAGE_PATH+"stats/"+dir+"/Interface_IN/0_0.csv");  
					Utils.storeDataCsv(dataOUT.toString(), NFProperties.SECONDARY_STORAGE_PATH+"stats/"+dir+"/Interface_OUT/0_0.csv");
				}

			}		
			try {
				snmp.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				transport.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void trigger(){ 
		try {
			//System.out.println("1 Minute: SNMP Triggered...");

			long timestamp = System.currentTimeMillis();
			String dir = Utils.getTodayDate();

			getProcessorCamel(timestamp, dir);
			getStorageCamel(timestamp, dir);
			getInterfaceCamel(oldDataIN1min,oldDataOUT1min,1,dir,timestamp);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void trigger30Min(){ 
		try {
			//System.out.println("30 Minute: SNMP Triggered...");

			long timestamp = System.currentTimeMillis();
			getProcessorCamel(timestamp,"month");
			getStorageCamel(timestamp, "month");

			getInterfaceCamel(oldDataIN30min,oldDataOUT30min,30,"month",timestamp);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

