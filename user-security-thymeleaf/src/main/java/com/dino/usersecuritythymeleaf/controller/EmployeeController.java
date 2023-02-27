package com.dino.usersecuritythymeleaf.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/dashboard")
	@Secured("ROLE_EMPLOYEE")
	public String getEmployeeDashboard(@AuthenticationPrincipal User user, Model model) {
		
		user = userService.findById(user.getId());
		
		List<User> usersAll = userService.findAll();
		List<User> users = new ArrayList<>();
		
		for (User userDb : usersAll) {
			if (userDb.getAuthorities().stream().filter((authority) -> authority.getAuthority().equals("ROLE_USER")).count() == 1) {
				users.add(userDb);
			}
		}
		
		model.addAttribute("users", users);
		user.setPassword("");
		model.addAttribute("employee", user);
		
		return "employee-dashboard";
	}
	
	@GetMapping("/delete/{id}")
	@Secured("ROLE_ADMIN")
	public String delteEmployee(@PathVariable Long id) {
		userService.deleteById(id);
		return "redirect:/admin/dashboard";
	}
	
	@GetMapping("/register")
	@Secured("ROLE_ADMIN")
	public String getRegister(Model model, @AuthenticationPrincipal User admin) {
		
		model.addAttribute("employee", new User());
		model.addAttribute("admin", admin);
		
		return "register-employee";
	}
	
	@PostMapping("/register")
	@Secured("ROLE_ADMIN")
	public String register(@ModelAttribute("employee") User employee) {
		userService.save(employee, "ROLE_EMPLOYEE");
		return "redirect:/admin/dashboard";
	}
	
	@GetMapping("/update/{id}")
	@Secured("ROLE_ADMIN")
	public String updateEmployee(@PathVariable Long id, Model model) {
		
		User employee = userService.findById(id);
		model.addAttribute("employee", employee);
		
		return "update-employee";
	}
	
	@PostMapping("/update")
	@Secured("ROLE_ADMIN")
	public String updateEmployee(@ModelAttribute("employee") User employee) {
		
		User employeeDb = userService.findById(employee.getId());
		
		if (employee.getPassword().equals("")) {
			employee.setPassword(employeeDb.getPassword());
		}
		else {
			employee.setPassword(passwordEncoder.encode(employee.getPassword()));
		}
		
		userService.update(employee);
		
		return "redirect:/admin/dashboard";
	}
}








