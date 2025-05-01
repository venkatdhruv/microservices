package com.venkatdhruv.product_service.service;

import com.venkatdhruv.product_service.model.ProductRequest;
import com.venkatdhruv.product_service.model.ProductResponse;

public interface ProductService {

    Long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(Long productId);

    void reduceQuantity(String productId, long quantity);

}
