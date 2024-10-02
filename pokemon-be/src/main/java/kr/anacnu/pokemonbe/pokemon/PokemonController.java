package kr.anacnu.pokemonbe.pokemon;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PokemonController {
    private final PokemonService pokemonService;

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

    @PostMapping("/vote/{name}")
    public ResponseEntity<?> votePokemon(@PathVariable("name") String name) {
        try {
            return ResponseEntity.ok(pokemonService.votePokemon(name));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
