package com.example.springboot.controller;

import static com.example.springboot.builder.EmployeeBuilder.employeeBuilder;
import static com.example.springboot.mapper.EmployeeMapper.objectToJson;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.List;

import com.example.springboot.exception.ResourceNotFoundException;
import com.example.springboot.model.EmployeeEntity;
import com.example.springboot.service.EmployeeService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = EmployeeController.class)
public class WebMockTest {

    private static final int ID = 1;

    private static final String PATH_ID_1 = ("/api/v1/employees/" + ID);

    private static final String PATH = "/api/v1/employees";

    private static final URI URI_PATH_ID_1 = URI.create(PATH_ID_1);

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private EmployeeService employeeService;

    @Test
    public void whenGetEmployeesThenReturnOk() throws Exception{

        EmployeeEntity employee = employeeBuilder();

        List<EmployeeEntity> employeeList = List.of(employee);

        String employeeJsonList = objectToJson(employeeList);

        Mockito.when(employeeService.getAllEmployees()).thenReturn(employeeList);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(employeeJsonList));
    }

    @Test
    public void whenGetEmployeeByIdThenReturnOk() throws Exception{

        EmployeeEntity employee = employeeBuilder();

        String employeeJson = objectToJson(employee);

        when(employeeService.getEmployeeById(ID)).thenReturn(ResponseEntity.ok(employee));

        mockMvc.perform(get(PATH_ID_1))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(content().json(employeeJson));
    }

    @Test
    public void whenGetEmployeeThatDoesntExistThrowsExceptionBadRequest() throws Exception{

        when(employeeService.getEmployeeById(ID)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(PATH_ID_1))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPostEmployeeThenReturnCreated() throws Exception{

        EmployeeEntity employee = employeeBuilder();

        String employeeJson = objectToJson(employee);

        Mockito.any();

        when(employeeService.createEmployee(employee))
            .thenReturn(ResponseEntity.created(URI_PATH_ID_1).body(employee));

        mockMvc.perform(post(PATH)
                .contentType(APPLICATION_JSON)
                .content(employeeJson)
                .accept(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().json(employeeJson));
    }

    @Test
    public void whenDeleteEmployeeThenReturnNoContent() throws Exception {

        Mockito.when(employeeService.deleteEmployee(ID)).thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete(PATH_ID_1))
            .andExpect(status().isNoContent());
    }

    @Test
    public void whenDeleteEmployeeDoesntExistThenReturnNoContent() throws Exception {

        Mockito.doThrow(ResourceNotFoundException.class).when(employeeService).deleteEmployee(ID);

        mockMvc.perform(delete(PATH_ID_1))
            .andExpect(status().isBadRequest());
    }
}
