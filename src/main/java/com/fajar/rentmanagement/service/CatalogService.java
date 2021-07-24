package com.fajar.rentmanagement.service;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.rentmanagement.dto.WebRequest;
import com.fajar.rentmanagement.dto.WebResponse;
import com.fajar.rentmanagement.dto.model.BaseModel;
import com.fajar.rentmanagement.entity.Product;
import com.fajar.rentmanagement.service.entity.CommonFilterResult;
import com.fajar.rentmanagement.service.entity.MasterDataService;
import com.fajar.rentmanagement.service.entity.ProductUpdateService;

@Service
public class CatalogService {
	
	@Autowired
	private MasterDataService masterDataService;
	@Autowired
	private ProductUpdateService productUpdateService;

	public WebResponse getProducts(WebRequest request) {
		CommonFilterResult<Product> result = masterDataService.filterEntities(request.getFilter(), Product.class);
		productUpdateService.postFilter(result.getEntities());
		return WebResponse.builder() 
				.entities(BaseModel.toModels(result.getEntities()))
				.totalData(result.getCount()).filter(request.getFilter()).build();
	}

	
}
