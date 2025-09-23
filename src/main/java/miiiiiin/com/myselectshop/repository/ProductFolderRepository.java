package miiiiiin.com.myselectshop.repository;

import java.util.Optional;
import miiiiiin.com.myselectshop.entity.Folder;
import miiiiiin.com.myselectshop.entity.Product;
import miiiiiin.com.myselectshop.entity.ProductFolder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductFolderRepository extends JpaRepository<ProductFolder, Long> {


    Optional<ProductFolder> findByProductAndFolder(Product product, Folder folder);
}
