package org.fasttrackit.onlineshop.persistance;

import org.fasttrackit.onlineshop.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContaining(String partialName, Pageable pageable);

    Page<Product> findByNameContainingAndQuantityGreaterThanEqual(
            String partialName, int minimumQuantity, Pageable pageable);

    //below is an example of JPQL (Java Persistence Query Language)
//    @Query("SELECT product FROM Product product WHERE name LIKE '%:partialName'")

    // below is an example of using native queries
    @Query(value = "SELECT * FROM product WHERE `name` LIKE '%?0'", nativeQuery = true)
    Page<Product> findByPartialName(String partialName, Pageable pageable);
 }
