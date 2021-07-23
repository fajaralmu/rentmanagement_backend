package com.fajar.rentmanagement.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fajar.rentmanagement.annotation.CustomRequestInfo;
import com.fajar.rentmanagement.dto.WebRequest;
import com.fajar.rentmanagement.dto.WebResponse;
import com.fajar.rentmanagement.service.transaction.TransactionService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping("/api/app/transaction")
@Slf4j
public class RestTransactionController extends BaseController {

	@Autowired
	private TransactionService service; 

	public RestTransactionController() {
		log.info("------------------Rest Transaction Controller-----------------");
	} 

	@PostMapping(value = "/addstock", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CustomRequestInfo(withRealtimeProgress = true)
	public WebResponse addStock(@RequestBody WebRequest request, HttpServletRequest httpRequest) throws Exception {
		log.info("add Stock ");
		return service.addStock(request, httpRequest, true);
	}

	 

}
