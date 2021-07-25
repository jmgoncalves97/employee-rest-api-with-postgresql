package com.example.springboot.service;

import java.net.URI;
import java.util.List;

import com.example.springboot.exception.ResourceNotFoundException;
import com.example.springboot.model.EmployeeEntity;
import com.example.springboot.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeService {

    public static final String URI_EMPLOYEES_EMPLOYEE = "/api/v1/employees";

    private EmployeeRepository employeeRepository;

    public List<EmployeeEntity> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public ResponseEntity<EmployeeEntity> getEmployeeById(long id) throws ResourceNotFoundException{
        EmployeeEntity employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Funcionado com id %d não foi encontrado.", id)));
            
        return ResponseEntity.ok().body(employee);
    }


    public ResponseEntity<Object> createEmployee(EmployeeEntity employeeParameter){
        EmployeeEntity employee = employeeRepository.save(employeeParameter);
        URI location = URI.create(URI_EMPLOYEES_EMPLOYEE);
        return ResponseEntity.created(location).body(employee);
    }

    public ResponseEntity<EmployeeEntity> updateEmployee(long id, EmployeeEntity employeeParameter) throws ResourceNotFoundException{
        EmployeeEntity employee = employeeRepository.findById(id).get();

        employee.setFirstName(employeeParameter.getFirstName());
        employee.setLastName(employeeParameter.getLastName());
        employee.setEmail(employeeParameter.getEmail());
        employee.setPhone(employeeParameter.getPhone());

        employeeRepository.save(employee);
        
        // return ResponseEntity.ok(this.employeeRepository.save(employee));
        return ResponseEntity.accepted().body(employee);
    }

    public ResponseEntity<Void> deleteEmployee(long id) throws ResourceNotFoundException {
        EmployeeEntity employee = employeeRepository.findById(id)
            .orElseThrow(()->new ResourceNotFoundException(String.format("Funcionario com id %d não existe.",id)));

        employeeRepository.delete(employee);

        return ResponseEntity.noContent().build();
    }


}
