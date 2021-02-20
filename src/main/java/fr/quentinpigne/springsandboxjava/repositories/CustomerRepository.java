package fr.quentinpigne.springsandboxjava.repositories;

import fr.quentinpigne.springsandboxjava.entities.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByCode(Integer code);
}
