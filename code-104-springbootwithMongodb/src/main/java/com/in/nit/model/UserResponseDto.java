package com.in.nit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.web.multipart.MultipartFile;

public class UserResponseDto {
	@Id
	private String id;
	private String userName;
	private String designation;
	private Double salary;
	private String photo;
	

}
