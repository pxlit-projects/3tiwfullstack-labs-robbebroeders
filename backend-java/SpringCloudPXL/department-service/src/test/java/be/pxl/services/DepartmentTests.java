package be.pxl.services;

import be.pxl.services.domain.Department;
import be.pxl.services.domain.dto.Employee;
import be.pxl.services.repository.DepartmentRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class DepartmentTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Container
    private static final MySQLContainer<?> sqlContainer =
            new MySQLContainer<>("mysql:5.7.37");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlContainer::getUsername);
        registry.add("spring.datasource.password", sqlContainer::getPassword);
    }

    @AfterEach
    public void clearDatabase() {
        departmentRepository.deleteAll();
    }

    @Test
    public void shouldCreateDepartment_WhenPostRequestIsValid() throws Exception {
        Department department = Department.builder()
                .organizationId(1234567L)
                .name("HR")
                .position("student")
                .build();

        String departmentString = objectMapper.writeValueAsString(department);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(departmentString))
                .andExpect(status().isCreated());

        assertEquals(1, departmentRepository.findAll().size());
    }

    @Test
    public void shouldReturnDepartmentById_WhenDepartmentExists() throws Exception {
        Department department = Department.builder()
                .organizationId(1234567L)
                .name("HR")
                .position("student")
                .build();

        Department savedDepartment = departmentRepository.save(department);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/department/" + savedDepartment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("HR"))
                .andExpect(jsonPath("$.position").value("student"));

        Optional<Department> retrievedDepartment = departmentRepository.findById(savedDepartment.getId());

        assertTrue(retrievedDepartment.isPresent());
        assertEquals(savedDepartment.getId(), retrievedDepartment.get().getId());
    }

    @Test
    public void shouldReturnAllDepartments_WhenGetIsCalled() throws Exception {
        Department department1 = Department.builder()
                .organizationId(1234567L)
                .name("HR")
                .position("student")
                .build();
        Department savedDepartment1 = departmentRepository.save(department1);
        Department department2 = Department.builder()
                .organizationId(1234567L)
                .name("Finance")
                .position("student")
                .build();
        Department savedDepartment2 = departmentRepository.save(department2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/department"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[1].name").exists());

        List<Department> retrievedDepartments = departmentRepository.findAll();

        assertEquals(2, retrievedDepartments.size());
        assertTrue(retrievedDepartments.stream().anyMatch(d -> d.getName().equals("HR")));
        assertTrue(retrievedDepartments.stream().anyMatch(d -> d.getName().equals("Finance")));
    }

    @Test
    public void shouldFindDepartmentByOrganization() throws Exception {
        Department savedDepartment = departmentRepository.save(
                Department.builder()
                        .organizationId(1234567L)
                        .name("HR")
                        .position("student")
                        .build()
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/department/organization/" + savedDepartment.getOrganizationId()))
                .andExpect(status().isOk());

        Optional<Department> retrievedDepartment = departmentRepository.findById(savedDepartment.getId());
        assertTrue(retrievedDepartment.isPresent());
        assertEquals(savedDepartment.getId(), retrievedDepartment.get().getId());
    }

    @Test
    public void shouldFindDepartmentByOrganizationWithEmployees() throws Exception {
        List<Employee> employees = new ArrayList<>();

        Employee employee1 = Employee.builder()
                .age(24)
                .name("Jan")
                .position("student")
                .build();

        Employee employee2 = Employee.builder()
                .age(28)
                .name("Alice")
                .position("developer")
                .build();

        employees.add(employee1);
        employees.add(employee2);

        Department savedDepartment = departmentRepository.save(
                Department.builder()
                        .organizationId(1234567L)
                        .name("HR")
                        .position("student")
                        .employees(employees)
                        .build()
        );

        mockMvc.perform(MockMvcRequestBuilders.get(
                        "/api/department/organization/" + savedDepartment.getOrganizationId() + "/with-employees"))
                .andExpect(status().isOk());

        Optional<Department> retrievedDepartment = departmentRepository.findById(savedDepartment.getId());

        assertTrue(retrievedDepartment.isPresent());
        assertEquals(savedDepartment.getId(), retrievedDepartment.get().getId());
        assertEquals(2, savedDepartment.getEmployees().size());
    }
}
