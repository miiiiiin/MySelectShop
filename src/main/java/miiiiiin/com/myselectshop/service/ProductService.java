package miiiiiin.com.myselectshop.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import miiiiiin.com.myselectshop.controller.ProductMyPriceRequestDto;
import miiiiiin.com.myselectshop.dto.ProductRequestDto;
import miiiiiin.com.myselectshop.dto.ProductResponseDto;
import miiiiiin.com.myselectshop.entity.Folder;
import miiiiiin.com.myselectshop.entity.Product;
import miiiiiin.com.myselectshop.entity.ProductFolder;
import miiiiiin.com.myselectshop.entity.User;
import miiiiiin.com.myselectshop.entity.UserRoleEnum;
import miiiiiin.com.myselectshop.naver.dto.ItemDto;
import miiiiiin.com.myselectshop.repository.FolderRepository;
import miiiiiin.com.myselectshop.repository.ProductFolderRepository;
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
    private final FolderRepository folderRepository;
    private final ProductFolderRepository productFolderRepository;

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
    public Page<ProductResponseDto> getProducts(User user, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction dir = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
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

    public void addFolder(Long productId, Long folderId, User user) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NullPointerException("해당 상품이 없습니다."));

        Folder folder = folderRepository.findById(folderId)
            .orElseThrow(() -> new NullPointerException("해당 폴더가 없습니다."));

        // product와 폴더가 현재 해당 user가 등록한건지 확인
        if (!product.getUser().getId().equals(user.getId())
            || (!folder.getUser().getId().equals(user.getId()))) {
            throw new IllegalArgumentException("회원님의 관심상품이 아니거나, 회원님의 폴더가 아닙니다.");
        }

        // 중복 확인 (하나의 상품에 중복된 폴더가 있는지 확인)
        Optional<ProductFolder> overlapFolder = productFolderRepository.findByProductAndFolder(product,
            folder);

        if (overlapFolder.isPresent()) {
            // 중복되었으니 exception
            throw new IllegalArgumentException("중복된 폴더입니다.");
        }

        // 외래키 넣어주기
        productFolderRepository.save(new ProductFolder(product, folder));
    }

    public Page<ProductResponseDto> getProductsInFolder(Long folderId, User user, int page, int size,
        String sortBy, boolean isAsc) {
        Sort.Direction dir = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(dir, sortBy); // 방향, 기준 정렬 항목
        Pageable pageable = PageRequest.of(page, size, sort);

        // 현재 로그인한 유저가 등록한 상품 조회(어떤 특정한 폴더에 등록된 -> 그 폴더를 가지고 있는 상품 조회)
        Page<Product> productList = productRepository.findAllByUserAndProductFolderList_FolderId(
            user, folderId, pageable);

        // 프로덕트 엔티티를 통해서 폴더를 조회하기 위해 현재 양방향으로 조회하고 있는 상태
        return productList.map(ProductResponseDto::new);
    }

//    public List<ProductResponseDto> getAllProducts() {
//        return productRepository.findAll()
//            .stream()
//            .map(ProductResponseDto::new)
//            .toList();
//
//    }
}
