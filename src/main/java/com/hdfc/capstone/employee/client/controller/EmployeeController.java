package com.hdfc.capstone.employee.client.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.hdfc.capstone.employee.client.dto.EmployeeDTO;
import com.hdfc.capstone.employee.client.dto.EmployeeDTOEncrypted;
import com.hdfc.capstone.employee.client.exception.InvalidEmployeeIdException;
import com.hdfc.capstone.employee.client.exception.ResponseStatusException;
import com.hdfc.capstone.employee.client.utils.AESUtils;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	RestTemplate restTemplate;
	
	String baseUrl="https://34.171.62.15:8989/employee";
	

	EmployeeDTOEncrypted response;
	
	@GetMapping("/getById/{employeeId}")
	public  EmployeeDTO getById(@PathVariable String employeeId) throws Exception,InvalidEmployeeIdException{
		try {
	        long employeeIdl = Long.parseLong(employeeId);
	        response = restTemplate.getForObject(baseUrl+"/getById/"+employeeIdl, EmployeeDTOEncrypted.class);
	        }catch (HttpClientErrorException.BadRequest ex) {
			throw new InvalidEmployeeIdException();
	       }catch (NumberFormatException e) {
        
           throw new ResponseStatusException();
    }
	
		return toDTO(response);

	
	}
	
	public EmployeeDTO toDTO(EmployeeDTOEncrypted employeeEncrypted) throws Exception {
		EmployeeDTO employeeDTO=new EmployeeDTO();
        byte[] decryptedDateOfBirth = AESUtils.decrypt(employeeEncrypted.getDateOfBirth());
	    LocalDate date= LocalDate.parse(new String(decryptedDateOfBirth, StandardCharsets.UTF_8));
	    employeeDTO.setEmployeeName(employeeEncrypted.getEmployeeName());
	    employeeDTO.setEmployeeId(employeeEncrypted.getEmployeeId());
	    employeeDTO.setDateOfBirth(date);
	    return employeeDTO;

	}
}
