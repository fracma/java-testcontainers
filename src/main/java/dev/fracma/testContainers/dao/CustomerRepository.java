package dev.fracma.testContainers.dao;

import dev.fracma.testContainers.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}