package kr.anacnu.pokemonbe.pokemon;

import kr.anacnu.pokemonbe.pokemon_type.PokemonType;
import kr.anacnu.pokemonbe.pokemon_type.PokemonTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PokemonService {
    private final PokemonRepository pokemonRepository;
    private final PokemonTypeRepository pokemonTypeRepository;

    public Optional<Pokemon> findPokemonByName(String name) {
        return pokemonRepository.findByName(name);
    }

    /**
     * (테스트용) 타입을 하나 가진 포켓몬을 추가합니다.
     * TODO: 입력값과 반환값을 DTO로 바꾸기
     * @param name
     * @param typeName
     * @return
     */
    public Pokemon addPokemon(String name, String typeName) {
        var type = pokemonTypeRepository.findByName(typeName);
        var types = new ArrayList<PokemonType>(); types.add(type);
        return pokemonRepository.save(Pokemon.builder().name(name).types(types).pokedexNum(1L).build());
    }
}
