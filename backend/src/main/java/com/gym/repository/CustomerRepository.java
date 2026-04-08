package com.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gym.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {}
