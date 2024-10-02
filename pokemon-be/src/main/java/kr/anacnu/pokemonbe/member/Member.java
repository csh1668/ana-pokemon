package kr.anacnu.pokemonbe.member;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member implements UserDetails {

    /**
     * DB에 저장되는 ID. 실제로 로그인 할 때의 ID와는 다릅니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * "ana + 교육 참가 학생의 학번"으로 로그인 ID가 구성됩니다.
     */
    @Column(length = 9, nullable = false)
    private String studentId;


    /**
     * "교육 참가 학생의 학번"이 로그인 password가 됩니다.
     */
    @Column(length = 9, nullable = false)
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return studentId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
