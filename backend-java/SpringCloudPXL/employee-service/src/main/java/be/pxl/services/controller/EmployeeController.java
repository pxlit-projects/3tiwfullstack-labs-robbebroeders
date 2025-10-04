package be.pxl.services.controller;

import be.pxl.services.domain.EmployeeRequest;
import be.pxl.services.domain.EmployeeResponse;
import be.pxl.services.services.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final IEmployeeService employeeService;

    @GetMapping
    public ResponseEntity getEmployees(){
        return new ResponseEntity(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addEmployee(@RequestBody EmployeeRequest employeeRequest) {
        employeeService.addEmployee(employeeRequest);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> findEmployeeById(@PathVariable Long employeeId) {
        EmployeeResponse employee = employeeService.findEmployeeById(employeeId);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeResponse>> findEmployeesByDepartment(@PathVariable Long departmentId) {
        List<EmployeeResponse> employees = employeeService.findEmployeeByDepartment(departmentId);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<EmployeeResponse>> findEmployeesByOrganization(@PathVariable Long organizationId) {
        List<EmployeeResponse> employees = employeeService.findEmployeeByOrganization(organizationId);
        return ResponseEntity.ok(employees);
    }

}
