package miiiiiin.com.myselectshop.repository;

import java.util.List;
import miiiiiin.com.myselectshop.entity.Product;
import miiiiiin.com.myselectshop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByUser(User user, Pageable pageable);

    // 예시: SELECT
    // p.title as productTitle
    // pf.productId as productId
    // pf.folder_id as folderId
    // FROM PRODUCT p
    // LEFT JOIN PRODUCT_FOLDER PF
    // ON p.id = pf.product_id
    // WHERE p.user_id=1 (robbie)
    // and
    // pf.folderId = 3 (robbie라는 회원이 등록한 폴더Id가 3인 폴더에 들어가있는 상품 조회)
    // ORDER BY p.id (ASC)
    // LIMIT 10, 10 (페이징 시작(예시는 2페이지에 해당)/페이지 개수 제한);
    Page<Product> findAllByUserAndProductFolderList_FolderId(User user, Long folderId, Pageable pageable);
}
