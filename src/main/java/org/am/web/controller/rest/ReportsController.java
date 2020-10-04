package org.am.web.controller.rest;

import java.security.Principal;
import java.util.ArrayList;

import org.am.web.bean.StatsGraphBean;
import org.am.web.bean.StatsTableBean;
import org.am.web.service.StatsReader;
import org.am.web.utils.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportsController {

	@Autowired
	StatsReader _stats;
	
	@RequestMapping(value="/rest/report/get/graph", method=RequestMethod.POST)
	public ArrayList<StatsGraphBean> getGraphData(@RequestParam(value="from", required=true) long from,
			@RequestParam(value="to", required=true) long to,
			@RequestParam(value="type", required=true) String type, Principal principall){
	
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
	
	@RequestMapping(value="/rest/report/get/table", method=RequestMethod.POST)
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
}
