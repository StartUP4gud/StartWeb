package org.am.web.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
	
	public static BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public static String encode(String password) {
		return new BCryptPasswordEncoder().encode(password);
		
	}
	
	public static boolean decode(String password,String hash) {
		return new BCryptPasswordEncoder().matches(password, hash);	
		
	}	
	
	public static boolean matches(String passwd, String encodedPasswd) {
		return new BCryptPasswordEncoder().matches(passwd, encodedPasswd);
	}
}
