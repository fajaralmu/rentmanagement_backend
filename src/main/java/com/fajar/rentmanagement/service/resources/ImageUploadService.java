package com.fajar.rentmanagement.service.resources;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.rentmanagement.entity.BaseEntity;
import com.fajar.rentmanagement.entity.Picture;
import com.fajar.rentmanagement.entity.setting.MultipleImageModel;
import com.fajar.rentmanagement.repository.EntityRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageUploadService {
	@Autowired
	private FileService fileService;
	@Autowired
	private EntityRepository entityRepository;
	@Autowired
	private ImageRemovalService imageRemovalService;

	 
	private void removeOldImage(MultipleImageModel model) {
		BaseEntity existingRecord = entityRepository.findById(((BaseEntity)model).getClass(), model.getId());
		if (null == existingRecord) {
			return;
		}
		MultipleImageModel existingImageModel = (MultipleImageModel) existingRecord;
		for (Picture picture : existingImageModel.getPictures()) {
			entityRepository.deleteById(picture.getId(), Picture.class);
			imageRemovalService.removeImage(picture.getName());
		}
	}

	/**
	 * upload multiple images
	 * 
	 * @param multipleImageModel
	 * @param httpServletRequest
	 * @return
	 */
	public Set<Picture> writeNewImages(MultipleImageModel multipleImageModel, HttpServletRequest httpServletRequest) {
		Set<Picture> pictures = multipleImageModel.getPictures();
		if (pictures == null || pictures.size() == 0) {
			return null;
		}
		Set<Picture> savedPictures = new HashSet<>();
		
		for (Picture picture : pictures) {
			String base64Image = picture.getBase64Data();
			
			if (base64Image == null || base64Image.equals(""))
				continue;
			
			try {
				String imageName = fileService.writeImage(multipleImageModel.getClass().getSimpleName(), base64Image,
						httpServletRequest);
				if (null != imageName) {
					savedPictures.add(picture.withName(imageName));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (0 == savedPictures.size()) {
			return null;
		}

		multipleImageModel.setPictures(savedPictures);

		return savedPictures;
	}

	/**
	 * update multiple images
	 * 
	 * @param multipleImageModel
	 * @param exixstingMultipleImageModel
	 * @param httpServletRequest
	 * @return
	 */
	public Set<Picture> updateImages(MultipleImageModel multipleImageModel, MultipleImageModel exixstingMultipleImageModel,
			HttpServletRequest httpServletRequest) {
		final Set<Picture> rawImageList = multipleImageModel.getPictures();
		if (rawImageList == null || rawImageList.size() == 0 || exixstingMultipleImageModel == null) {
			return null;
		}
		final Set<Picture> saved = new HashSet<>();
		// loop
		log.info("rawImageList length: {}", rawImageList.size());
		for (Picture picture : rawImageList) {
			if (isBase64Image( picture.getBase64Data())) {
				try {
					String imageName = fileService.writeImage(multipleImageModel.getClass().getSimpleName(), picture.getBase64Data(), httpServletRequest);
					log.info("saved base64 image {}", imageName);
					saved.add(picture.withName(imageName));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				if (picture.getId() != null) {
					Picture pictureRecord = entityRepository.findById(Picture.class, picture.getId());
					if (null != pictureRecord ) {
						saved.add(pictureRecord);
					}
				}
			}
 
		}
		if (saved.size() == 0) {
			return null;
		}
		log.info("saved pictures: {}", saved.size());
		multipleImageModel.setPictures(saved);

		return saved;
	}

	private boolean pictureExist(String imageName, Set<Picture> pictures) {
		for (Picture picture : pictures) {
			if (imageName.equals(picture.getName())) {
				return true;
			}
		}

		return false;
	}

	private boolean isBase64Image(String rawImage) {

		return rawImage != null && rawImage.startsWith("data:image");
	}
}
