package be.pxl.services.client;

import be.pxl.services.domain.dto.Employee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "employee-service") // -> naam van de service
public interface EmployeeClient {

    @GetMapping("/api/employee/organization/{id}")
    List<Employee> getEmployeesByOrganization(@PathVariable Long id);
}
