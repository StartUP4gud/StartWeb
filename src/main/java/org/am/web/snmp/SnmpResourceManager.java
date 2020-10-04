package org.am.web.snmp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.am.web.snmp.bean.Interface;
import org.am.web.snmp.bean.Memory;
import org.am.web.snmp.bean.Processor;
import org.am.web.snmp.bean.Storage;
import org.am.web.snmp.bean.oids.InterfaceOID;
import org.am.web.snmp.bean.oids.MemoryOID;
import org.am.web.snmp.bean.oids.ProcessorOID;
import org.am.web.utils.NFProperties;
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

public class SnmpResourceManager {

	static String snmpAddress;
	static String snmpKey;
	static String version;

	static {
		try {
			snmpAddress = "127.0.0.1" + "/" + 161; 
			snmpKey = NFProperties.SNMP_KEY;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Processor> getProcessor() {

		OID typeOid = new OID("1.3.6.1.2.1.25.3.2.1.2");
		OID LoadOid = new OID("1.3.6.1.2.1.25.3.3.1.2");

		ArrayList<Processor> procList = new ArrayList<Processor>();
	
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
			if (Pevents == null || Pevents.size() == 0) {
				return null;
			}
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
			if (Levents == null || Levents.size() == 0) {
				return null;
			}
			for (TreeEvent Levent : Levents) {
				VariableBinding[] lvbs = Levent.getVariableBindings();
				if (lvbs == null || lvbs.length == 0) {
					//System.out.println("VarBinding: No result returned.");
				}
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
				
				Processor p = new Processor(); 
				p.load = load;
				p.core = cpuIndex;
				
				procList.add(p);
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
		return procList;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<ArrayList<Object>> getStorage(){

		String oidString = MemoryOID.memoryOID;

		TransportMapping transport;
		Snmp snmp;
		ArrayList<ArrayList<Object>> responseList = new ArrayList<ArrayList<Object>>();
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
							HashMap<String,String> map = null;
							if (storageMap.containsKey(temp[1])) {
								map = storageMap.get(temp[1]);
							} else if (memoryMap.containsKey(temp[1])) {
								map = memoryMap.get(temp[1]);
							}
							map.put("total", value);
							storageMap.put(temp[1], map);
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
					ArrayList<Object> storageList = new ArrayList<Object>();
					for (String key : s) {
						HashMap<String,String> m = storageMap.get(key);
						String path = m.get("path");
						if (path.startsWith("/run") || path.startsWith("/sys")) {
							continue;
						}
						Storage storage = new Storage();
						storage.path = path;
						storage.totalStorage = Long.parseLong(m.get("total")) * Long.parseLong(m.get("alloc"));
						storage.usedStorage = Long.parseLong(m.get("used")) * Long.parseLong(m.get("alloc"));
						storage.freeStorage = storage.totalStorage
								- storage.usedStorage;
						storageList.add(storage);

					}
					responseList.add(storageList);

					Set<String> s1 = memoryMap.keySet();
					ArrayList<Object>	memoryList = new ArrayList<Object>();
					Memory memory = new Memory();
					for (String key : s1) {
						HashMap<String,String> m = memoryMap.get(key);
						String name = (String) m.get("name");
						if (name.equals("Physical memory")) {
							memory.totalphysicalMemory = Long.parseLong( m
									.get("total"))
									* Long.parseLong( m.get("alloc"));
							memory.usedPhysicalMemory = Long.parseLong( m
									.get("used"))
									* Long.parseLong( m.get("alloc"));
						} else if (name.equals("Cached memory")) {
							memory.usedCacheMemory = Long.parseLong( m
									.get("used"))
									* Long.parseLong( m.get("alloc"));
						} else if (name.equals("Memory buffers")) {
							memory.usedBufferMemory = Long.parseLong( m
									.get("used"))
									* Long.parseLong( m.get("alloc"));
						} else if (name.equals("Shared memory")) {
							memory.usedSharedMemory = Long.parseLong( m
									.get("used"))
									* Long.parseLong( m.get("alloc"));
						}memory.freePhysicalMemory = memory.totalphysicalMemory
								- memory.usedPhysicalMemory;
					}

					memoryList.add(memory);
					responseList.add(memoryList);
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
		return responseList;

	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Interface> getInterface() {
	
		OID IOid = new OID("1.3.6.1.2.1.2.2.1");
		TransportMapping transport;
		Snmp snmp;
		ArrayList<Interface> interfaceList = new ArrayList<Interface>();
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
				if (Ivbs == null || Ivbs.length == 0) {
					//System.out.println("VarBinding: No result returned.");

				}
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
					}else if (oid.startsWith(InterfaceOID.status)) {
						String temp[] = oid.split(InterfaceOID.status);
						if (interfaceMap.containsKey(temp[1])) {
							HashMap<String,String> map = interfaceMap.get(temp[1]);
							if(Integer.parseInt(value)==1){
								map.put("status", "UP");
							}else
							{
								map.put("status", "DOWN");
							}//map.put("status", value);
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
				if (Ivbs == null || Ivbs.length == 0) {
					//System.out.println("VarBinding: No result returned.");

				}
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
				Enumeration<NetworkInterface> nets = NetworkInterface
						.getNetworkInterfaces();
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
				interfaceList.add(interfaces);
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
		
		return interfaceList;
	}

}
