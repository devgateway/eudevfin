package org.devgateway.eudevfin.persistence.repositories;

import java.util.List;

import org.devgateway.eudevfin.domain.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemCrudRepository extends CrudRepository<Item, Long> {
	
	List<Item> findByProduct(String product);
	List<Item> findByPrice(double price);
}
