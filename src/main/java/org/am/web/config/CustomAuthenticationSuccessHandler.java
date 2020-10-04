package org.am.web.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.am.web.bean.CurrentUser;
import org.am.web.bean.User;
import org.am.web.dao.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler  {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Autowired
	UsersDao userDao;
	
	@Autowired
	UserConfigManager userConfigManager;
	
	@Override
	public void onAuthenticationSuccess (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication)
			throws IOException, ServletException {
		HttpSession session = httpServletRequest.getSession();
		setUserInfo (authentication.getName(), session, httpServletRequest);
		DefaultSavedRequest defaultSavedRequest = (DefaultSavedRequest) httpServletRequest.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
			
		userDao.resetLoginAttemptCount(0, authentication.getName());
		
		if(defaultSavedRequest != null){
			redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, defaultSavedRequest.getRedirectUrl());
		}else{
			redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/");
		}
	}

	private void setUserInfo (String username, HttpSession session, HttpServletRequest request) {
		User user = userConfigManager.getUser (username);
		try {
			CurrentUser userBean = new CurrentUser();
			userBean.username = username;
			userBean.displayName = user.name;
			userBean.role = user.role;
			session.setAttribute("user", userBean);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
