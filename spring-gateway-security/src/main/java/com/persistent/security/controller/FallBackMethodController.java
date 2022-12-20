package com.persistent.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackMethodController {

	@GetMapping("/courseServiceFallBack")
	public String userServiceFallBackMethod() {
		return "Course Service is taking longer than Expected." + " Please try again later";
	}

	@GetMapping("/studentServiceFallBack")
	public String departmentServiceFallBackMethod() {
		return "Student Service is taking longer than Expected." + " Please try again later";
	}
}