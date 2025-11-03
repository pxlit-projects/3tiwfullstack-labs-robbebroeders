package be.pxl.services.controller;

import be.pxl.services.domain.dto.OrganizationResponse;
import be.pxl.services.services.IOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final IOrganizationService organizationService;

    @GetMapping("/{organizationId}")
    public ResponseEntity<OrganizationResponse> findOrganizationById(@PathVariable Long organizationId) {
        OrganizationResponse organization = organizationService.findOrganizationById(organizationId);
        return ResponseEntity.ok(organization);
    }

    @GetMapping("{organizationId}/with-departments")
    public ResponseEntity<OrganizationResponse> findOrganizationByIdWithDepartments(@PathVariable Long organizationId) {
        OrganizationResponse organization = organizationService.findOrganizationByIdWithDepartments(organizationId);
        return ResponseEntity.ok(organization);
    }

    @GetMapping("{organizationId}/with-departments-and-employees")
    public ResponseEntity<OrganizationResponse> findOrganizationByIdWithDepartmentsAndEmployees(@PathVariable Long organizationId) {
        OrganizationResponse organization = organizationService.findOrganizationByIdWithDepartmentsAndEmployees(organizationId);
        return ResponseEntity.ok(organization);
    }

    @GetMapping("{organizationId}/with-employees")
    public ResponseEntity<OrganizationResponse> findOrganizationByIdWithEmployees(@PathVariable Long organizationId) {
        OrganizationResponse organization = organizationService.findOrganizationByIdWithEmployees(organizationId);
        return ResponseEntity.ok(organization);
    }
}
