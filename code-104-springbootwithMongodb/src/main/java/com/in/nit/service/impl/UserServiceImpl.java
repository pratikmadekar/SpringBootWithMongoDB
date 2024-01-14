package com.in.nit.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.in.nit.exception.UserNotFoundException;
import com.in.nit.model.User;
import com.in.nit.repository.UserRepository;
import com.in.nit.service.IUserService;

@Service
@Transactional
public class UserServiceImpl implements IUserService {
	//private static final String PHOTOS_DIR = "D://FileServer/sprinbootWithMongoDB/user-photos/";

	@Value("${app.user-photo}")
	private String USER_PHOTO;

	@Autowired
	private UserRepository repo;

	@Override
	public User regsiterUser(User user) {
		return repo.save(user);
	}

	@Override
	public List<User> getAllUser() {
		List<User> list = repo.findAll();
		Resource resource = null;
		List<User> userDtoList = new ArrayList<>();
		try {
			for(User user: list) {
				Path   filePath = Paths.get(USER_PHOTO, "".trim()+user.getId(), user.getPhoto());
				resource = new UrlResource(filePath.toUri());
				String base64Image = null;
				if (resource.exists() && resource.isReadable()) {			
					byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
					base64Image = Base64.getEncoder().encodeToString(imageBytes);
					//					logger.info("image data found"+category.getCategoryId());
					System.out.println(" image data");;
				} else {
					//					logger.info("no image data");
					System.out.println("no image data");;
				}
				// Convert image to base64

				User userResp = new User();
				userResp.setId(user.getId());
				userResp.setUserName(user.getUserName());
				userResp.setDesignation(user.getDesignation());
				userResp.setSalary(user.getSalary());
				userResp.setDesignation(user.getDesignation());
				userResp.setUserImage(base64Image);
				userDtoList.add(userResp);
			}
		} catch (MalformedURLException e) {
			System.out.println("file path not found"+e.getMessage());
		}catch (IOException e) {
			//logger.error("Exception"+e.getMessage());
			System.out.println(e.getMessage());
		}
		return userDtoList;
	}

	@Override
	public User updateUser(User user)throws UserNotFoundException {
		return repo.save(user);
	}

	@Override
	public User getOneUser(String id) throws UserNotFoundException {
		User user=  repo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		Resource resource = null;
		User userResp = null;
		try {
			Path   filePath=null;
			if(user.getPhoto()!=null && !user.getPhoto().isEmpty()) {
				filePath = Paths.get(USER_PHOTO, "".trim()+user.getId(), user.getPhoto());
				resource = new UrlResource(filePath.toUri());
			}
			String base64Image = null;
			if (resource.exists() && resource.isReadable()) {			
				byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
				base64Image = Base64.getEncoder().encodeToString(imageBytes);
				System.out.println(" image data");
			} else {
				System.out.println("no image data");
			}
			// Convert image to base64
			userResp = new User();
			userResp.setId(user.getId());
			userResp.setUserName(user.getUserName());
			userResp.setDesignation(user.getDesignation());
			userResp.setSalary(user.getSalary());
			userResp.setUserImage(base64Image);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return userResp;
	}

	@Override
	public void deleteUser(String id) throws UserNotFoundException{
		User user = repo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		if(user.getId()!=null && !user.getId().isEmpty()) {
			repo.deleteById(id);
		}
	}

}
