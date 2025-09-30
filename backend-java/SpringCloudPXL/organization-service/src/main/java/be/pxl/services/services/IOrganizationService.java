package be.pxl.services.services;

import be.pxl.services.domain.Organization;

import java.util.List;

public interface IOrganizationService {
    List<Organization> getAllOrganizations();
}
