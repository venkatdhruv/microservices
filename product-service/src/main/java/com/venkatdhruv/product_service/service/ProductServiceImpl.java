package com.venkatdhruv.product_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.venkatdhruv.product_service.entity.Product;
import com.venkatdhruv.product_service.exception.ProductServiceCustomException;
import com.venkatdhruv.product_service.model.ProductRequest;
import com.venkatdhruv.product_service.model.ProductResponse;
import com.venkatdhruv.product_service.repository.ProductRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Long addProduct(ProductRequest productRequest) {
        log.info("adding product : {}", productRequest);
        Product product = Product.builder()
                .productName(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity()).build();
        productRepository.save(product);
        log.info("product added : {}", product);
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        log.info("getting product by id : {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException("PRODUCT_NOT_FOUND",
                        "Product not found with id : " + productId));
        log.info("product found : {}", product);
        ProductResponse productResponse = ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .quantity(product.getQuantity()).build();
        log.info("product response : {}", productResponse);
        return productResponse;
    }

    @Override
    public void reduceQuantity(String productId, long quantity) {
        log.info("reducing quantity of product : {} by {}", productId, quantity);
        Product product = productRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new ProductServiceCustomException("PRODUCT_NOT_FOUND",
                        "Product not found with id : " + productId));
        if (product.getQuantity() < quantity) {
            throw new ProductServiceCustomException("INSUFFICIENT_QUANTITY",
                    "Insufficient quantity for product : " + productId);
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("quantity reduced for product : {}", product);
    }

}
