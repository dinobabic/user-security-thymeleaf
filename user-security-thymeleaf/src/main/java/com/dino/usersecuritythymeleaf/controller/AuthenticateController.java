package com.dino.usersecuritythymeleaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.dino.usersecuritythymeleaf.entity.User;
import com.dino.usersecuritythymeleaf.service.UserService;

/* TODO

	ROLE_USER -> When he logs in he can see his profile and also update it
	ROLE_EMPLOYEE -> When he logs in he can see the list of users and also delete them
	ROLE_ADMIN -> When he logs in he can view users and employees list, delete them and update employee

*/
@Controller
public class AuthenticateController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/authenticate")
	public String authenticate(Model model) {	
		return "login-form";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("user", new User());
		return "register-form";
	}
	
	@PostMapping("/register") 
	public String registerUser(@ModelAttribute("user") User user) {
		user = userService.save(user, "ROLE_USER");
		return "redirect:/authenticate";
	}
}
