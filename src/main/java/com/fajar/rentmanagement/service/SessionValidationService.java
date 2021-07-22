package com.fajar.rentmanagement.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.fajar.rentmanagement.config.security.UserDetailDomain;
import com.fajar.rentmanagement.entity.User;
import com.fajar.rentmanagement.util.HttpRequestUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SessionValidationService {

	public boolean validatePrinciple(Object principal) {
//		log.info("principal=====> {}", principal);
		 
		if (principal instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
			if (usernamePasswordAuthenticationToken.getPrincipal() instanceof UserDetailDomain == false) {
				log.error("usernamePasswordAuthenticationToken.getPrincipal() is not instance of UserDetailDomain");
				return false;
			}
			//throw new IllegalArgumentException("Principal can not be null!");
			return true;
		}
		log.error("Principal is not instance of UsernamePasswordAuthenticationToken");
		return false;
	}
	public User getLoggedUser(HttpServletRequest request) {
		if (validatePrinciple(request.getUserPrincipal()) == false) {
			return null;
		}
		User user = ((UserDetailDomain) getUserPrincipal(request)).getUserDetails();
		user.setRequestId(HttpRequestUtil.getPageRequestId(request));
		return user;
	}
	public Object getUserPrincipal(HttpServletRequest request) {
		boolean validated = validatePrinciple(request.getUserPrincipal());
		if (validated) {
			return ((UsernamePasswordAuthenticationToken)request.getUserPrincipal()).getPrincipal();
		}
		return null;
	}

}
