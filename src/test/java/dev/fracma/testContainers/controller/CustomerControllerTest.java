package dev.fracma.testContainers.controller;

import dev.fracma.testContainers.model.Customer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTest {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
			"postgres:16-alpine"
	);

	@BeforeAll
	static void beforeAll() {
		postgres.start();
	}

	@AfterAll
	static void afterAll() {
		postgres.stop();
	}

	private String buildUrl(String endpoint) {
		return "http://localhost:" + port + endpoint;
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}


	@Test
	public void testGetEmptyCustomer() {
		String url = buildUrl("/customer");

		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).isEqualTo("[]");
	}

	@Test
	public void testCreateCustomer() {
		String url = buildUrl("/customer");

		Customer customer1 = new Customer("customer1@ti.com", "customer1");

		HttpEntity<Customer> requestPost = new HttpEntity<>(customer1);
		ResponseEntity<Customer> responsePost = restTemplate.postForEntity(url, requestPost, Customer.class);
		assertThat(responsePost.getStatusCode().is2xxSuccessful()).isTrue();

		ResponseEntity<List<Customer>> response = restTemplate.exchange(
				url,
				org.springframework.http.HttpMethod.GET,
				null,
				new ParameterizedTypeReference<>() {
				}
		);
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		List<Customer> items = response.getBody();
		assertThat(items).isNotNull();
		assertThat(items.size()).isEqualTo(1);
	}
}