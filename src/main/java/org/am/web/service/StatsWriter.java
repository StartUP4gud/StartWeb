package org.am.web.service;

import java.util.HashMap;

import org.am.web.utils.NFProperties;
import org.am.web.utils.Utils;

public class StatsWriter {

	public static final String INGRESS_PACKETS = "IN_PKTS";
	public static final String INGRESS_BITS = "IN_BITS";
	public static final String EGRESS_PACKETS = "OUT_PKTS";
	public static final String EGRESS_BITS = "OUT_BITS";
	public static final String EGRESS_FLOWS = "OUT_FLOWS";
	public static final String DROP_PACKETS = "DROP_PKTS";

	private static HashMap<String, Long> ingressPacketsMap = new HashMap<String, Long>();
	private static HashMap<String, Long> ingressBitsMap = new HashMap<String, Long>();
	private static HashMap<String, Long> egressPacketsMap = new HashMap<String, Long>();
	private static HashMap<String, Long> egressBitsMap = new HashMap<String, Long>();
	private static HashMap<String, Long> egressFlowsMap = new HashMap<String, Long>();
	private static HashMap<String, Long> dropPacketsMap = new HashMap<String, Long>();

	private static HashMap<String, Long> ingressPackets10MinMap = new HashMap<String, Long>();
	private static HashMap<String, Long> ingressBits10MinMap = new HashMap<String, Long>();
	private static HashMap<String, Long> egressPackets10MinMap = new HashMap<String, Long>();
	private static HashMap<String, Long> egressBits10MinMap = new HashMap<String, Long>();
	private static HashMap<String, Long> egressFlows10MinMap = new HashMap<String, Long>();
	private static HashMap<String, Long> dropPackets10MinMap = new HashMap<String, Long>();

