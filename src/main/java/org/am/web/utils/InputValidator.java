package org.am.web.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class InputValidator {
	private static Pattern VALID_IPV4_PATTERN = null;
	private static Pattern VALID_IPV6_PATTERN = null;
	private static Pattern VALID_PORT_PATTERN = null;
	private static Pattern VALID_NAME_PATTERN = null;
	private static Pattern VALID_FIELD_PATTERN = null;
	
	private static final String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
	private static final String ipv6Pattern = "([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}";
	private static final String portRegex = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
	private static final String namePattern = "[A-Za-z0-9_]+";
	private static final String fieldsPattern = "[A-Za-z0-9_,]+";
	
	static {
		try {
			VALID_IPV4_PATTERN = Pattern.compile(ipv4Pattern, Pattern.CASE_INSENSITIVE);
			VALID_IPV6_PATTERN = Pattern.compile(ipv6Pattern, Pattern.CASE_INSENSITIVE);
			VALID_PORT_PATTERN = Pattern.compile(portRegex, Pattern.CASE_INSENSITIVE);
			VALID_NAME_PATTERN = Pattern.compile(namePattern, Pattern.CASE_INSENSITIVE);
			VALID_FIELD_PATTERN = Pattern.compile(fieldsPattern, Pattern.CASE_INSENSITIVE);
				
		} catch (PatternSyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isIpAddress(final String ipAddress) {
		if(ipAddress.length()>100) {
			return false;
		}
		if (ipAddress == null || ipAddress.trim().equals("") || ipAddress.equals("NA"))
		{
			return true ;
		}
		String[] ips = null;
		if (ipAddress.contains(","))
		{
			ips = ipAddress.split (",");
		}
		else if (ipAddress.contains(" "))
		{
			ips = ipAddress.split (" ");
		}
		else
		{
			ips = new String[1];
			ips[0] = ipAddress;
		}
		boolean isIpAddress = true;
		for (String ip : ips)
		{
			Matcher m1 = InputValidator.VALID_IPV4_PATTERN.matcher(ip);
			Matcher m2 = InputValidator.VALID_IPV6_PATTERN.matcher(ip);
			if (!m1.matches() && !m2.matches()) {
				isIpAddress = false;
				break;
			}
		}
		return isIpAddress;
	}

	public static boolean validatePortNumber(String port){
		if(port.length()>100) {
			return false;
		}
		if (port == null || port.trim().equals("") || port.equals("NA"))
		{
			return true ;
		}
		return VALID_PORT_PATTERN.matcher(port).matches();
	}

	public static boolean isValidName(String name) {
		if(name.length()>100) {
			return false;
		}
		if (name == null || name.trim().equals(""))
		{
			return true ;
		}
		return VALID_NAME_PATTERN.matcher(name).matches();
	}
	
	public static boolean isValidPassword(String password) {
		if(password.length()>100) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public static boolean isValidDateRange (long from, long to) {
		if (from > to)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	public static boolean isValidField (String field) {
		if (field == null || field.trim().equals(""))
		{
			return true ;
		}
		return VALID_FIELD_PATTERN.matcher(field).matches();
	}
}
