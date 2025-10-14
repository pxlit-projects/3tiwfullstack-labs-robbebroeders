package be.pxl.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Department Service
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class DepartmentServiceApplication {
    public static void main( String[] args ) {
        SpringApplication.run(DepartmentServiceApplication.class, args);
    }
}
