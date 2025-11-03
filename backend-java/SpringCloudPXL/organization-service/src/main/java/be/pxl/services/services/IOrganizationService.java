package be.pxl.services.services;

import be.pxl.services.domain.dto.OrganizationResponse;

public interface IOrganizationService {
    OrganizationResponse findOrganizationById(Long organizationId);
    OrganizationResponse findOrganizationByIdWithDepartments(Long organizationId);
    OrganizationResponse findOrganizationByIdWithDepartmentsAndEmployees(Long organizationId);
    OrganizationResponse findOrganizationByIdWithEmployees(Long organizationId);
}
