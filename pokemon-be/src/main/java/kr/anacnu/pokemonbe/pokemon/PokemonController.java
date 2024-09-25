package kr.anacnu.pokemonbe.pokemon;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PokemonController {
    private final PokemonService pokemonService;

    @GetMapping("/list")
    public ResponseEntity<?> listPokemons(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "") String kw) {
        try {
            return ResponseEntity.ok(pokemonService.getPokemons(page, kw));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/{name}")
    public ResponseEntity<?> getPokemon(@PathVariable("name") String name) {
        try {
            return ResponseEntity.ok(pokemonService.getPokemonByName(name));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
