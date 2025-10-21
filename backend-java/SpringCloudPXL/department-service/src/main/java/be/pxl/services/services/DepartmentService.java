package be.pxl.services.services;

import be.pxl.services.domain.*;
import be.pxl.services.domain.dto.DepartmentRequest;
import be.pxl.services.domain.dto.DepartmentResponse;
import be.pxl.services.exception.NotFoundException;
import be.pxl.services.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService implements IDepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public void addDepartment(DepartmentRequest departmentRequest) {
        Department department = Department.builder()
                .organizationId(departmentRequest.getOrganizationId())
                .name(departmentRequest.getName())
                .position(departmentRequest.getPosition())
                .build();
        departmentRepository.save(department);
    }

    @Override
    public DepartmentResponse findDepartmentById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new NotFoundException("No department with id [" + departmentId + "]"));
        return mapToDepartmentResponse(department);
    }

    @Override
    public List<DepartmentResponse> findDepartmentsByOrganization(Long organizationId) {
        List<Department> departments = departmentRepository.findDepartmentByOrganizationId(organizationId).stream().toList();
        if (departments.isEmpty()) {
            throw new NotFoundException("No departments with organizationId [" + organizationId + "]");
        }
        return departments.stream().map(this::mapToDepartmentResponse).toList();
    }

    @Override
    public List<DepartmentResponse> findDepartmentsByOrganizationWithEmployees(Long organizationId) {
        List<Department> departments = departmentRepository.findDepartmentByOrganizationId(organizationId).stream().toList();
        if (departments.isEmpty()) {
            throw new NotFoundException("No departments with organizationId [" + organizationId + "]");
        }
        return departments.stream().map(this::mapToDepartmentResponseWithEmployees).toList();
    }

    private DepartmentResponse mapToDepartmentResponse(Department department) {
        return DepartmentResponse.builder()
                .organizationId(department.getOrganizationId())
                .name(department.getName())
                .position(department.getPosition())
                .build();
    }

    private DepartmentResponse mapToDepartmentResponseWithEmployees(Department department) {
        return DepartmentResponse.builder()
                .organizationId(department.getOrganizationId())
                .name(department.getName())
                .position(department.getPosition())
                .employees(department.getEmployees())
                .build();
    }
}
