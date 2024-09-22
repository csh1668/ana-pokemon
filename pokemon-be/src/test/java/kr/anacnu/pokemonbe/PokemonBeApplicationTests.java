package kr.anacnu.pokemonbe;

import kr.anacnu.pokemonbe.pokemon.PokemonDto;
import kr.anacnu.pokemonbe.pokemon.PokemonService;
import kr.anacnu.pokemonbe.pokemon_type.PokemonTypeService;
import kr.anacnu.pokemonbe.utils.CsvUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@SpringBootTest
class PokemonBeApplicationTests {
	@Autowired
	PokemonService pokemonService;
	@Autowired
	PokemonTypeService pokemonTypeService;

	private final String pathPokemonData = "../pokemon_data.csv";
	private final String pathPokemonTypeData = "../pokemon_type_data.csv";

	@Test
	void addTypes() {
		var typesData = CsvUtil.readCsv(pathPokemonTypeData);
		if (typesData.size() < 2) {
			System.out.println("No data found in " + pathPokemonTypeData);
			return;
		}

		for (int i = 1; i < typesData.size(); i++) {
			var typeData = typesData.get(i);
			System.out.println(pokemonTypeService.addOrUpdatePokemonType(typeData[0], typeData[1]));
		}

		System.out.println(pokemonTypeService.getPokemonsByTypeName("풀"));
	}

	@Test
	void addPokemons() {
		var pokemonsData = CsvUtil.readCsv(pathPokemonData);
		if (pokemonsData.size() < 2) {
			System.out.println("No data found in " + pathPokemonData);
			return;
		}

		for (int i = 1; i < pokemonsData.size(); i++) {
			var pokemonData = pokemonsData.get(i);

			var types = Arrays.stream(pokemonData[4].split("\\|")).toList();
			var dto = PokemonDto.builder()
					.pokedexNum(Long.parseLong(pokemonData[0]))
					.name(pokemonData[1])
					.types(types)
					.height(Float.parseFloat(pokemonData[2]))
					.weight(Float.parseFloat(pokemonData[3]))
					.imageUrl(pokemonData[5])
					.build();

			System.out.println(pokemonService.addPokemon(dto));
		}
	}

}
