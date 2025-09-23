package miiiiiin.com.myselectshop.repository;

import java.util.List;
import miiiiiin.com.myselectshop.entity.Folder;
import miiiiiin.com.myselectshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    // 이름 여러개 찾는다고 하면 IN 으로 메서드명 설정
    List<Folder> findAllByUserAndNameIn(User user, List<String> folderNames);

    List<Folder> findAllByUser(User user);
}
