package com.dino.usersecuritythymeleaf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dino.usersecuritythymeleaf.entity.Authority;
import com.dino.usersecuritythymeleaf.entity.User;
import com.dino.usersecuritythymeleaf.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}

	@Override
	public User save(User user, String role) {
		
		Authority authority = new Authority();
		authority.setAuthority(role);
		authority.setUser(user);
		
		user.getAuthorities().add(authority);
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id).orElse(new User());
	}

	@Override
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public void update(User user) {
		userRepository.save(user);
	}
	
}
