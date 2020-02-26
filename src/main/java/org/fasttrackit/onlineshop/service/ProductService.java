package org.fasttrackit.onlineshop.service;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.persistance.ProductRepository;
import org.fasttrackit.onlineshop.transfer.SaveProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
// anotation for the whole class to be available in the IoC container,
// so they can interact with each other
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    // inversion of control (IoC)
    private final ProductRepository productRepository;

    // dependency injection (from IoC container)
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(SaveProductRequest request) {

        LOGGER.info("Creating product {}", request);

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setImageUrl(request.getImageUrl());

        return productRepository.save(product);
    }

    public Product getProduct(long id){
        LOGGER.info("Retrieving product {}", id);

        // this is a lambda expression
        return productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Product " + id + " not found."));

        // optional usage explained (what is an optional and how to use it)

//        Optional<Product> productOptional = productRepository.findById(id);
//
//        if (productOptional.isPresent()) {
//            return productOptional.get();
//        } else {
//            throw new ResourceNotFoundException("Product " + id + " not found.");
//        }
    }
}
