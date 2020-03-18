package org.fasttrackit.onlineshop;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.service.ProductService;
import org.fasttrackit.onlineshop.steps.ProductTestSteps;
import org.fasttrackit.onlineshop.transfer.product.SaveProductRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

// this is an import of a METHOD only

@SpringBootTest
public class ProductServiceIntegrationTests {

    // field injection (when you inject something from the ioc container (a dependency))
    // annotating the field itself (properties of the class are called the field/instance variables)
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductTestSteps productTestSteps;

    @Test
    void createProduct_whenValidRequest_thenProductIsCreated() {productTestSteps.createProduct();
    }

    @Test
    void createProduct_whenMissingName_thenExceptionIsThrown() {
        SaveProductRequest request = new SaveProductRequest();
        request.setQuantity(1);
        request.setPrice(124.0);

        try {
            productService.createProduct(request);
        } catch (Exception e) {
            assertThat(e, notNullValue());
            assertThat("Unexpected exception type.", e instanceof TransactionSystemException);
        }
    }

    @Test
    void getProduct_whenExistingProduct_thenReturnProduct() {
        Product product = productTestSteps.createProduct();

        Product response = productService.getProduct(product.getId());

        assertThat(response, notNullValue());
        assertThat(response.getId(), is(product.getId()));
        assertThat(response.getName(), is(product.getName()));
        assertThat(response.getQuantity(), is(product.getQuantity()));
        assertThat(response.getPrice(), is(product.getPrice()));
        assertThat(response.getImageUrl(), is(product.getImageUrl()));
    }

    @Test
    void getProduct_whenNonExistingProduct_throwResourceNotFoundException() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.getProduct(1259871291));
    }

    @Test
    void updateProduct_whenValidRequest_thenReturnUpdatedProduct() {
        Product product = productTestSteps.createProduct();

        SaveProductRequest request = new SaveProductRequest();

        request.setName(product.getName() + "updated");
        request.setDescription(product.getDescription() + "updated");
        request.setPrice(product.getPrice() + 125);
        request.setQuantity(product.getQuantity() + 125);

        Product updatedProduct = productService.updateProduct(product.getId(), request);

        assertThat(updatedProduct, notNullValue());
        assertThat(updatedProduct.getId(), is(product.getId()));
        assertThat(updatedProduct.getName(), is(request.getName()));
        assertThat(updatedProduct.getDescription(), is(request.getDescription()));
        assertThat(updatedProduct.getPrice(), is(request.getPrice()));
        assertThat(updatedProduct.getQuantity(), is(request.getQuantity()));
    }

    @Test
    void deleteProduct_whenExistingProduct_thenProductDoesNotExistAnymore() {
        Product product = productTestSteps.createProduct();

        productService.deleteProduct(product.getId());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.getProduct(product.getId()));
    }
}
