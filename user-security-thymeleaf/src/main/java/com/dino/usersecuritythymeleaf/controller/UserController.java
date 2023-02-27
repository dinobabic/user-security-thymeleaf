package com.dino.usersecuritythymeleaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dino.usersecuritythymeleaf.entity.User;
import com.dino.usersecuritythymeleaf.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/dashboard")
	@Secured("ROLE_USER")
	public String getUserDashboard(@AuthenticationPrincipal User user, Model model) {
		
		user = userService.findById(user.getId());
		
		user.setPassword("");
		model.addAttribute("user", user);
		
		return "user-dashboard";
	}
	
	@PostMapping("/update")
	public String updateUser(@ModelAttribute("user") User user) {

		User userDb = userService.findById(user.getId());
		
		if (user.getPassword().equals("")) {
			user.setPassword(userDb.getPassword());
		}
		else {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		
		userService.update(user);
		
		return "redirect:/user/dashboard";
	}
	
	@GetMapping("/delete/{id}")
	@Secured("ROLE_EMPLOYEE")
	public String deleteUser(@PathVariable Long id) {
		
		userService.deleteById(id);
		return "redirect:/employee/dashboard";
	}
}







