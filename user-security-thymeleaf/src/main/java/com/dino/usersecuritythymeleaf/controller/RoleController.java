package com.dino.usersecuritythymeleaf.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dino.usersecuritythymeleaf.entity.Authority;
import com.dino.usersecuritythymeleaf.entity.User;
import com.dino.usersecuritythymeleaf.service.UserService;

@Controller
@RequestMapping("/")
public class RoleController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public String checkRole(@AuthenticationPrincipal User user) {
		
		User userDb = userService.findByUsername(user.getUsername());
		user.setId(userDb.getId());
		
		Set<Authority> authorities = user.getAuthorities();
		boolean isEmployee = false;
		boolean isAdmin = false;
		
		for (Authority authority : authorities) {
			if (authority.getAuthority().equals("ROLE_EMPLOYEE")) {
				isEmployee = true;
			}
			else if (authority.getAuthority().equals("ROLE_ADMIN")) {
				isAdmin = true;
			}
		}
		
		if (isEmployee) {
			return "redirect:/employee/dashboard";
		}
		else if (isAdmin) {
			return "redirect:/admin/dashboard";
		}
		else {
			return "redirect:/user/dashboard";
		}
	}
}









