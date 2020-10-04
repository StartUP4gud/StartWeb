package org.am.web.controller.rest;

import java.security.Principal;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.am.web.bean.StatsGraphBean;
import org.am.web.bean.StatsTableBean;
import org.am.web.service.StatsReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.am.web.snmp.SnmpResourceManager;
import org.am.web.snmp.bean.Interface;
import org.am.web.snmp.bean.Processor;
import org.am.web.utils.InputValidator;

@RestController
public class DeviceStatusController {

	@Autowired
	StatsReader _stats;
	
	@RequestMapping(value="/rest/device/status/get/graph", method=RequestMethod.POST)
	public ArrayList<StatsGraphBean> getGraphData(@RequestParam(value="from", required=true) long from,
			@RequestParam(value="to", required=true) long to,
			@RequestParam(value="type", required=true) String type, Principal principal){
		
		ArrayList<StatsGraphBean> stats = new ArrayList<StatsGraphBean>();	
		try {
			if (!InputValidator.isValidDateRange(from, to))
			{
				return stats;
			}
			stats =  _stats.getGraphData(from, to, type);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return stats;
	}
	
	@RequestMapping(value="/rest/device/status/get/table", method=RequestMethod.POST)
	public ArrayList<StatsTableBean> getTableData(@RequestParam(value="from", required=true) long from,
			@RequestParam(value="to", required=true) long to,
			@RequestParam(value="type", required=true) String type, Principal principal){
		
		ArrayList<StatsTableBean> stats = new ArrayList<StatsTableBean>();	
		try {
			if (!InputValidator.isValidDateRange(from, to))
			{
				return stats;
			}
			stats =  _stats.getTableData(from, to, type);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stats;
	}
	
	@RequestMapping(value="/rest/device/storage", method=RequestMethod.POST)
	public ArrayList<ArrayList<Object>> storage(HttpSession session) {
		ArrayList<ArrayList<Object>> list = new ArrayList<ArrayList<Object>>();
		try {
			list = SnmpResourceManager.getStorage();
		} catch(Exception ex) {
		}
		return list;
	}

	@RequestMapping(value="/rest/device/interfaces", method=RequestMethod.POST)
	public ArrayList<Interface> interfaces(HttpSession session) {
		ArrayList<Interface> list = new ArrayList<Interface>();
		try {
			list = SnmpResourceManager.getInterface();
		} catch(Exception ex) {
		}
		return list;
	}
	
	@RequestMapping(value="/rest/device/cpu", method=RequestMethod.POST)
	public ArrayList<Processor> resource(HttpSession session) {

		ArrayList<Processor> list = new ArrayList<Processor>();
		try {
			list = SnmpResourceManager.getProcessor();
		} catch(Exception ex) {

		}
		return list;
	}
}
