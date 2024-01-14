package com.in.nit.controller.rest;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.in.nit.config.FileUploadUtil;
import com.in.nit.exception.UserNotFoundException;
import com.in.nit.model.User;
import com.in.nit.service.IUserService;

@RestController
public class UserRestController {

	@Value("${app.user-photo}")
	private String USER_PHOTO;

	@Autowired
	private IUserService service;

	@GetMapping("/user")
	public ResponseEntity<?> getAllUser(){
		ResponseEntity<?> resp =null;
		List<User> list =null;
		try {
			list = service.getAllUser();
			Predicate<List<User>> p = x-> x!=null && !x.isEmpty();
			if(p.test(list)) {
				resp = new ResponseEntity<List<User>>(list,HttpStatus.OK);
			}else {
				resp= new ResponseEntity<String>("Data not found",HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			resp = new ResponseEntity<String>("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@PostMapping("/user")
	public ResponseEntity<?> registerUser(
			@RequestParam("userName") String  userName,
			@RequestParam("designation") String designation,
			@RequestParam("salary") Double salary,

			@RequestParam("imageFile") MultipartFile file

			){
		ResponseEntity<?> resp = null;
		
		User user = new User();
		User savedUser = null;
		try {
			user.setUserName(userName);
			user.setDesignation(designation);
			user.setSalary(Double.valueOf(salary));
			if (!file.isEmpty()) {
				String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
				user.setPhoto(fileName);
				savedUser = service.regsiterUser(user);
				String uploadDir = USER_PHOTO+savedUser.getId();
				FileUploadUtil.cleanDir(uploadDir);				
				FileUploadUtil.saveFile(uploadDir, fileName, file);
				resp = new	ResponseEntity<User>(savedUser,HttpStatus.OK);
			}else {
				user.setPhoto("default.png");
				savedUser = service.regsiterUser(user);
				resp = new	ResponseEntity<User>(savedUser,HttpStatus.OK);
			}
			savedUser = service.regsiterUser(user);
			Predicate<User> p = x->x!=null;
			if(p.test(user)) {
				resp = new ResponseEntity<User>(user,HttpStatus.CREATED);
			}else {
				resp = new ResponseEntity<String>("Unable to register",HttpStatus.OK);
			}
		} catch (Exception e) {
			resp = new ResponseEntity<String>("Internal Server Eror",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<?> getOneUser(@PathVariable("id") String id){
		ResponseEntity<?> resp = null;
		User user =null;
		try {
			user = service.getOneUser(id);
			resp = new  ResponseEntity<User>(user,HttpStatus.OK);
		} catch (UserNotFoundException e) {
			resp = new ResponseEntity<String>("User not found with id="+id,HttpStatus.OK);
		}catch (Exception e) {
			resp = new ResponseEntity<String>("Internal Server Eror",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@PostMapping("/user/update/{id}")
	public ResponseEntity<?> updateUser( @PathVariable("id") String id, 

			@RequestParam("userName") String  userName,
			@RequestParam("designation") String designation,
			@RequestParam("salary") String salary,

			@RequestParam("imageFile") MultipartFile file
			){
		ResponseEntity<?> resp = null;
		User existingUser = null;
		User user = new User();
		System.out.println("updateUser 116");
		try {
			User userUpdated = null;
			System.out.println("updateUser 116"+id);
			existingUser = service.getOneUser(id);
			System.out.println("updateUser 120"+existingUser.toString());
			existingUser.setId(id);
			existingUser.setUserName(userName);
			existingUser.setDesignation(designation);
			existingUser.setSalary(Double.valueOf(salary));
			System.out.println("updateUser 122");
			if (!file.isEmpty()) {
				String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
				user.setPhoto(fileName);
				//existingUser = service.getOneUser(id);
				Predicate<User> p = x->x!=null;
				System.out.println("updateUser 128");
				if(p.test(existingUser)) {
					existingUser.setPhoto(fileName);
					existingUser.setUserImage(null);
					System.out.println("updateUser 132");
					userUpdated = service.updateUser(existingUser);
					System.out.println("updateUser 134");
				}
				String uploadDir = USER_PHOTO+existingUser.getId();
				FileUploadUtil.cleanDir(uploadDir);				
				FileUploadUtil.saveFile(uploadDir, fileName, file);
				resp = new	ResponseEntity<User>(userUpdated,HttpStatus.OK);
			}else {
				/// have to handle the condition for default photo for mens and womens(gender specific)
				existingUser.setPhoto("default.png");
				//	String uploadDir = USER_PHOTO+existingUser.getId();
				//	FileUploadUtil.cleanDir(uploadDir);	
				userUpdated = service.updateUser(existingUser);
				resp = new	ResponseEntity<User>(userUpdated,HttpStatus.OK);
			}

		}catch (UserNotFoundException e) {
			resp = new ResponseEntity<String>("User not found with id="+id,HttpStatus.OK);
		}catch (Exception e) {
			resp = new ResponseEntity<String>("Internal Server Eror",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable String id){
		ResponseEntity<?> resp = null;
		try {
			service.deleteUser(id);
			resp = new ResponseEntity<String>("User Deleted Successfully",HttpStatus.OK);
		}catch (UserNotFoundException e) {
			resp = new ResponseEntity<String>("User not found with id="+id,HttpStatus.OK);
		}catch (Exception e) {
			resp = new ResponseEntity<String>("Internal Server Eror",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}

}