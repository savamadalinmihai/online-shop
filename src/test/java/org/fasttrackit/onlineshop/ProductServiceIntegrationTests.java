package org.fasttrackit.onlineshop;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.service.ProductService;
import org.fasttrackit.onlineshop.transfer.SaveProductRequest;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionSystemException;

// this is an import of a METHOD only
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest
public class ProductServiceIntegrationTests {

    // field injection (when you inject something from the ioc container (a dependency))
    // annotating the field itself (properties of the class are called the field/instance variables)
    @Autowired
    private ProductService productService;

    @Test
    void createProduct_whenValidRequest_thenProductIsCreated() {
        createProduct();
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
        Product product = createProduct();

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

    private Product createProduct() {
        SaveProductRequest request = new SaveProductRequest();
        request.setName("Phone");
        request.setQuantity(100);
        request.setPrice(399.9);

        Product product = productService.createProduct(request);

        assertThat(product, notNullValue());
        assertThat(product.getId(), greaterThan(0L));
        assertThat(product.getName(), is(request.getName()));
        assertThat(product.getQuantity(), is(request.getQuantity()));
        assertThat(product.getPrice(), is(request.getPrice()));

        return product;
    }

    @Test
    void updateProduct_whenValidRequest_thenReturnUpdatedProduct() {
        Product product = createProduct();

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
        Product product = createProduct();

        productService.deleteProduct(product.getId());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.getProduct(product.getId()));
    }
}
