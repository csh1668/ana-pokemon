package kr.anacnu.pokemonbe.pokemon;

import kr.anacnu.pokemonbe.pokemon_type.PokemonType;
import kr.anacnu.pokemonbe.pokemon_type.PokemonTypeRelation;
import kr.anacnu.pokemonbe.pokemon_type.PokemonTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.Hashtable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PokemonService {
    private final PokemonRepository pokemonRepository;
    private final PokemonTypeRepository pokemonTypeRepository;

    private static final int PAGE_SIZE = 50;

    private final Hashtable<String, PokemonType> typeCached = new Hashtable<>();

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

    public PagedModel<PokemonDto> getPokemons(int page, String kw) {
        List<Sort.Order> sorts = List.of(Sort.Order.asc("pokedexNum"));
        var pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(sorts));
        var spec = nameLike(kw);
        var ret = pokemonRepository.findAll(spec, pageable).map(PokemonDto::new);

        return new PagedModel<>(ret);
    }

    private Specification<Pokemon> nameLike(String kw) {
        return (b, query, cb) -> {
            query.distinct(true);
            return cb.or(cb.like(b.get("name"), "%" + kw + "%"));
        };
    }

    public PokemonDto getPokemonByName(String name) {
        var pokemon = findPokemonByName(name);
        if (pokemon == null) {
            return null;
        } else {
            return new PokemonDto(pokemon);
        }
    }

    private Pokemon findPokemonByName(String name) {
        return pokemonRepository.findByName(name).orElse(null);
    }
}
