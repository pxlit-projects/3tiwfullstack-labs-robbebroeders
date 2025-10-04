package be.pxl.services.services;

import be.pxl.services.domain.EmployeeRequest;
import be.pxl.services.domain.EmployeeResponse;

import java.util.List;

public interface IEmployeeService {
    List<EmployeeResponse> getAllEmployees();
    void addEmployee(EmployeeRequest employeeRequest);
    EmployeeResponse findEmployeeById(Long employeeId);
    List<EmployeeResponse> findEmployeeByDepartment(Long departmentId);
    List<EmployeeResponse> findEmployeeByOrganization(Long organizationId);
}
