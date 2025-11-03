package be.pxl.services.client;

import be.pxl.services.domain.dto.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "department-service") // -> naam van de service
public interface DepartmentClient {

    @GetMapping("/api/department/organization/{id}")
    List<Department> getDepartmentsByOrganization(@PathVariable Long id);
}