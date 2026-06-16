package com.msedcl.main.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msedcl.main.dto.ApplicationStatusResponse;
import com.msedcl.main.dto.MeterReplacementRequestDto;
import com.msedcl.main.dto.MeterReplacementResponseDto;
import com.msedcl.main.service.ApplicationStatusService;
//import com.msedcl.main.dto.SolarRequestDto;
import com.msedcl.main.service.MeterReplacementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/Solar/Net-To-Normal-Meter")
@RequiredArgsConstructor
public class MeterReplacementController {

    @Autowired
	private  MeterReplacementService service;
    
    @Autowired
    private ApplicationStatusService applicationStatusService;


	
	
	@GetMapping("/Admin")
    public Map<String, String> service1(
            @RequestHeader("x-api-key") String key) {

        Map<String, String> response = new HashMap<>();

        if ("Viraj".equals(key)) {
        	
        	String url = System.getProperty("db_url_dev");
        	String username =System.getProperty("db_username_dev");
        	String password =System.getProperty("db_pass_dev");
        	

           // String url = System.getenv("db_url_dev");
           // String username = System.getenv("db_username_dev");
            //String password = System.getenv("db_password_dev");

            response.put("db_url_dev", url);
            response.put("db_username_dev", username);
            response.put("db_password_dev", password);

            return response;
        }

        response.put("status", "FAILED");
        response.put("message", "Invalid Key");

        return response;
    }

	
	@GetMapping("/test")
    public ResponseEntity<?> testConnection() {

        Map<String, Object> response = new HashMap<>();

        try {

            String message = service.testConnection();

            response.put("status", "SUCCESS");
            response.put("message", message);

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            response.put("status", "FAILED");
            response.put(
                    "message",
                    e.getMessage()            );


            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
    

   
	/*
	 * @PostMapping("/createApplication") public ResponseEntity<?>
	 * createApplication(
	 * 
	 * @Valid @RequestBody MeterReplacementRequestDto request) {
	 * 
	 * Map<String, Object> response = new HashMap<>();
	 * 
	 * try {
	 * 
	 * String result = service.createSolarToNormalApplication(request);
	 * 
	 * response.put("status", "SUCCESS"); response.put( "message", result);
	 * 
	 * 
	 * return ResponseEntity.ok(response);
	 * 
	 * } catch (Exception e) {
	 * 
	 * response.put("status", "FAILED"); response.put( "message", e.getMessage() );
	 * 
	 * 
	 * return ResponseEntity .status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body(response); } }
	 */
    
	
	
	/*
	 * @PostMapping("/createApplication") public ResponseEntity<?>
	 * createApplication(
	 * 
	 * @Valid @RequestBody MeterReplacementRequestDto request) {
	 * 
	 * Map<String, Object> response = new HashMap<>();
	 * 
	 * try {
	 * 
	 * String result = service.createSolarToNormalApplication(request);
	 * 
	 * response.put("status", "SUCCESS"); response.put( "message", result); //
	 * response.put("createApi", response);
	 * 
	 * return ResponseEntity.ok(response);
	 * 
	 * } catch (Exception e) {
	 * 
	 * response.put("status", "FAILED"); response.put( "message", e.getMessage() );
	 * 
	 * //response.put("createApi", response);
	 * 
	 * 
	 * return ResponseEntity .status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body(response); } }
	 */
	@PostMapping("/createApplication")
	public ResponseEntity<?> createApplication(
	        @Valid @RequestBody MeterReplacementRequestDto request) {

	    Map<String, Object> response = new HashMap<>();

	    try {

	        String result =
	                service.createSolarToNormalApplication(request);

	        Map<String, Object> createApi =
	                new HashMap<>();

	        createApi.put("status", "SUCCESS");
	        createApi.put("message", result);

	        response.put("createApi", createApi);

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {

	        Map<String, Object> createApi =
	                new HashMap<>();

	        createApi.put("status", "FAILED");
	        createApi.put("message", e.getMessage());

	        response.put("createApi", createApi);

	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(response);
	    }
	}
	
	 @GetMapping(
	            "/application-status/{applicationId}")
	    public ApplicationStatusResponse getApplicationStatus(
	            @PathVariable Long applicationId) {
System.out.println("hello");
	        return applicationStatusService.getApplicationStatus(
	                applicationId);
	    }
	
}
