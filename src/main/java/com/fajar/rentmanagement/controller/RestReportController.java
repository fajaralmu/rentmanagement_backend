package com.fajar.rentmanagement.controller;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fajar.rentmanagement.annotation.CustomRequestInfo;
import com.fajar.rentmanagement.dto.WebRequest;
import com.fajar.rentmanagement.exception.ApplicationException;
import com.fajar.rentmanagement.service.entity.EntityReportService;
import com.fajar.rentmanagement.service.report.CustomWorkbook;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping("/api/app/report")
@Slf4j
public class RestReportController extends BaseController {

	@Autowired
	private EntityReportService entityReportService;

	public RestReportController() {
		log.info("------------------RestReportController-----------------");
	}
 
	
	@PostMapping(value = "/records", consumes = MediaType.APPLICATION_JSON_VALUE)
	@CustomRequestInfo(withRealtimeProgress = true)
	public void recordsReport(@RequestBody WebRequest request, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		try {
			 
			log.info("entityreport {}", request);

			CustomWorkbook result = entityReportService.generateEntityReport(request, httpRequest);

			writeXSSFWorkbook(httpResponse, result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ApplicationException(e);
		}
	}
	
	public static void writeXSSFWorkbook(HttpServletResponse httpResponse, CustomWorkbook xwb) throws Exception {
		httpResponse.setContentType("text/xls");
		httpResponse.setHeader("Access-Control-Expose-Headers", "Content-disposition,access-token");
		httpResponse.setHeader("Content-disposition", "attachment;filename=" + xwb.getFileName());

		try (OutputStream outputStream = httpResponse.getOutputStream()) {
			xwb.write(outputStream);
		}
	}

}
