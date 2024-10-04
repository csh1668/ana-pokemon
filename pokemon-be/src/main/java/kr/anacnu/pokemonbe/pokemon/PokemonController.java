package kr.anacnu.pokemonbe.pokemon;

import kr.anacnu.pokemonbe.jwt.CustomUserDetailsService;
import kr.anacnu.pokemonbe.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PokemonController {
    private final PokemonService pokemonService;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("/list")
    @Cacheable(value = "pokemons", key = "#page + #kw + #kind + #order")
    public ResponseEntity<?> listPokemons(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "") String kw,
                                          @RequestParam(defaultValue = "name") String kind,
                                          @RequestParam(defaultValue = "asc") String order) {
        try {
            boolean isAsc = order.equals("asc");
            var searchType = PokemonSearchType.valueOf(kind.toUpperCase());
            return ResponseEntity.ok(pokemonService.getPokemons(page, kw, searchType, isAsc));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 포켓몬 이름 또는 도감 번호로 포켓몬을 검색합니다.
     * @param name 숫자로만 구성된다면 도감 번호로, 그렇지 않다면 포켓몬 이름으로 간주합니다.
     * @return
     */
    @GetMapping("/get/{name}")
    public ResponseEntity<?> getPokemon(@PathVariable("name") String name) {
        try {
            boolean isNumber = name.chars().allMatch(Character::isDigit);
            if (isNumber) {
                return ResponseEntity.ok(pokemonService.getPokemonByPokedexNum(Long.parseLong(name)));
            } else {
                return ResponseEntity.ok(pokemonService.getPokemonByName(name));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 해당 포켓몬에게 투표합니다.
     * @param name
     * @return
     */
    @PostMapping("/vote/{name}")
    public ResponseEntity<?> votePokemon(@PathVariable("name") String name) {
        try {
            var userInfo = customUserDetailsService.loadUserByUsername(SecurityUtil.getCurrentStudentId());
            if (userInfo == null) {
                return ResponseEntity.badRequest().body("이 작업을 수행하려면 로그인이 필요합니다.");
            }
            boolean isNumber = name.chars().allMatch(Character::isDigit);
            if (isNumber) {
                return ResponseEntity.ok(pokemonService.votePokemonByPokedexNum(Long.parseLong(name)));
            } else {
                return ResponseEntity.ok(pokemonService.votePokemonByName(name));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-vote/{name}")
    public ResponseEntity<?> resetVotePokemon(@PathVariable("name") String name) {
        try {
            var userInfo = customUserDetailsService.loadUserByUsername(SecurityUtil.getCurrentStudentId());
            if (userInfo == null) {
                return ResponseEntity.badRequest().body("이 작업을 수행하려면 로그인이 필요합니다.");
            }
            boolean isNumber = name.chars().allMatch(Character::isDigit);
            if (isNumber) {
                return ResponseEntity.ok(pokemonService.resetVotePokemonByPokedexNum(Long.parseLong(name)));
            } else {
                return ResponseEntity.ok(pokemonService.resetVotePokemonByName(name));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-vote-all")
    public ResponseEntity<?> resetVoteAll() {
        try {
            var userInfo = customUserDetailsService.loadUserByUsername(SecurityUtil.getCurrentStudentId());
            if (userInfo == null) {
                return ResponseEntity.badRequest().body("이 작업을 수행하려면 로그인이 필요합니다.");
            }
            return ResponseEntity.ok(pokemonService.resetVoteAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
