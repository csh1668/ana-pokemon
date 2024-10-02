package kr.anacnu.pokemonbe.jwt;

import kr.anacnu.pokemonbe.member.Member;
import kr.anacnu.pokemonbe.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    /**
     * 주어진 studentId로 회원을 로드합니다.
     * @param studentId
     * @return
     * @throws UsernameNotFoundException
     */

    @Override
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {
        return memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 회원을 찾을 수 없습니다."));
    }
}


