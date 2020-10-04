package org.am.web.controller;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.am.web.bean.CurrentUser;
import org.am.web.dao.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MVController {

	@Autowired
	UsersDao _usersDao;
	
	@RequestMapping("/login")
	public String login() {

		return "login2";
	}
	
	@RequestMapping("/denied")
	public String accessDenied(final Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "denied";
	}
	
	@RequestMapping("/")
	public String root(final Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "index";
	}

	@RequestMapping("/index")
	public String index(final Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "index";
	}

	@RequestMapping("/report/ingress_bps")
	public String ingressBps(final Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "reports_ingress_bps";
	}
	
	@RequestMapping("/report/ingress_pps")
	public String ingressPps(final Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "reports_ingress_pps";
	}
	
	@RequestMapping("/report/egress_bps")
	public String egressBps(final Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "reports_egress_bps";
	}
	
	@RequestMapping("/report/egress_pps")
	public String egressPps(final Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "reports_egress_pps";
	}
	
	@RequestMapping("/report/egress_fps")
	public String egressFps(final Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "reports_egress_fps";
	}
	
	@RequestMapping("/report/drop_pps")
	public String dropPps(final Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "reports_drop_pps";
	}	
	
	/*
	@RequestMapping("/config/collector")
	public String collector (Principal principal, Model model, HttpSession session) {
		try
		{
			String loggedInUsername = principal.getName();			
			UserEntity loggedInUser = _usersDao.findByUsername(loggedInUsername);
			if (loggedInUser.role.equals("ADMIN")) {
				setModelAttributes(model, session);
				return "collector";
			}
		}
		catch (Exception e) {
		}
		return "index";
	}

	@RequestMapping("/config/user")
	public String user (Principal principal, Model model, HttpSession session) {
		try
		{
			String loggedInUsername = principal.getName();			
			UserEntity loggedInUser = _usersDao.findByUsername(loggedInUsername);
			if (loggedInUser.role.equals("ADMIN")) {
				setModelAttributes(model, session);
				return "user";
			}
		}
		catch (Exception e) {
		}
		return "index";
	}
	
	@RequestMapping("/config/template_config")
	public String templateconfig (Principal principal, Model model, HttpSession session) {
		try
		{
			String loggedInUsername = principal.getName();			
			UserEntity loggedInUser = _usersDao.findByUsername(loggedInUsername);
			if (loggedInUser.role.equals("ADMIN")) {
				setModelAttributes(model, session);
				return "template_config";
			}
		}
		catch (Exception e) {
		}
		return "index";
	}
	*/
	
	@RequestMapping("/devicestatus")
	public String devicestatus(Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "devicestatus";
	}

	@RequestMapping("/resetpassword")
	public String resetpassword(Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "resetpassword";
	}

	@RequestMapping("/netflow/internal")
	public String internal(Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "internal";
	}

	@RequestMapping("/netflow/external")
	public String external(Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "external";
	}

	@RequestMapping("/netflow/top_application")
	public String topApplication(final Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "top_application";
	}

	@RequestMapping("/netflow/top_country")
	public String topCountry(final Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "top_country";
	}

	@RequestMapping("/netflow/top_port")
	public String topPort(final Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "top_port";
	}

	@RequestMapping("/netflow/top_asn")
	public String topAsn(final Model model, HttpSession session) {
		setModelAttributes(model, session);
		return "top_asn";
	}
	
	@RequestMapping(value = {"/logout"}, method = RequestMethod.POST)
	public String logoutDo(HttpServletRequest request,HttpServletResponse response){
	HttpSession session= request.getSession(false);
	    SecurityContextHolder.clearContext();
	         session= request.getSession(false);
	        if(session != null) {
	            session.invalidate();
	        }
	        for(Cookie cookie : request.getCookies()) {
	            cookie.setMaxAge(0);
	        }

	    return "logout";
	}
	
	public void setModelAttributes (Model model, HttpSession session)
	{
		try
		{
			CurrentUser userBean = (CurrentUser)session.getAttribute("user");
			model.addAttribute("currUser", userBean);
		}
		catch (Exception e)
		{
		}
		
	}
}