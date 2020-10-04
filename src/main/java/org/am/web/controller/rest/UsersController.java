package org.am.web.controller.rest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.am.web.bean.User;
import org.am.web.dao.UsersDao;
import org.am.web.entity.UserEntity;
import org.am.web.utils.InputValidator;
import org.am.web.utils.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

	@Autowired 
	UsersDao _usersDao;

	@RequestMapping(value="/rest/user/list", method=RequestMethod.POST)
	public ArrayList<User> list( Principal principal ){
		ArrayList<User> userList  = new ArrayList<User>();
		try {
			String username = principal.getName();			
			UserEntity loggedInUser = _usersDao.findByUsername(username);
			if(loggedInUser.role.equals("ADMIN")) {

				List<UserEntity> users = _usersDao.findAll();
				for(UserEntity userObj : users) {
					User user = new User();
					user.name = userObj.name;
					user.id = userObj.id;
					user.role = userObj.role;
					user.username = userObj.username;
					userList.add(user);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}

	@RequestMapping(value="/rest/user/add", method=RequestMethod.POST)
	public String add(@RequestParam(value="username", required=true) String username,
			@RequestParam(value="name", required=true) String name,
			@RequestParam(value="password", required=true) String password,
			@RequestParam(value="role", required=true) String role, Principal principal){
		try {
			if(!InputValidator.isValidName(username)
					||!InputValidator.isValidName(name)
					||!InputValidator.isValidPassword(password)
					||!InputValidator.isValidName(role))
			{
				return "Invalid input";
			}
			UserEntity user = new UserEntity();
			user.name = name;
			user.username = username;
			user.role = role;
			user.password = PasswordEncoder.encode(password);
			user.enabled = 1;

			String loggedInUsername = principal.getName();			
			UserEntity loggedInUser = _usersDao.findByUsername(loggedInUsername);
			if(loggedInUser.role.equals("ADMIN")) {
				_usersDao.saveAndFlush(user);
				return "SUCESS";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "FAILED";
	}


	@RequestMapping(value="/rest/user/delete", method=RequestMethod.POST)
	public String delete(@RequestParam(value="id", required=true) Integer id, Principal principal){
		try {
			UserEntity userObj = new UserEntity();
			userObj.id = id;

			String username = principal.getName();			
			UserEntity loggedInUser = _usersDao.findByUsername(username);
			if(loggedInUser.role.equals("ADMIN")) {
				
				String inputUsername = _usersDao.findByID(id).username;
				if( !inputUsername.equals("admin") ) {
					_usersDao.delete(userObj);
					return "SUCESS";
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "FAILED";
	}

	@RequestMapping(value="/rest/user/get", method=RequestMethod.POST)
	public User get(@RequestParam(value="id", required=false) Integer id, @RequestParam(value="username", required=true) String username, Principal principal){
		User user = new User();
		try {
			if(!InputValidator.isValidName(username))
			{
				return user;
			}
			UserEntity userObj = null;
			
			String loggedInUsername = principal.getName();			
			UserEntity loggedInUser = _usersDao.findByUsername(loggedInUsername);
			
			if(id != null && id == loggedInUser.id && userObj == null) {
				userObj= _usersDao.findByID(id);

			} else if(username != null && username.equals(loggedInUsername) && userObj == null) {
				userObj= _usersDao.findByUsername(username);
			}		
			user.name = userObj.name;
			user.id = userObj.id;
			user.role = userObj.role;
			user.username = userObj.username;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@RequestMapping(value="/rest/user/password/change", method=RequestMethod.POST)
	public String changePasswd(@RequestParam(value="old", required=true) String oldPasswd, @RequestParam(value="new", required=true) String newPasswd, Principal principal){
		try {
			if (!InputValidator.isValidPassword(newPasswd) || !InputValidator.isValidPassword(oldPasswd))
			{
				return "Invalid input";
			}
			String username = principal.getName();			
			UserEntity userObj = _usersDao.findByUsername(username);

			if(PasswordEncoder.matches(oldPasswd, userObj.password)) {
				userObj.password = PasswordEncoder.encode(newPasswd);
				_usersDao.saveAndFlush(userObj);
			}
			else{
				return "FAILED";
			}
			return "SUCESS";
		} catch (Exception e) {
			e.printStackTrace();
			return "FAILED";
		}
	}
}
