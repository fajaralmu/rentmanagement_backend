package com.fajar.rentmanagement.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fajar.rentmanagement.annotation.CustomEntity;
import com.fajar.rentmanagement.dto.model.ProductModel;
import com.fajar.rentmanagement.dto.model.UserModel;
import com.fajar.rentmanagement.entity.setting.MultipleImageModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@CustomEntity
@Entity
@Table(name = "users")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity<UserModel> implements MultipleImageModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3896877759244837620L;
	@Column(unique = true) 
	private String username;
	@Column(name = "display_name") 
	private String displayName;
	@Column
	private String password;
 
	@Column(name = "profile_image")
	private String profileImage;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER) 
	@JoinTable(name = "user_authority", 
			joinColumns = { @JoinColumn(name = "user_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "authority_id") }) 
	@Default
	private Set<Authority> authorities = new HashSet<>();
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) 
	@JoinTable(name = "user_pictures", 
			joinColumns = { @JoinColumn(name = "user_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "picture_id") }) 
	@Default
	private Set<Picture> pictures = new HashSet<>();
	
	public void addPicture(Picture entity) {
		validatePictures();
		pictures.add(entity);
	}
	public void addAuthority(Authority authority) {
		authorities.add(authority);
	}
	 
	@Transient
	@JsonIgnore
	private String requestId;

	 
	@Override
	public UserModel toModel() {
		UserModel model = new UserModel();
		if (null != pictures) {
			pictures.forEach(p-> {
				model.addPicture(p.toModel());
			});
		}
		return copy(model, "pictures");
	}
	 
	 

}