	public static void handleData(String data) {
		String[] entries = data.split(",");
		for(String entry: entries) {
			try {				
				String[] fields = entry.split(":");

				if(fields.length != 3) {
					return;
				}
				String type = fields[0].trim();
				String field = fields[1].trim();
				Long value = Long.parseLong(fields[2].trim());
				
				if(type.equals(INGRESS_PACKETS)) {
					Long mapValue = ingressPacketsMap.get(field);
					if(mapValue == null) {
						mapValue = 0l;
					}
					mapValue = mapValue + value;
					ingressPacketsMap.put(field, mapValue);

				} else if(type.equals(INGRESS_BITS)) {
					Long mapValue = ingressBitsMap.get(field);
					if(mapValue == null) {
						mapValue = 0l;
					}
					mapValue = mapValue + value;
					ingressBitsMap.put(field, mapValue);

				} else if(type.equals(EGRESS_PACKETS)) {
					Long mapValue = egressPacketsMap.get(field);
					if(mapValue == null) {
						mapValue = 0l;
					}
					mapValue = mapValue + value;
					egressPacketsMap.put(field, mapValue);

				} else if(type.equals(EGRESS_BITS)) {
					Long mapValue = egressBitsMap.get(field);
					if(mapValue == null) {
						mapValue = 0l;
					}
					mapValue = mapValue + value;
					egressBitsMap.put(field, mapValue);
				} else if(type.equals(EGRESS_FLOWS)) {
					Long mapValue = egressFlowsMap.get(field);
					if(mapValue == null) {
						mapValue = 0l;
					}
					mapValue = mapValue + value;
					egressFlowsMap.put(field, mapValue);
				} else if(type.equals(DROP_PACKETS)) {
					Long mapValue = dropPacketsMap.get(field);
					if(mapValue == null) {
						mapValue = 0l;
					}
					mapValue = mapValue + value;
					dropPacketsMap.put(field, mapValue);
				}

			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void trigger(){ 
		System.out.println("1 Minute: Stats Triggered...");
		try {
			HashMap<String, Long> ingressPacketsMapTemp = ingressPacketsMap;
			HashMap<String, Long> ingressBitsMapTemp = ingressBitsMap;
			HashMap<String, Long> egressPacketsMapTemp = egressPacketsMap;
			HashMap<String, Long> egressBitsMapTemp = egressBitsMap;
			HashMap<String, Long> egressFlowsMapTemp = egressFlowsMap;
			HashMap<String, Long> dropPacketsMapTemp = dropPacketsMap;

			ingressPacketsMap = new HashMap<String, Long>();
			ingressBitsMap = new HashMap<String, Long>();
			egressPacketsMap = new HashMap<String, Long>();
			egressBitsMap = new HashMap<String, Long>();
			egressFlowsMap = new HashMap<String, Long>();
			dropPacketsMap = new HashMap<String, Long>();

			long timestamp = System.currentTimeMillis();
			String dir = Utils.getTodayDate();

			writeToFile(INGRESS_PACKETS, timestamp, dir, 60, ingressPacketsMapTemp);
			writeToFile(INGRESS_BITS, timestamp, dir, 60, ingressBitsMapTemp);
			writeToFile(EGRESS_PACKETS, timestamp, dir, 60, egressPacketsMapTemp);
			writeToFile(EGRESS_BITS, timestamp, dir, 60, egressBitsMapTemp);
			writeToFile(EGRESS_FLOWS, timestamp, dir, 60, egressFlowsMapTemp);
			writeToFile(DROP_PACKETS, timestamp, dir, 60, dropPacketsMapTemp);

			saveTo10MinMap(INGRESS_PACKETS, ingressPacketsMapTemp);
			saveTo10MinMap(INGRESS_BITS,  ingressBitsMapTemp);
			saveTo10MinMap(EGRESS_PACKETS, egressPacketsMapTemp);
			saveTo10MinMap(EGRESS_BITS,  egressBitsMapTemp);
			saveTo10MinMap(EGRESS_FLOWS, egressFlowsMapTemp);
			saveTo10MinMap(DROP_PACKETS, dropPacketsMapTemp);

		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private void saveTo10MinMap(String type, HashMap<String, Long> map) {
		for(String field: map.keySet()) {

			Long value = map.get(field);

			if(type.equals(INGRESS_PACKETS)) {
				Long mapValue = ingressPackets10MinMap.get(field);
				if(mapValue == null) {
					mapValue = 0l;
				}
				mapValue = mapValue + value;
				ingressPackets10MinMap.put(field, mapValue);

			} else if(type.equals(INGRESS_BITS)) {
				Long mapValue = ingressBits10MinMap.get(field);
				if(mapValue == null) {
					mapValue = 0l;
				}
				mapValue = mapValue + value;
				ingressBits10MinMap.put(field, mapValue);

			} else if(type.equals(EGRESS_PACKETS)) {
				Long mapValue = egressPackets10MinMap.get(field);
				if(mapValue == null) {
					mapValue = 0l;
				}
				mapValue = mapValue + value;
				egressPackets10MinMap.put(field, mapValue);

			} else if(type.equals(EGRESS_BITS)) {
				Long mapValue = egressBits10MinMap.get(field);
				if(mapValue == null) {
					mapValue = 0l;
				}
				mapValue = mapValue + value;
				egressBits10MinMap.put(field, mapValue);
			} else if(type.equals(EGRESS_FLOWS)) {
				Long mapValue = egressFlows10MinMap.get(field);
				if(mapValue == null) {
					mapValue = 0l;
				}
				mapValue = mapValue + value;
				egressFlows10MinMap.put(field, mapValue);
			} else if(type.equals(DROP_PACKETS)) {
				Long mapValue = dropPackets10MinMap.get(field);
				if(mapValue == null) {
					mapValue = 0l;
				}
				mapValue = mapValue + value;
				dropPackets10MinMap.put(field, mapValue);
			}
		}
	}

	public void trigger10Min(){ 
		System.out.println("10 Minute: Stats Triggered...");
		try {
			HashMap<String, Long> ingressPacketsMapTemp = ingressPackets10MinMap;
			HashMap<String, Long> ingressBitsMapTemp = ingressBits10MinMap;
			HashMap<String, Long> egressPacketsMapTemp = egressPackets10MinMap;
			HashMap<String, Long> egressBitsMapTemp = egressBits10MinMap;
			HashMap<String, Long> egressFlowsMapTemp = egressFlows10MinMap;
			HashMap<String, Long> dropPackets10MinMapTemp = dropPackets10MinMap;

			ingressPackets10MinMap = new HashMap<String, Long>();
			ingressBits10MinMap = new HashMap<String, Long>();
			egressPackets10MinMap = new HashMap<String, Long>();
			egressBits10MinMap = new HashMap<String, Long>();
			egressFlows10MinMap = new HashMap<String, Long>();
			dropPackets10MinMap = new HashMap<String, Long>();

			long timestamp = System.currentTimeMillis();
			String dir = "month";

			writeToFile(INGRESS_PACKETS, timestamp, dir, 600, ingressPacketsMapTemp);
			writeToFile(INGRESS_BITS, timestamp, dir, 600, ingressBitsMapTemp);
			writeToFile(EGRESS_PACKETS, timestamp, dir, 600, egressPacketsMapTemp);
			writeToFile(EGRESS_BITS, timestamp, dir, 600, egressBitsMapTemp);
			writeToFile(EGRESS_FLOWS, timestamp, dir, 600, egressFlowsMapTemp);
			writeToFile(DROP_PACKETS, timestamp, dir, 600, dropPackets10MinMapTemp);

		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void writeToFile(String type, long timestamp, String dir, int seconds, HashMap<String, Long> map) {
		try {

			String path = null;
			if(dir.equals("month")) {
				path = NFProperties.SECONDARY_STORAGE_PATH + "stats/" + dir + "/" + type + "/" + Utils.getMonth() + "/0_0.csv";  
			} else {
				path = NFProperties.SECONDARY_STORAGE_PATH + "stats/" + dir + "/" + type + "/0_0.csv";
			}

			StringBuilder stringBuilder = new StringBuilder();
			for(String field: map.keySet()) {

				Long value = map.get(field);
				value = value / seconds; 

				stringBuilder.append(timestamp).append(",").append(field).append(",").append(value).append("\n");
			}
			Utils.storeDataCsv(stringBuilder.toString(), path);

		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
