package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
public class MainController {
	
	@Autowired
	HttpSession session;

	
	@GetMapping("/login")
	public String login() {
		session.setAttribute("user_id", "user");
		return String.valueOf(session.getAttribute("user_id"));
	}
	
	@GetMapping("/loginChk")
	public String loginChk() {
		System.out.println(String.valueOf(session.getAttribute("user_id")));
		return "";
	}
	
	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	
	@GetMapping("/get1")
	public String get1() {
		return "get1";
	}
	
	@GetMapping("/get2")
	public String get2() {
		return "get2";
	}
	
	@PostMapping("/post1")
	public String insertMethod(Map<String, Object> map) {
		
		for (Map.Entry<String, Object> entry : map.entrySet()) {
		    String key = entry.getKey();
		    Object value = entry.getValue();
		    System.out.println("key: " + key + ", value: " + value);
		}
		    
		return "post1";
	}
	
	@PostMapping("/post2")
	public String updateMethod(Map<String, Object> map) {
		
		for (Map.Entry<String, Object> entry : map.entrySet()) {
		    String key = entry.getKey();
		    Object value = entry.getValue();
		    System.out.println("key: " + key + ", value: " + value);
		}
		
		return "post2";
	}
	
	

}
