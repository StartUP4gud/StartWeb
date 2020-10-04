package org.am.web.controller.rest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.am.web.dao.CollectorDao;
import org.am.web.dao.UsersDao;
import org.am.web.entity.CollectorEntity;
import org.am.web.entity.UserEntity;
import org.am.web.utils.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollectorController {

	@Autowired 
	UsersDao _usersDao;

	@Autowired 
	CollectorDao _collectorDao;

	@RequestMapping(value="/rest/collector/list", method=RequestMethod.POST)
	public List<CollectorEntity> list(){
		List<CollectorEntity> collectors = new ArrayList<CollectorEntity>();
		try {
			collectors = _collectorDao.findAll();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return  collectors;
	}

	@RequestMapping(value="/rest/collector/add", method=RequestMethod.POST)
	public String add(@RequestParam(value="hostname", required=true) String hostname,
			@RequestParam(value="ip", required=true) String ip,
			@RequestParam(value="version", required=true) String version,
			@RequestParam(value="protocol", required=true) String protocol,
			@RequestParam(value="port", required=true) Integer port,
			@RequestParam(value="sampling_rate", required=true) Integer samplingRate,
			@RequestParam(value="template_id", required=false) Integer templateId,
			@RequestParam(value="export_timeout", required=false) Integer exportTimeout, Principal principal){
		try {
			if(!InputValidator.isValidName(hostname)
					||!InputValidator.isIpAddress(ip)
					||!InputValidator.isValidName(version)
					||!InputValidator.validatePortNumber(port.toString())
					||!InputValidator.isValidName(protocol)
					||!InputValidator.isValidName(samplingRate.toString()))
			{
				return "Invalid input";
			}
			CollectorEntity collector = new CollectorEntity();
			collector.hostname = hostname;
			collector.ip = ip;
			collector.protocol = protocol;
			collector.port = port;
			collector.sampling_rate = samplingRate;
			collector.template_id = templateId;	
			collector.version = version;	
			//collector.export_timeout = export_timeout;
			collector.enabled = 1;

			String loggedInUsername = principal.getName();			
			UserEntity loggedInUser = _usersDao.findByUsername(loggedInUsername);
		
			if(loggedInUser.role.equals("ADMIN")) {
				_collectorDao.saveAndFlush(collector);
				return "SUCESS";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "FAILED";
	}


	@RequestMapping(value="/rest/collector/delete", method=RequestMethod.POST)
	public String delete(@RequestParam(value="id", required=true) Integer id, Principal principal){
		try {
			CollectorEntity collector = new CollectorEntity();
			collector.id = id;

			String username = principal.getName();			
			UserEntity loggedInUser = _usersDao.findByUsername(username);
		
			if(loggedInUser.role.equals("ADMIN")) {
				_collectorDao.delete(collector);
				return "SUCESS";
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "FAILED";
	}

	@RequestMapping(value="/rest/collector/get", method=RequestMethod.POST)
	public CollectorEntity get(@RequestParam(value="id", required=false) Integer id){
		CollectorEntity collector = new CollectorEntity();
		try {
			if(id != null) {
				collector  = _collectorDao.findByID(id);

			} 				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return collector;
	}
}
