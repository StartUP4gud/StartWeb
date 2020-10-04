package org.am.web.config;

import org.am.web.bean.User;
import org.am.web.dao.UsersDao;
import org.am.web.entity.UserEntity;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserConfigManager {

	@Autowired 
	UsersDao _usersDao;


	public User getUser(String username) {

		User user= new User();
		try {
			UserEntity userEntity = _usersDao.getUserDetailsByUserName(username);

			user.username = userEntity.username;
			user.name = userEntity.name;
			user.role = userEntity.role;
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return user;
	}

	public void loginEnableAfterMaxAttempts(Exchange e) {
		try {
			_usersDao.enableUserStatus();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
}
