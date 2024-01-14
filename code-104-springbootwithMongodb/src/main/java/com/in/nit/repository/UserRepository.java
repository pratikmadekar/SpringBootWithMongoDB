package com.in.nit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.in.nit.model.User;

public interface UserRepository extends MongoRepository<User, String>{

}
