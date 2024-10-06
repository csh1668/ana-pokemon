package kr.anacnu.pokemonbe.utils;

import jakarta.annotation.PostConstruct;
import kr.anacnu.pokemonbe.pokemon.PokemonDto;
import kr.anacnu.pokemonbe.pokemon.PokemonService;
import kr.anacnu.pokemonbe.pokemon_type.PokemonTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DBInitializer {
    private final String pathPokemonData = "/app/data/pokemon_data.csv";
    private final String pathPokemonTypeData = "/app/data/pokemon_type_data.csv";

    private final PokemonService pokemonService;
    private final PokemonTypeService pokemonTypeService;

    @PostConstruct
    public void init() {
        addTypes();
        addPokemons();
        System.out.println("DB Initialized");
    }

    private void addTypes() {
        var typesData = CsvUtil.readCsv(pathPokemonTypeData);
        if (typesData.size() < 2) {
            System.out.println("No data found in " + pathPokemonTypeData);
            return;
        }

        for (int i = 1; i < typesData.size(); i++) {
            var typeData = typesData.get(i);
            pokemonTypeService.addOrUpdatePokemonType(typeData[0], typeData[1]);
        }
        System.out.println(typesData.size() + " types are added or updated");

//        System.out.println(pokemonTypeService.getPokemonsByTypeName("풀"));
    }

    private void addPokemons() {
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
                    .gifUrl(pokemonData.length > 6 ? pokemonData[6] : null)
                    .build();

            pokemonService.addPokemon((dto));
        //    System.out.println(pokemonService.addPokemon(dto));
        }
        System.out.println(pokemonsData.size() + " pokemons are added or updated");
    }
}
