package org.fasttrackit.onlineshop.persistance;

import org.fasttrackit.onlineshop.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //finding review by nested property (id is a property of product which is an object in the
    // Review class and can only be accessed this way.)
    Page<Review> findByProductId(long productId, Pageable pageable);

}
