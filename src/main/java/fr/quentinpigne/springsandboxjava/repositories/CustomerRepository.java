package fr.quentinpigne.springsandboxjava.repositories;

import fr.quentinpigne.springsandboxjava.entities.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, UUID> {

    List<Customer> findByCode(Long code);
}
