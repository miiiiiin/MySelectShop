package miiiiiin.com.myselectshop.dto;

import lombok.Getter;
import miiiiiin.com.myselectshop.entity.Folder;

@Getter
public class FolderResponseDto {
    private Long id;
    private String name;

    public FolderResponseDto(Folder folder) {
        this.id = folder.getId();
        this.name = folder.getName();
    }
}