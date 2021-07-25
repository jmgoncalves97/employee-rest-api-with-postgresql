package com.example.springboot.builder;

import com.example.springboot.model.EmployeeEntity;

public class EmployeeBuilder {
    
    public static EmployeeEntity employeeBuilder() {
        return EmployeeEntity.builder()
            .id(1)
            .build();
    }
}
