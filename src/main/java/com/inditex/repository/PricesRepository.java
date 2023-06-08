package com.inditex.repository;

import com.inditex.domain.Prices;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "prices")
public interface PricesRepository extends CrudRepository<Prices, Integer>, JpaSpecificationExecutor<Prices> {
}
