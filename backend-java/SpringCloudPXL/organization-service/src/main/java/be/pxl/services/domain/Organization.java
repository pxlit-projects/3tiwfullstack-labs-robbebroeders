package be.pxl.services.domain;

import be.pxl.services.domain.dto.Department;
import be.pxl.services.domain.dto.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="organization")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String address;
    //TODO normally many to one and one to many??
    @Transient
    private List<Employee> employees;
    //TODO normally many to one and one to many??
    @Transient
    private List<Department> departments;
}
