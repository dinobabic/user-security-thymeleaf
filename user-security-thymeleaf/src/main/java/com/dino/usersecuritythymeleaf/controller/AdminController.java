package com.dino.usersecuritythymeleaf.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dino.usersecuritythymeleaf.entity.User;
import com.dino.usersecuritythymeleaf.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/dashboard")
	public String getAdminDashboard(@AuthenticationPrincipal User user, Model model) {
		
		user = userService.findById(user.getId());
		
		List<User> users = userService.findAll();
		users = users.stream()
				.filter((userStream) -> 
				userStream.getAuthorities().stream()
						.filter((authority) -> 
							authority.getAuthority().equals("ROLE_EMPLOYEE")).count() == 1).collect(Collectors.toList());
		
		model.addAttribute("employees", users);
		user.setPassword("");
		model.addAttribute("admin", user);
		
		return "admin-dashboard";
	}
	
	@PostMapping("/update")
	public String updateAdmin(@ModelAttribute("admin") User user) {
		
		User adminDb = userService.findById(user.getId());
		
		if (user.getPassword().equals("")) {
			user.setPassword(adminDb.getPassword());
		}
		else {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		
		userService.update(user);
		
		return "redirect:/admin/dashboard";
		
	}

}
