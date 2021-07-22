package com.fajar.rentmanagement.service.config;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fajar.rentmanagement.constants.AuthorityType;
import com.fajar.rentmanagement.dto.WebRequest;
import com.fajar.rentmanagement.dto.WebResponse;
import com.fajar.rentmanagement.entity.Authority;
import com.fajar.rentmanagement.entity.User;
import com.fajar.rentmanagement.repository.AuthorityRepository;
import com.fajar.rentmanagement.repository.EntityRepository;
import com.fajar.rentmanagement.repository.UserRepository;
import com.fajar.rentmanagement.service.SessionValidationService;
import com.fajar.rentmanagement.service.resources.ImageUploadService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultUserService {
	@Autowired
	private UserRepository userRepository;  
	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private SessionValidationService sessionValidationService;  
	@Autowired
	private EntityRepository entityRepository;
	
	@Autowired
	private ImageUploadService imageUploadService;
	
	
	private BCryptPasswordEncoder passwordEncoder;
	
	public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	@PostConstruct
	public void init() {
		try {
			checkUserAuthorities();
			checkUser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * don't invoke this method when tables has not been created yet
	 */
	private void checkUserAuthorities() {
		Authority auth = authorityRepository.findTop1ByName(AuthorityType.ROLE_ADMIN);
		if (null == auth) {
			auth = Authority.createAdmin();
			authorityRepository.save(auth);
		}
		Authority authUser = authorityRepository.findTop1ByName(AuthorityType.ROLE_USER);
		if (null == authUser) {
			authUser = Authority.createUser();
			authorityRepository.save(authUser);
		}
	}

	/**
	 * don't invoke this method when tables has not been created yet
	 */
	private void checkUser() {
		log.info("___________checkUser");
		List<User> adminUser = userRepository.getByAuthority(AuthorityType.ROLE_ADMIN.toString());
		if (adminUser == null || adminUser.isEmpty()) {
			generateDefaultAdmin();
		}
	}
	
	private void generateDefaultAdmin() {
		Authority adminAuth = authorityRepository.findTop1ByName(AuthorityType.ROLE_ADMIN);
		if (null == adminAuth) {
			log.info("___________null == adminAuth");
			return;
		}
		
		User user = new User();
		Authority auth = new Authority();
		auth.setId(adminAuth.getId());
		user.addAuthority(auth );
		user.setPassword(passwordEncoder.encode("123"));
		user.setUsername("admin");
		user.setDisplayName("Application Admin");
		
		log.info("___________userRepository.save(user)");
		userRepository.save(user);
	}
	
	public WebResponse updateProfile(HttpServletRequest httpServletRequest, WebRequest webRequest) {
		log.info("Update profile");
		
		final User loggedUser = sessionValidationService.getLoggedUser(httpServletRequest);
		final User user = webRequest.getUser().toEntity();
		 
		updateUserData(loggedUser, user, httpServletRequest);
		
		WebResponse response = new WebResponse();
		response.setUser(loggedUser.toModel());
		return response;
	}

	private void updateUserData(User loggedUser, User user, HttpServletRequest httpServletRequest) {
		if (user.getUsername() != null && !user.getUsername().isEmpty()) {
			loggedUser.setUsername(user.getUsername());
		}
		if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
			loggedUser.setDisplayName(user.getDisplayName());
		}
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			loggedUser.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		
		if (user.getPictures() != null && !user.getPictures().isEmpty()) {
			loggedUser.setPictures(user.getPictures());
			imageUploadService.updateImages(user, loggedUser, httpServletRequest);
		}
		entityRepository.save(loggedUser);
	}
}
