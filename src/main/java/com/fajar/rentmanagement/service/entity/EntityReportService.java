package com.fajar.rentmanagement.service.entity;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.rentmanagement.dto.WebRequest;
import com.fajar.rentmanagement.dto.WebResponse;
import com.fajar.rentmanagement.dto.model.BaseModel;
import com.fajar.rentmanagement.entity.BaseEntity;
import com.fajar.rentmanagement.entity.User;
import com.fajar.rentmanagement.entity.setting.EntityProperty;
import com.fajar.rentmanagement.service.ProgressService;
import com.fajar.rentmanagement.service.SessionValidationService;
import com.fajar.rentmanagement.service.report.CustomWorkbook;
import com.fajar.rentmanagement.service.report.EntityReportBuilder;
import com.fajar.rentmanagement.service.report.ProgressNotifier;
import com.fajar.rentmanagement.util.EntityPropertyBuilder;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EntityReportService {
 
	@Autowired
	private ProgressService progressService;
	@Autowired
	private SessionValidationService sessionValidationService;
	@Autowired
	private MasterDataService masterDataService;

	public CustomWorkbook getEntityReport(List<? extends BaseModel> entities, Class<? extends BaseEntity> entityClass,
			HttpServletRequest httpRequest) throws Exception {
		log.info("Generate entity report: {}", entityClass); 
		User currentUser = sessionValidationService.getLoggedUser(httpRequest);
		String requestId = currentUser.getRequestId();
		Class modelClass = BaseEntity.getModelClass(entityClass);
		
		EntityProperty entityProperty = EntityPropertyBuilder.createEntityProperty(modelClass, null);
//		ReportData reportData = ReportData.builder().entities(entities).entityProperty(entityProperty).requestId(requestId).build(); 
	
		EntityReportBuilder reportBuilder = new EntityReportBuilder( entityProperty, entities, requestId);
		reportBuilder.setProgressNotifier(notifier(httpRequest));
		
		progressService.sendProgress(1, 1, 10, false, httpRequest);

		CustomWorkbook file = reportBuilder.buildReport(); 
		
		log.info("Entity Report generated");

		return file;
	}
	
	private ProgressNotifier notifier(final HttpServletRequest httpServletRequest) {

		return new ProgressNotifier() {

			@Override
			public void notify(int progress, int maxProgress, double percent) {
				progressService.sendProgress(progress, maxProgress, percent, httpServletRequest);

			}
		};
	}

	 public CustomWorkbook generateEntityReport(WebRequest request, HttpServletRequest httpRequest) throws Exception {
         Objects.requireNonNull(request);
         log.info("generateEntityReport, request: {}", request);

         WebResponse response = masterDataService.filter(request, null);
         progressService.sendProgress(1, 1, 20, true, httpRequest);
         CustomWorkbook file = this.getEntityReport(response.getEntities(), response.getEntityClass(), httpRequest);
         return file;
 }


}
