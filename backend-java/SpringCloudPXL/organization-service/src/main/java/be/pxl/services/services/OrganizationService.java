package be.pxl.services.services;

import be.pxl.services.client.DepartmentClient;
import be.pxl.services.client.EmployeeClient;
import be.pxl.services.domain.*;
import be.pxl.services.domain.dto.OrganizationResponse;
import be.pxl.services.exception.NotFoundException;
import be.pxl.services.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationService implements IOrganizationService {

    private final OrganizationRepository organizationRepository;
    private final EmployeeClient employeeClient;
    private final DepartmentClient departmentClient;

    @Override
    public OrganizationResponse findOrganizationById(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new NotFoundException("No employee with id [" + organizationId + "]"));
        return mapToOrganizationResponse(organization);
    }

    @Override
    public OrganizationResponse findOrganizationByIdWithDepartments(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new NotFoundException("No employee with id [" + organizationId + "]"));

        organization.setDepartments(departmentClient.getDepartmentsByOrganization(organization.getId()));

        return mapToOrganizationResponseWithDepartments(organization);
    }

    @Override
    public OrganizationResponse findOrganizationByIdWithDepartmentsAndEmployees(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new NotFoundException("No employee with id [" + organizationId + "]"));

        organization.setEmployees(employeeClient.getEmployeesByOrganization(organization.getId()));
        organization.setDepartments(departmentClient.getDepartmentsByOrganization(organization.getId()));

        return mapToOrganizationResponseWithDepartmentsAndEmployees(organization);
    }

    @Override
    public OrganizationResponse findOrganizationByIdWithEmployees(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new NotFoundException("No employee with id [" + organizationId + "]"));

        organization.setEmployees(employeeClient.getEmployeesByOrganization(organization.getId()));

        return mapToOrganizationResponseWithEmployees(organization);
    }

    private OrganizationResponse mapToOrganizationResponse(Organization organization) {
        return OrganizationResponse.builder()
                .name(organization.getName())
                .address(organization.getAddress())
                .build();
    }

    private OrganizationResponse mapToOrganizationResponseWithDepartments(Organization organization) {
        return OrganizationResponse.builder()
                .name(organization.getName())
                .address(organization.getAddress())
                .departments(organization.getDepartments())
                .build();
    }

    private OrganizationResponse mapToOrganizationResponseWithDepartmentsAndEmployees(Organization organization) {
        return OrganizationResponse.builder()
                .name(organization.getName())
                .address(organization.getAddress())
                .employees(organization.getEmployees())
                .departments(organization.getDepartments())
                .build();
    }

    private OrganizationResponse mapToOrganizationResponseWithEmployees(Organization organization) {
        return OrganizationResponse.builder()
                .name(organization.getName())
                .address(organization.getAddress())
                .employees(organization.getEmployees())
                .build();
    }
}
