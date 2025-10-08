package be.pxl.services;

import be.pxl.services.domain.Employee;
import be.pxl.services.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class EmployeeTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Container
    private static MySQLContainer sqlContainer =
            new MySQLContainer("mysql:5.7.37");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlContainer::getUsername);
        registry.add("spring.datasource.password", sqlContainer::getPassword);
    }

    @AfterEach
    public void clearDatabase() {
        employeeRepository.deleteAll();
    }

    @Test
    public void shouldCreateEmployee_WhenPostRequestIsValid() throws Exception {

        Employee employee = Employee.builder()
                .age(24)
                .name("Jan")
                .position("student")
                .build();

        String employeeString = objectMapper.writeValueAsString(employee);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeString))
                .andExpect(status().isCreated());

        assertEquals(1, employeeRepository.findAll().size());
    }

    @Test
    public void shouldReturnEmployeeById_WhenEmployeeExists() throws Exception {

        Employee employee = Employee.builder()
                .age(24)
                .name("Jan")
                .position("student")
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/" + savedEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jan"))
                .andExpect(jsonPath("$.position").value("student"));

        Optional<Employee> retrievedEmployee = employeeRepository.findById(savedEmployee.getId());

        assertTrue(retrievedEmployee.isPresent());
        assertEquals(savedEmployee.getId(), retrievedEmployee.get().getId());
    }

    @Test
    public void shouldReturnAllEmployees_WhenGetIsCalled() throws Exception {
        Employee employee1 = Employee.builder()
                .age(24)
                .name("Jan")
                .position("student")
                .build();
        Employee savedEmployee1 = employeeRepository.save(employee1);

        Employee employee2 = Employee.builder()
                .age(28)
                .name("Alice")
                .position("developer")
                .build();
        Employee savedEmployee2 = employeeRepository.save(employee2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[1].name").exists());

        List<Employee> retrievedEmployees = employeeRepository.findAll();

        assertEquals(2, retrievedEmployees.size());
        assertTrue(retrievedEmployees.stream().anyMatch(e -> e.getName().equals("Jan")));
        assertTrue(retrievedEmployees.stream().anyMatch(e -> e.getName().equals("Alice")));

    }

    @Test
    public void shouldFindEmployeeByDepartment() throws Exception {
        Employee savedEmployee = employeeRepository.save(Employee.builder()
                .age(24)
                .name("Jan")
                .position("student")
                .departmentId(1234567L)
                .build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/department/" + savedEmployee.getDepartmentId()))
                .andExpect(status().isOk());

        Optional<Employee> retrievedEmployee = employeeRepository.findById(savedEmployee.getId());
        assertTrue(retrievedEmployee.isPresent());
        assertEquals(savedEmployee.getId(), retrievedEmployee.get().getId());
    }

    @Test
    public void shouldFindEmployeeByOrganization() throws Exception {
        Employee savedEmployee = employeeRepository.save(Employee.builder()
                .age(24)
                .name("Jan")
                .position("student")
                .organizationId(1234567L)
                .build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/organization/" + savedEmployee.getOrganizationId()))
                .andExpect(status().isOk());

        Optional<Employee> retrievedEmployee = employeeRepository.findById(savedEmployee.getId());
        assertTrue(retrievedEmployee.isPresent());
        assertEquals(savedEmployee.getId(), retrievedEmployee.get().getId());
    }
}
