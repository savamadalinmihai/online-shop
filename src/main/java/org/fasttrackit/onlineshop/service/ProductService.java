package org.fasttrackit.onlineshop.service;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.persistance.ProductRepository;
import org.fasttrackit.onlineshop.transfer.product.GetProductsRequest;
import org.fasttrackit.onlineshop.transfer.product.SaveProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<Product> getProducts(GetProductsRequest request, Pageable pageable) {
        LOGGER.info("Searching products: {}", request);

        if (request != null) {
            if (request.getPartialName() != null && request.getMinQuantity() != null) {
                return productRepository.findByNameContainingAndQuantityGreaterThanEqual
                        (request.getPartialName(), request.getMinQuantity(), pageable);
            } else if (request.getPartialName() != null) {
                return productRepository.findByNameContaining(request.getPartialName(), pageable);
            }
        }
        return productRepository.findAll(pageable);
    }

    public Product getProduct(long id) {
        LOGGER.info("Retrieving product {}", id);

        // this is a lambda expression
        return productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Product " + id + " not found."));
    }


    public Product updateProduct(long id, SaveProductRequest request) {

        LOGGER.info("Updating product {}: {}", id, request);

        Product product = getProduct(id);

        BeanUtils.copyProperties(request, product);

        return productRepository.save(product);
    }

    public void deleteProduct(long id) {

        LOGGER.info("Deleting product {}: ", id);

        productRepository.deleteById(id);
    }
}
