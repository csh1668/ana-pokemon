package kr.anacnu.pokemonbe.member;

import kr.anacnu.pokemonbe.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * 로그인을 처리하고 JWT Token을 반환합니다.
     *
     * @param studentId
     * @param password
     * @return
     */
    @Transactional
    public String signIn(String studentId, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(studentId, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    public String signUp(String studentId, String password) {
        if (memberRepository.findByStudentId(studentId).isPresent())
            throw new RuntimeException("이미 존재하는 학번입니다.");

        Member member = new Member();
        member.setStudentId(studentId);
        member.setPassword(passwordEncoder.encode(password));
        memberRepository.save(member);
        return studentId;
    }
}