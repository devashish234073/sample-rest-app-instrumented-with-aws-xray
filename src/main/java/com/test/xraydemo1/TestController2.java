package com.test.xraydemo1;

import java.lang.management.ManagementFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("xraydemo2")
public class TestController2 {
	
	@Autowired
	RestTemplate restTemplate;
	
	@GetMapping("/api")
	public ResponseEntity<String> getMsg(@RequestParam("code") int code) {
		if(code==200) {
			//String msg = postMessage("test app").getBody();
			String msg = restTemplate.postForObject(
			         "http://localhost:8080/postapi/v3", "test", String.class);
			return ResponseEntity.ok().body(String.format("SUCCESS [%s]",msg)) ;
		} else {
			if(code>=400 && code<499) {
				return ResponseEntity.badRequest().body("bad req code:"+code);
			} else {
				return ResponseEntity.internalServerError().body("error");
			}
		}
	}
}
