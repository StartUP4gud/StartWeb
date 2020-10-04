package org.am.web.config;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.am.web.entity.UserEntity;
import org.am.web.dao.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler  {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Autowired
	UsersDao userDao;

	@Override
	public void onAuthenticationFailure (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AuthenticationException exception) throws IOException, ServletException {
		
		String page = "/login";
		try {
			String username = httpServletRequest.getParameter ("username");
			UserEntity user = userDao.getUserDetailsByUserName (username);
			int condtionInt=4;
			if (user.failure_attempt_count == condtionInt){
				if(page != null) {
					page = "/denied";
				}
				userDao.blockUserAfterMaxAttempts(username);   
				System.out.println("Failure, crossed maximum attempts");
			}
			else{
				int tsLimit = 60000;
				if ((System.currentTimeMillis() - user.failure_timestamp.getTime()) <= tsLimit){
					userDao.updateFailureAttemptCount(username);
					System.out.println("Invalid username or password");
				}
				else{
					userDao.updateFailureTimestamp (new Timestamp(System.currentTimeMillis()), username);
					userDao.resetLoginAttemptCount (1, username);
					System.out.println("Invalid username or password");
				}
			}
		} catch(Exception ex) {

		}
		redirectStrategy.sendRedirect (httpServletRequest, httpServletResponse, page);
	}

}