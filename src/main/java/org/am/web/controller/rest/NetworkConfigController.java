package org.am.web.controller.rest;

import java.net.SocketException;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.am.web.bean.NetworkInterface;
import org.am.web.utils.Network;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class NetworkConfigController {

	@RequestMapping(value="/rest/network/interface/list",method = RequestMethod.POST,headers="Accept=application/json")
	public ArrayList<NetworkInterface> getInterfaces(HttpSession session) throws SocketException{
		try {
			return Network.getInterfaceDetails();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
