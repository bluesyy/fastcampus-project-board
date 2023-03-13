package fastcampus.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
// 검색 기능을 위한 인덱스
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Article extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mySQL의 AI는 identity 방식으로!
    private Long id;

    /* Setter를 각 필드에 걸고 전체 클래스 래벨에 주지 않는 이유 */
    /* 사용자가 특정 필드에 접근하지 못하게 하기 위해 ex) ID */
    @Setter @Column(nullable = false) private String title; // 제목
    @Setter @Column(nullable = false, length = 10000) private String content; // 본문

    @Setter private String hashtag; // 해시태그

    @ToString.Exclude // 순환참조를 막기위해
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();


    /* 기본 생성자 */
    protected Article() {}

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }


    /* 팩토리 메서드를 통해 제공할 수 있게 한다 */
    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    /* 동등성 검사 */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        /* return id != null && 추가 */
        /* id가 부여되지 않았으면, 영속화 되지 않았으면 다 다른것으로 간주하거나 처리하지 않겠다 */
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
