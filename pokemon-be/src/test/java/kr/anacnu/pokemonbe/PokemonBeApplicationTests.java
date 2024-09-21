package kr.anacnu.pokemonbe;

import kr.anacnu.pokemonbe.pokemon.PokemonService;
import kr.anacnu.pokemonbe.pokemon_type.PokemonType;
import kr.anacnu.pokemonbe.pokemon_type.PokemonTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PokemonBeApplicationTests {
	@Autowired
	PokemonService pokemonService;
	@Autowired
	PokemonTypeService pokemonTypeService;

	@Test
	void addTestType() {
		pokemonTypeService.addPokemonType("풀");
	}

	@Test
	void addTestPokemon() {
		var pokemon = pokemonService.addPokemon("이상해씨", "풀");
		System.out.println(pokemon);
	}

}
