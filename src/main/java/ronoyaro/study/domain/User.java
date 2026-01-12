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
    private String firstName;
    private String lastName;
    private String email;
}
