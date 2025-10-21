package be.pxl.services.controller;

import be.pxl.services.domain.dto.DepartmentRequest;
import be.pxl.services.domain.dto.DepartmentResponse;
import be.pxl.services.services.IDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final IDepartmentService departmentService;

    @GetMapping
    public ResponseEntity getDepartments() {
        return new ResponseEntity(departmentService.getAllDepartments(), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addDepartments(@RequestBody DepartmentRequest departmentRequest) {
        departmentService.addDepartment(departmentRequest);
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponse> findDepartmentById(@PathVariable Long departmentId) {
        DepartmentResponse department = departmentService.findDepartmentById(departmentId);
        return ResponseEntity.ok(department);
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<DepartmentResponse>> findDepartmentsByOrganization(@PathVariable Long organizationId) {
        List<DepartmentResponse> departments = departmentService.findDepartmentsByOrganization(organizationId);
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/organization/{organizationId}/with-employees")
    public ResponseEntity<List<DepartmentResponse>> findDepartmentsByOrganizationWithEmployees(@PathVariable Long organizationId) {
        List<DepartmentResponse> departments = departmentService.findDepartmentsByOrganizationWithEmployees(organizationId);
        return ResponseEntity.ok(departments);
    }
}
