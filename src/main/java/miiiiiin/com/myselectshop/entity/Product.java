package miiiiiin.com.myselectshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miiiiiin.com.myselectshop.controller.ProductMyPriceRequestDto;
import miiiiiin.com.myselectshop.dto.ProductRequestDto;
import miiiiiin.com.myselectshop.naver.dto.ItemDto;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@Table(name = "product") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class Product extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private int lprice;

    @Column(nullable = false)
    private int myprice;

    // 기본값인 즉시 로딩 대신 (상품 등록 시 회원이 등록할 수 있음)
    // 그런데 프로덕트 조회할 때마다 그 프로덕트와 연관된 회원 정보가 필요 없음. (항상 필요한게 아님)
    // 지연 로딩 (회원 정보가 정말 필요할 때만 조회해올 수 있도록 fetch type을 지연 로딩으로 설정)
    @ManyToOne(fetch = FetchType.LAZY) // 상품과 회원은 N:1
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Product(ProductRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.image = requestDto.getImage();
        this.link = requestDto.getLink();
        this.lprice = requestDto.getLprice();
        this.user = user;
    }

    public void update(ProductMyPriceRequestDto request) {
        this.myprice = request.getMyprice();
    }

    public void updateByItemDto(ItemDto itemDto) {
        this.lprice = itemDto.getLprice();
    }
}