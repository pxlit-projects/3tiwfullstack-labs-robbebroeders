package be.pxl.services.services;

import be.pxl.services.domain.*;
import be.pxl.services.exception.NotFoundException;
import be.pxl.services.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationService implements IOrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    public OrganizationResponse findOrganizationById(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new NotFoundException("No employee with id [" + organizationId + "]"));
        return mapToOrganizationResponse(organization);
    }

    @Override
    public OrganizationResponse findOrganizationByIdWithDepartments(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new NotFoundException("No employee with id [" + organizationId + "]"));
        return mapToOrganizationResponseWithDepartments(organization);
    }

    @Override
    public OrganizationResponse findOrganizationByIdWithDepartmentsAndEmployees(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new NotFoundException("No employee with id [" + organizationId + "]"));
        return mapToOrganizationResponseWithDepartmentsAndEmployees(organization);
    }

    @Override
    public OrganizationResponse findOrganizationByIdWithEmployees(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new NotFoundException("No employee with id [" + organizationId + "]"));
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
