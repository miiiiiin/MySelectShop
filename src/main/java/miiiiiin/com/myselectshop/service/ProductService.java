package miiiiiin.com.myselectshop.service;

import jakarta.transaction.Transactional;
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

    // 지연로딩 기능 사용하려면 Transactional이 필요 (즉시 로딩 사용할 지, 지연 로딩 조회 기능 사용할 지 양자택일)
    // 그러나 프로덕트 조회 시마다 계속 무조건 프로덕트 폴더 리스트가 필요하다면 즉시 로딩이 적합
    // 때에 따라 다르면 지연 로딩
    // 지연 로딩 하려면 영속성 컨텍스트가 필요 (Transaction 환경이 걸려 있어야 함)
    // 조회 성능 위해 readOnly true로 설정
    @Transactional
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
