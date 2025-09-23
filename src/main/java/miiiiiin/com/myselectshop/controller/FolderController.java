package miiiiiin.com.myselectshop.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import miiiiiin.com.myselectshop.dto.FolderRequestDto;
import miiiiiin.com.myselectshop.dto.FolderResponseDto;
import miiiiiin.com.myselectshop.entity.Folder;
import miiiiiin.com.myselectshop.security.UserDetailsImpl;
import miiiiiin.com.myselectshop.service.FolderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping("/folders")
    public void addFolders(@RequestBody FolderRequestDto request,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<String> folderNames = request.getFolderNames();
        folderService.addFolders(folderNames, userDetails.getUser());
    }

    // 회원이 등록한 모든 폴더 조회
    @GetMapping("/folders")
    public List<FolderResponseDto> getFolders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return folderService.getFolders(userDetails.getUser());
    }

}
