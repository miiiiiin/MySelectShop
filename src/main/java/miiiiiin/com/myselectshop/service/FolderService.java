package miiiiiin.com.myselectshop.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import miiiiiin.com.myselectshop.dto.FolderResponseDto;
import miiiiiin.com.myselectshop.entity.Folder;
import miiiiiin.com.myselectshop.entity.User;
import miiiiiin.com.myselectshop.repository.FolderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    public void addFolders(List<String> folderNames, User user) {
        // 폴더 중복 체크
        List<Folder> existingFolders = folderRepository.findAllByUserAndNameIn(user, folderNames);
        // select * from folder where user_id=1 and name in (1, 2, 3);

        List<Folder> folderList = new ArrayList<>();
        for (String name : folderNames) {
            if (!isExistsFolderName(name, existingFolders)) {
                // 중복이 아니라면
                Folder folder = new Folder(name, user);
                // 폴더 리스트에 저장
                folderList.add(folder);
            } else {
                throw new IllegalArgumentException("폴더명이 중복되었습니다.");
            }
        }
        folderRepository.saveAll(folderList);
    }

    public List<FolderResponseDto> getFolders(User user) {
        List<Folder> folders = folderRepository.findAllByUser(user);
        return folders.stream()
            .map(FolderResponseDto::new)
            .toList();
    }

    private boolean isExistsFolderName(String folderName, List<Folder> existingFolders) {
        for (Folder existingFolder : existingFolders) {
            if (folderName.equals(existingFolder.getName())) {
                return true;
            }
        }
        return false;
    }
}
