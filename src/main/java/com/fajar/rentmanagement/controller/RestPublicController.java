package com.fajar.rentmanagement.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fajar.rentmanagement.dto.WebRequest;
import com.fajar.rentmanagement.dto.WebResponse;
import com.fajar.rentmanagement.externalapp.DeploymentUtils;
import com.fajar.rentmanagement.service.CatalogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/public")
public class RestPublicController extends BaseController {
  
	@Autowired
	private CatalogService catalogService;
	
	public RestPublicController() {
		log.info("----------------------Rest Public Controller-------------------");
	}

	 
	@PostMapping(value = "/requestid", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse getRequestId(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		
		log.info("generate or update requestId }");
		WebResponse response = userSessionService.generateRequestId(httpRequest, httpResponse);
		return response;
	}
	
	@PostMapping(value = "/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse getProducts(@RequestBody WebRequest webRequest) throws IOException { 
		return catalogService.getProducts(webRequest);
	}
	
	/**
	 * 
	 * Testing
	 * @throws Exception 
	 * 
	 */
	@GetMapping("/rebuild")
	public WebResponse reBuildApp() throws Exception {
		DeploymentUtils.main(new String[] {});
		return new WebResponse();
	}
	 
	
}
