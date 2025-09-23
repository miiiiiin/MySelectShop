package miiiiiin.com.myselectshop.repository;

import java.util.Collection;
import java.util.List;
import miiiiiin.com.myselectshop.entity.Product;
import miiiiiin.com.myselectshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByUser(User user);
}
