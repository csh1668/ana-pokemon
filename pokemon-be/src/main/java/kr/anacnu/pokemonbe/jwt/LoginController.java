package kr.anacnu.pokemonbe.jwt;


import kr.anacnu.pokemonbe.member.MemberService;
import kr.anacnu.pokemonbe.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;
    private final CustomUserDetailsService customUserDetailsService;

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

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody LoginDto loginDto) {
        try {
            return ResponseEntity.ok(memberService.signUp(loginDto.getStudentId(), loginDto.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo() {
        try {
            return ResponseEntity.ok(SecurityUtil.getCurrentStudentId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
