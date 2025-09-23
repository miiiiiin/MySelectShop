package miiiiiin.com.myselectshop.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import miiiiiin.com.myselectshop.entity.Product;
import miiiiiin.com.myselectshop.entity.ProductFolder;

@Getter
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String title;
    private String link;
    private String image;
    private int lprice;
    private int myprice;

    // 프로덕트 하나에 폴더가 여러 개 등록될 수 있음 (관심상품 하나에 여러 개의 폴더가 해시태그처럼 생길 수 있음)
    private List<FolderResponseDto> productFolderList = new ArrayList<>();

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.link = product.getLink();
        this.image = product.getImage();
        this.lprice = product.getLprice();
        this.myprice = product.getMyprice();
        // 지연 로딩 (Product에서 OneToMany로 설정 (뒤가 Many로 끝나면 fetch type LAZY로 됨))
        for (ProductFolder productFolder : product.getProductFolderList()) {
            // 폴더의 정보들
            productFolderList.add(new FolderResponseDto(productFolder.getFolder()));
        }
    }
}
