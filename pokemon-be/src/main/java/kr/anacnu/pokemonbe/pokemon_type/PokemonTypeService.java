package kr.anacnu.pokemonbe.pokemon_type;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PokemonTypeService {
    private final PokemonTypeRepository pokemonTypeRepository;

    public PokemonType findPokemonTypeByName(String name) {
        return pokemonTypeRepository.findByName(name);
    }

    public PokemonType addPokemonType(String name) {
        return pokemonTypeRepository.save(PokemonType.builder().name(name).build());
    }
}
