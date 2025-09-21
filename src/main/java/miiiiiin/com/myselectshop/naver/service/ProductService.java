package miiiiiin.com.myselectshop.naver.service;

import lombok.RequiredArgsConstructor;
import miiiiiin.com.myselectshop.dto.ProductRequestDto;
import miiiiiin.com.myselectshop.dto.ProductResponseDto;
import miiiiiin.com.myselectshop.entity.Product;
import miiiiiin.com.myselectshop.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor 
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDto createProduct(ProductRequestDto request) {
        Product product = productRepository.save(new Product(request));
        return new ProductResponseDto(product);
    }
}
