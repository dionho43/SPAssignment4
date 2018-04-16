package com.DH.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DH.Entity.Book;
import com.DH.Entity.User;
import com.DH.Repository.UserRepository;


//Establish service with Spring
@Service
public class UserService {

@Autowired
protected UserRepository userRepository;
//Inherited Save method
public void save(User user) {
    userRepository.save(user);
} 
//Methods executing named queries
public List<User> findByUsername(String username)
{
	return userRepository.findByUsername(username);
}

public List<User> findById(int id)
{
	return userRepository.findById(id);
}

public List<User> findAll()
{
	return userRepository.findAll();
}
}