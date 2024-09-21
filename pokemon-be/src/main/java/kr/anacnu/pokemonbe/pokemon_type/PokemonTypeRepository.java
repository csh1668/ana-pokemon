package kr.anacnu.pokemonbe.pokemon_type;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PokemonTypeRepository extends JpaRepository<PokemonType, Long> {
    PokemonType findByName(String name);
}
