package com.fajar.rentmanagement.entity.setting;

import java.util.Set;

import com.fajar.rentmanagement.entity.Picture;

public interface MultipleImageModel {
	
	public void setPictures(Set<Picture> pictures);
	Set<Picture> getPictures();

}
