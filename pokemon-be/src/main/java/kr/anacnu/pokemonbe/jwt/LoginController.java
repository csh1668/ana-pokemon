package kr.anacnu.pokemonbe.jwt;


import kr.anacnu.pokemonbe.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    /**
     * 로그인을 시도합니다.
     * 성공한다면 정상적인 JWT Token을 반환합니다.
     * 실패한다면 빈 토큰과 UNAUTHORIZED 상태 코드를 반환합니다.
     * @param loginDto
     * @return
     */
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody LoginDto loginDto) {
        try {
            String token = memberService.signIn(loginDto.getStudentId(), loginDto.getPassword());
            JwtToken jwtToken = new JwtToken("Bearer", token);
            return ResponseEntity.ok(jwtToken);
        } catch (BadCredentialsException e) {
            JwtToken emptyToken = new JwtToken("Bearer","" );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(emptyToken);
        }
    }
}
