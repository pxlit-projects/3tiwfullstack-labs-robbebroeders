package be.pxl.services;

import be.pxl.services.domain.Department;
import be.pxl.services.domain.Employee;
import be.pxl.services.domain.Organization;
import be.pxl.services.repository.OrganizationRepository;
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
public class OrganizationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrganizationRepository organizationRepository;

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
        organizationRepository.deleteAll();
    }

    @Test
    public void shouldCreateOrganization_WhenPostRequestIsValid() throws Exception {
        Organization organization = Organization.builder()
                .address("Hasselt")
                .name("Cegeka")
                .build();

        String organizationString = objectMapper.writeValueAsString(organization);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(organizationString))
                .andExpect(status().isCreated());

        assertEquals(1, organizationRepository.findAll().size());
    }

    @Test
    public void shouldReturnOrganizationById_WhenOrganizationExists() throws Exception {
        Organization savedOrganization = organizationRepository.save(
                Organization.builder()
                        .address("Hasselt")
                        .name("Cegeka")
                        .build()
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/organization/" + savedOrganization.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cegeka"))
                .andExpect(jsonPath("$.address").value("Hasselt"));

        Optional<Organization> retrievedOrganization = organizationRepository.findById(savedOrganization.getId());
        assertTrue(retrievedOrganization.isPresent());
        assertEquals(savedOrganization.getId(), retrievedOrganization.get().getId());
    }

    @Test
    public void shouldReturnOrganizationWithDepartments_WhenGetIsCalled() throws Exception {
        Department department1 = Department.builder()
                .organizationId(1L)
                .name("HR")
                .position("student")
                .build();

        Department department2 = Department.builder()
                .organizationId(1L)
                .name("Finance")
                .position("student")
                .build();

        List<Department> departments = new ArrayList<>();
        departments.add(department1);
        departments.add(department2);

        Organization savedOrganization = organizationRepository.save(
                Organization.builder()
                        .address("Hasselt")
                        .name("Cegeka")
                        .departments(departments)
                        .build()
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/organization/" + savedOrganization.getId() + "/with-departments"))
                .andExpect(status().isOk());

        Optional<Organization> retrievedOrganization = organizationRepository.findById(savedOrganization.getId());
        assertTrue(retrievedOrganization.isPresent());
        assertEquals(savedOrganization.getId(), retrievedOrganization.get().getId());
        assertEquals(2, savedOrganization.getDepartments().size());
    }

    @Test
    public void shouldReturnOrganizationWithDepartmentsAndEmployees_WhenGetIsCalled() throws Exception {
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

        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);

        Department department1 = Department.builder()
                .organizationId(1L)
                .name("HR")
                .position("student")
                .employees(employees)
                .build();

        Department department2 = Department.builder()
                .organizationId(1L)
                .name("Finance")
                .position("student")
                .employees(employees)
                .build();

        List<Department> departments = new ArrayList<>();
        departments.add(department1);
        departments.add(department2);

        Organization savedOrganization = organizationRepository.save(
                Organization.builder()
                        .address("Hasselt")
                        .name("Cegeka")
                        .departments(departments)
                        .build()
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/organization/" + savedOrganization.getId() + "/with-departments-and-employees"))
                .andExpect(status().isOk());

        Optional<Organization> retrievedOrganization = organizationRepository.findById(savedOrganization.getId());
        assertTrue(retrievedOrganization.isPresent());
        assertEquals(savedOrganization.getId(), retrievedOrganization.get().getId());
        assertEquals(2, savedOrganization.getDepartments().size());
        assertEquals(2, savedOrganization.getDepartments().get(0).getEmployees().size());
    }

    @Test
    public void shouldReturnOrganizationWithEmployees_WhenGetIsCalled() throws Exception {
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

        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);

        Organization savedOrganization = organizationRepository.save(
                Organization.builder()
                        .address("Hasselt")
                        .name("Cegeka")
                        .employees(employees)
                        .build()
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/organization/" + savedOrganization.getId() + "/with-employees"))
                .andExpect(status().isOk());

        Optional<Organization> retrievedOrganization = organizationRepository.findById(savedOrganization.getId());
        assertTrue(retrievedOrganization.isPresent());
        assertEquals(savedOrganization.getId(), retrievedOrganization.get().getId());
        assertEquals(2, savedOrganization.getEmployees().size());
    }
}
