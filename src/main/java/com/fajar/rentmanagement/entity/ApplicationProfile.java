package com.fajar.rentmanagement.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fajar.rentmanagement.annotation.CustomEntity;
import com.fajar.rentmanagement.annotation.Dto;
import com.fajar.rentmanagement.constants.FontAwesomeIcon;
import com.fajar.rentmanagement.dto.model.ApplicationProfileModel;
import com.fajar.rentmanagement.entity.setting.MultipleImageModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Dto(ignoreBaseField = false, updateService = "shopProfileUpdateService")
@CustomEntity
@Entity
@Table(name = "shop_profile")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationProfile extends BaseEntity<ApplicationProfileModel>  implements MultipleImageModel{

	/**
	* 
	*/
	private static final long serialVersionUID = 4095664637854922384L;
	@Column  
	private String name;
	@Column(name = "mart_code", unique = true)
	private String appCode;
	@Column(name = "short_description")
	private String shortDescription;
	@Column
	private String about;
	@Column(name = "welcoming_message")
	private String welcomingMessage;
	@Column
	private String address;

	@Column
	private String contact;
	@Column
	private String website;
	 
	@Column(name= "footer_icon_class")
	@Enumerated(EnumType.STRING) 
	private FontAwesomeIcon footerIconClass; 
	
	@Column(name = "general_color")
	private String color;
	@Column(name = "font_color")
	private String fontColor;
	
	@Default
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) 
	@JoinTable(name = "application_profile_pictures", 
			joinColumns = { @JoinColumn(name = "application_profile_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "picture_id") }) 
	private Set<Picture> pictures = new HashSet<>();
	
	@Transient
	private String assetsPath;
	
	@JsonIgnore
	@Transient
	private String FooterIconClassValue;
	public String getFooterIconClassValue() {
		if(null == footerIconClass) {
			return "fa fa-home"; 
		}
		return footerIconClass.value;
	}
	 
	 
	@Override
	public void addPicture(Picture picture) {
		validatePictures();
		this.pictures.add(picture);
		
	}
	
	

}
