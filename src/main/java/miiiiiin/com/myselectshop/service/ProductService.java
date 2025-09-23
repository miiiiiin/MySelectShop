package miiiiiin.com.myselectshop.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import miiiiiin.com.myselectshop.controller.ProductMyPriceRequestDto;
import miiiiiin.com.myselectshop.dto.ProductRequestDto;
import miiiiiin.com.myselectshop.dto.ProductResponseDto;
import miiiiiin.com.myselectshop.entity.Product;
import miiiiiin.com.myselectshop.entity.User;
import miiiiiin.com.myselectshop.entity.UserRoleEnum;
import miiiiiin.com.myselectshop.naver.dto.ItemDto;
import miiiiiin.com.myselectshop.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto createProduct(ProductRequestDto request, User user) {
        Product product = productRepository.save(new Product(request, user));
        return new ProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductMyPriceRequestDto request) {
        int myPrice = request.getMyprice();

        if (myPrice < MIN_MY_PRICE) {
            throw new IllegalArgumentException("유효하지 않은 관심 가격");
        }

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NullPointerException("해당 상품은 찾을 수 없습니다."));

        product.update(request);
        return new ProductResponseDto(product);
    }

    public Page<ProductResponseDto> getProducts(User user, int page, int size, String sortBy, boolean isASC) {
        Sort.Direction dir = isASC ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(dir, sortBy); // 방향, 기준 정렬 항목
        Pageable pageable = PageRequest.of(page, size, sort);

        UserRoleEnum role = user.getRole();
        Page<Product> productList;

        if (role == UserRoleEnum.USER) {
            productList = productRepository.findAllByUser(user, pageable);
        } else {
            productList = productRepository.findAll(pageable);
        }

//        return productRepository.findAllByUser(user, pageable)
//            .stream()
//            .map(ProductResponseDto::new)
//            .toList();

        return productList
            .map(ProductResponseDto::new);
    }

    @Transactional
    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NullPointerException("찾는 상품이 없습니다."));

        product.updateByItemDto(itemDto);
    }

//    public List<ProductResponseDto> getAllProducts() {
//        return productRepository.findAll()
//            .stream()
//            .map(ProductResponseDto::new)
//            .toList();
//
//    }
}
