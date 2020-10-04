package org.am.web.controller.rest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.am.web.dao.TemplateDao;
import org.am.web.dao.UsersDao;
import org.am.web.entity.TemplateEntity;
import org.am.web.entity.UserEntity;
import org.am.web.utils.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TemplateController {

	@Autowired 
	UsersDao _usersDao;
	
	@Autowired 
	TemplateDao _templateDao;

	@RequestMapping(value="/rest/template/list", method=RequestMethod.POST)
	public List<TemplateEntity> list(@RequestParam(value="version", required=false) String version){
		List<TemplateEntity> templates = new ArrayList<TemplateEntity>();
		try {
			if(version == null || version.equals("")) {
				templates = _templateDao.findAll();
			} else {
				templates = _templateDao.findByVersion(version);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return  templates;
	}

	@RequestMapping(value="/rest/template/add", method=RequestMethod.POST)
	public String add(@RequestParam(value="name", required=true) String name,
			@RequestParam(value="fields", required=true) String fields,
			@RequestParam(value="apps", required=true) String apps,
			@RequestParam(value="version", required=true) String version, Principal principal){
		try {
			if(!InputValidator.isValidName(name)
					|| !InputValidator.isValidName(version)
					|| !InputValidator.isValidField(fields)
					|| !InputValidator.isValidField(apps))
			{
				return "Invalid input";
			}
			
			if(!version.equals("json") && !version.equals("v9") && !version.equals("ipfix")) {
				return "Invalid input";
			}
			
			TemplateEntity template = new TemplateEntity();
			template.name = name;
			template.fields = fields;
			template.apps = apps;
			template.version = version;

			String loggedInUsername = principal.getName();			
			UserEntity loggedInUser = _usersDao.findByUsername(loggedInUsername);
		
			if(loggedInUser.role.equals("ADMIN")) {
				_templateDao.saveAndFlush(template);
				return "SUCESS";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "FAILED";
	}


	@RequestMapping(value="/rest/template/delete", method=RequestMethod.POST)
	public String delete(@RequestParam(value="id", required=true) Integer id, Principal principal){
		try {
			TemplateEntity template = new TemplateEntity();
			template.id = id;

			String username = principal.getName();			
			UserEntity loggedInUser = _usersDao.findByUsername(username);
		
			if(loggedInUser.role.equals("ADMIN")) {
				_templateDao.delete(template);
				return "SUCESS";
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "FAILED";
	}

	@RequestMapping(value="/rest/template/get", method=RequestMethod.POST)
	public TemplateEntity get(@RequestParam(value="id", required=false) Integer id){
		TemplateEntity template = new TemplateEntity();
		try {
			if(id != null) {
				template  = _templateDao.findByID(id);

			} 				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return template;
	}
}
