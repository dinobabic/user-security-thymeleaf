package com.dino.usersecuritythymeleaf.service;

import java.util.List;

import com.dino.usersecuritythymeleaf.entity.User;

public interface UserService {

	User findByUsername(String username);

	User save(User user, String role);

	List<User> findAll();

	User findById(Long id);

	void deleteById(Long id);

	void update(User user);
}
