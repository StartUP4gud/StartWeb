package org.am.web.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.am.web.bean.StatsGraphBean;
import org.am.web.bean.StatsTableBean;
import org.am.web.utils.NFProperties;
import org.am.web.utils.Utils;
import org.springframework.stereotype.Service;


@Service
public class StatsReader {

	public ArrayList<StatsGraphBean> getGraphData(long from , long to, String type) {

		ArrayList<StatsGraphBean> statsList = new ArrayList<StatsGraphBean>();

		HashMap<String, StatsGraphBean> statsMap = new HashMap<String, StatsGraphBean>();

		ArrayList<String> files = Utils.getFileList(from, to);
		for(String file : files) {
			try {
				String path = NFProperties.SECONDARY_STORAGE_PATH + "stats/" + file + "/" + type + "/0_0.csv";

				byte[] buf = new byte[8192];
				InputStream inputStreamObj = Files.newInputStream(Paths.get(path));

				int len = inputStreamObj.read(buf);
				while(len > 0) {
					String line = new String (buf);
					//timestamp, field, value
					String fields[] = line.split(",");

					if(fields.length == 3) {
						long timestamp = Long.parseLong(fields[0]);
						String key =  fields[1];
						long value =  Long.parseLong(fields[2]);

						StatsGraphBean bean = statsMap.get(key);
						if(bean == null) {
							bean = new StatsGraphBean();
							bean.data = new ArrayList<long[]>();
							bean.id = key;
							bean.name = key;
						}
						long arr[] = new long[2];
						arr[0] = timestamp;
						arr[1] = value;
						bean.data.add(arr);

						statsMap.put(key, bean);
					}
					len = inputStreamObj.read(buf);
				}
				inputStreamObj.close();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}//for

		for(String key : statsMap.keySet()) {
			statsList.add(statsMap.get(key));
		}
		return statsList;
	}

	public ArrayList<StatsTableBean> getTableData(long from , long to, String type) {

		ArrayList<StatsTableBean> statsList = new ArrayList<StatsTableBean>();

		HashMap<String, StatsTableBean> statsMap = new HashMap<String, StatsTableBean>();

		ArrayList<String> files = Utils.getFileList(from, to);
		for(String file : files) {
			try {
				String path = NFProperties.SECONDARY_STORAGE_PATH + "stats/" + file + "/" + type + "/0_0.csv";
				System.out.println(path);

				byte[] buf = new byte[8192];
				InputStream inputStreamObj = Files.newInputStream(Paths.get(path));
				int len = inputStreamObj.read(buf);
				while(len > 0) {
					String line = new String (buf);
					String fields[] = line.split(",");

					if(fields.length == 3) {
						//long timestamp = Long.parseLong(fields[0]);
						String key =  fields[1];
						long value =  Long.parseLong(fields[2]);

						StatsTableBean bean = statsMap.get(key);
						if(bean == null) {
							bean = new StatsTableBean();
							bean.id = key;
							bean.name = key;
							bean.total = 0;
							bean.count = 0;
						}
						bean.total  = bean.total + value;
						bean.count = bean.count + 1;

						statsMap.put(key, bean);
					}
					len = inputStreamObj.read(buf);
				}
				inputStreamObj.close();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}//for

		for(String key : statsMap.keySet()) {
			statsList.add(statsMap.get(key));
		}
		return statsList;
	}

}
