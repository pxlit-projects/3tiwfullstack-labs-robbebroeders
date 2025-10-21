package be.pxl.services.services;

import be.pxl.services.domain.Department;
import be.pxl.services.domain.dto.DepartmentRequest;
import be.pxl.services.domain.dto.DepartmentResponse;

import java.util.List;

public interface IDepartmentService {
    List<Department> getAllDepartments();
    void addDepartment(DepartmentRequest departmentRequest);
    DepartmentResponse findDepartmentById(Long employeeId);
    List<DepartmentResponse> findDepartmentsByOrganization(Long departmentId);
    List<DepartmentResponse> findDepartmentsByOrganizationWithEmployees(Long organizationId);
}