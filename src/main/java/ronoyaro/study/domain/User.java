package ronoyaro.study.domain;

import jakarta.persistence.*;
import lombok.*;


@Builder
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
}
