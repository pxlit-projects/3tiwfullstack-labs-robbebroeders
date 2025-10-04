package be.pxl.services.services;

import be.pxl.services.domain.Employee;
import be.pxl.services.domain.EmployeeRequest;
import be.pxl.services.domain.EmployeeResponse;
import be.pxl.services.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import be.pxl.services.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(this::mapToEmployeeResponse).toList();
    }

    @Override
    public void addEmployee(EmployeeRequest employeeRequest) {
        Employee employee = Employee.builder()
                .age(employeeRequest.getAge())
                .name(employeeRequest.getName())
                .position(employeeRequest.getPosition())
                .build();
        employeeRepository.save(employee);
    }

    @Override
    public EmployeeResponse findEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new NotFoundException("No employee with id [" + employeeId + "]"));
        return mapToEmployeeResponse(employee);
    }

    @Override
    public List<EmployeeResponse> findEmployeeByDepartment(Long departmentId) {
        List<Employee> employees = employeeRepository.findByDepartmentId(departmentId).stream().toList();
        if (employees.isEmpty()) {
            throw new NotFoundException("No employees with departmentId [" + departmentId + "]");
        }
        return employees.stream().map(this::mapToEmployeeResponse).toList();
    }

    @Override
    public List<EmployeeResponse> findEmployeeByOrganization(Long organizationId) {
        List<Employee> employees = employeeRepository.findByOrganizationId(organizationId).stream().toList();
        if (employees.isEmpty()) {
            throw new NotFoundException("No employees with organizationId [" + organizationId + "]");
        }
        return employees.stream().map(this::mapToEmployeeResponse).toList();
    }

    private EmployeeResponse mapToEmployeeResponse(Employee employee) {
        return EmployeeResponse.builder()
                .age(employee.getAge())
                .name(employee.getName())
                .position(employee.getPosition())
                .build();
    }
}
