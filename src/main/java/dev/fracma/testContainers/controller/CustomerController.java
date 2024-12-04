package dev.fracma.testContainers.controller;

import dev.fracma.testContainers.dao.CustomerRepository;
import dev.fracma.testContainers.model.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customer")
@Slf4j
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerRepository customerRepository;

	@GetMapping
	public List<Customer> getCustomer (){
		log.info("Get Customers");
		return customerRepository.findAll();
	}

	@PostMapping
	public void createCustomer(@RequestBody Customer newCustomer){
		log.info("Create Customer");
		customerRepository.save(newCustomer);
	}
}
