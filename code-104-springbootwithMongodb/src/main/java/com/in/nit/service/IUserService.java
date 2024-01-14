package com.in.nit.service;

import java.util.List;

import com.in.nit.exception.UserNotFoundException;
import com.in.nit.model.User;

public interface IUserService {
	
	public User regsiterUser(User user);
	public List<User> getAllUser();
	public User updateUser(User user)throws UserNotFoundException;
	public User getOneUser(String id) throws UserNotFoundException;
	public void deleteUser(String id) throws UserNotFoundException;

}
