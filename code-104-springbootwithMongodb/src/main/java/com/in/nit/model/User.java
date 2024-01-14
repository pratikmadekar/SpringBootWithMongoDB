package com.in.nit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;


import lombok.Data;

@Data
@Document(collection="User")
public class User {

	@Id
	private String id;

	private String userName;
	private String designation;

	private Double salary;
	private String photo;

	@Transient
	private MultipartFile imageFile;

	@Transient
	private String userImage;


}
