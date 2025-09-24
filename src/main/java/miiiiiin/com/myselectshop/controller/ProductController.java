package miiiiiin.com.myselectshop.controller;

import lombok.RequiredArgsConstructor;
import miiiiiin.com.myselectshop.dto.ProductRequestDto;
import miiiiiin.com.myselectshop.dto.ProductResponseDto;
import miiiiiin.com.myselectshop.security.UserDetailsImpl;
import miiiiiin.com.myselectshop.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto request, @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        return productService.createProduct(request, userDetails.getUser());
    }

    @PutMapping("/products/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id, @RequestBody ProductMyPriceRequestDto request) {
        return productService.updateProduct(id, request);
    }

    @GetMapping("/products")
    public Page<ProductResponseDto> getProducts(@AuthenticationPrincipal
    UserDetailsImpl userDetails, @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("sortBy") String sortBy,
        @RequestParam("isAsc") boolean isAsc) {
        return productService.getProducts(userDetails.getUser(),
            page - 1,
            size,
            sortBy,
            isAsc
            );
    }

    @PostMapping("/products/{productId}/folder")
    public void addFolder(@PathVariable Long productId,
        @RequestParam Long folderId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.addFolder(productId, folderId, userDetails.getUser());

    }

    // 폴더별 관심상품 조회
    @GetMapping("/folders/{folderId}/products")
    public Page<ProductResponseDto> getProductsInFolder(
        @PathVariable Long folderId,
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("sortBy") String sortBy,
        @RequestParam("isAsc") boolean isAsc,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return productService.getProductsInFolder(
            folderId,
            userDetails.getUser(),
            page - 1,
            size,
            sortBy,
            isAsc
        );
    }

    /**
     * 모든 계정에서 등록한 모든 상품을 조회 (admin에서)
     */
//    @GetMapping("/admin/products")
//    public List<ProductResponseDto> getAll() {
//        return productService.getAllProducts();
//    }
}
