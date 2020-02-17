package cn.ruc.xyy.jpev.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class HomePageController {	
	public HomePageController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value ="/", method=RequestMethod.GET)
	public String getHomePageInfo() {
		System.out.println("test");
		return "Hello, JPEV!";
	}
}
