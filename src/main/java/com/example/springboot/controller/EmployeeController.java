package com.example.springboot.controller;

import java.util.List;

import javax.validation.Valid;

import com.example.springboot.exception.ResourceNotFoundException;
import com.example.springboot.model.EmployeeEntity;
import com.example.springboot.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeController {
    
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public List<EmployeeEntity> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeEntity> getEmployeeById(@PathVariable(value = "id") long id) throws ResourceNotFoundException {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping("employees")
    public ResponseEntity<Object> createEmployee(@RequestBody EmployeeEntity employee){
        return employeeService.createEmployee(employee);
    }

    @PutMapping("employees/{id}")
    public ResponseEntity<EmployeeEntity> updateEmployee(@PathVariable(value = "id") long id, @Valid @RequestBody EmployeeEntity employee) throws ResourceNotFoundException{
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable(value = "id") long id) throws ResourceNotFoundException{
        return employeeService.deleteEmployee(id);
    }

}
