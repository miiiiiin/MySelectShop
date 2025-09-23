package miiiiiin.com.myselectshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "folder")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // 회원 객체에서 폴더 객체를 조회하는 경우가 없기 때문에 폴더와 회원을  N : 1 단방향 연관관계로 설정
    // 폴더 객체를 조회할 때, 항상 회원 정보를 같이 조회할 필요는 없음.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Folder(String name, User user) {
        this.name = name;
        this.user = user;
    }
}