package org.am.web.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utils {

	public static String getTodayDate(){ 		
		Date date = new Date(); 		
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd"); 		
		String df = ft.format(date);
				
		return df;		
	}

	public static String getMonth(){ 		
		Date date = new Date(); 		
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM"); 		
		return ft.format(date); 	
	}

	public static ArrayList<String> getFileList(long from, long to) {
	
		ArrayList<String> dateList = new ArrayList<String>();
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");	
		
		Date start = new Date(from);
		Date end = new Date(to);
		long interval = 24 * 1000 * 60 * 60;
		while (!start.after(end)) {
			String day = ft.format(start); 
			dateList.add(day);
			
		    start = new Date(start.getTime() + interval);
		}
				
		return dateList;
	}

	public static void  storeDataCsv(String text,String filPath){
		try {
			File file = new File(filPath);			
			FileWriter fileWriter;
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
				fileWriter =new FileWriter(file);
			}
			else{
				fileWriter =new FileWriter(file,true);
			}
			fileWriter.write(text);
			fileWriter.close();
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}	
}
