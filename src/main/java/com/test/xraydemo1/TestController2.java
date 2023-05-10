package com.test.xraydemo1;

import java.io.IOException;
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

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

@RestController
@RequestMapping("xraydemo2")
public class TestController2 {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
    private AmazonS3 s3Client;
	
	@GetMapping("/api")
	public ResponseEntity<String> getMsg(@RequestParam("code") int code) {
		if(code==200) {
			//String msg = postMessage("test app").getBody();
			String msg = restTemplate.postForObject(
			         "http://localhost:8080/xraydemo1/postapi/v3", "test", String.class);
			String dataFromS3 = "";
			try {
				dataFromS3 = readDataFromS3();
			} catch(Exception e) {
				e.printStackTrace();
				dataFromS3 = e.toString();
			}
			return ResponseEntity.ok().body(String.format("SUCCESS [%s] ['dataFromS3':'%s']",msg,dataFromS3)) ;
		} else {
			if(code>=400 && code<499) {
				return ResponseEntity.badRequest().body("bad req code:"+code);
			} else {
				return ResponseEntity.internalServerError().body("error");
			}
		}
	}
	
	private String readDataFromS3() throws IOException {
		String bucketName = "my-bucket";
        String objectKey = "my-file.txt";
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectKey); 
		S3Object s3Object = s3Client.getObject(getObjectRequest);
		String objectContent = IOUtils.toString(s3Object.getObjectContent());
		return objectContent;
	}
}
