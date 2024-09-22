package kr.anacnu.pokemonbe.pokemon;

import kr.anacnu.pokemonbe.pokemon_type.PokemonType;
import kr.anacnu.pokemonbe.pokemon_type.PokemonTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Hashtable;

@Service
@RequiredArgsConstructor
public class PokemonService {
    private final PokemonRepository pokemonRepository;
    private final PokemonTypeRepository pokemonTypeRepository;

    private final Hashtable<String, PokemonType> typeCached = new Hashtable<>();

    public Pokemon findPokemonByName(String name) {
        return pokemonRepository.findByName(name).orElse(null);
    }

    public Pokemon addPokemon(PokemonDto dto) {
        var existingPokemon = findPokemonByName(dto.getName());
        if (existingPokemon == null) {
            var types = dto.getTypes().stream().map(this::findPokemonTypeByName).toList();
            var newPokemon = Pokemon.builder()
                    .pokedexNum(dto.getPokedexNum())
                    .name(dto.getName())
                    .height(dto.getHeight())
                    .weight(dto.getWeight())
                    .imageUrl(dto.getImageUrl())
                    .types(types)
                    .build();
            return pokemonRepository.save(newPokemon);
        }
        // 값 업데이트는 아직 지원하지 않습니다.
        System.out.println("이미 존재하는 포켓몬: " + existingPokemon.getName());
        return existingPokemon;
    }

    private PokemonType findPokemonTypeByName(String name) {
        if (typeCached.containsKey(name)) {
            return typeCached.get(name);
        } else {
            var type = pokemonTypeRepository.findByName(name).orElse(null);
            if (type != null) {
                typeCached.put(name, type);
            }
            return type;
        }
    }
}
