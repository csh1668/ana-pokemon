package kr.anacnu.pokemonbe.pokemon_type;

import kr.anacnu.pokemonbe.pokemon.Pokemon;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PokemonTypeService {
    private final PokemonTypeRepository pokemonTypeRepository;

    public PokemonType findPokemonTypeByName(String name) {
        return pokemonTypeRepository.findByName(name).orElse(null);
    }

    public PokemonType addOrUpdatePokemonType(String name, String imgUrl) {
        var existingType = findPokemonTypeByName(name);

        // 존재하지 않는다면 새로 추가
        // 이미 존재하지만 이미지 URL이 다르다면 업데이트
        // 이미 존재하고 이미지 URL도 같다면 그대로 반환
        if (existingType == null) {
            var newType = PokemonType.builder()
                    .name(name)
                    .imageUrl(imgUrl)
                    .build();
            return pokemonTypeRepository.save(newType);
        } else if (!existingType.getImageUrl().equals(imgUrl)) {
            existingType.setImageUrl(imgUrl);
            return pokemonTypeRepository.save(existingType);
        } else {
            return existingType;
        }
    }

    @Transactional(readOnly = true)
    public Set<PokemonTypeRelation> getPokemonsByTypeName(String name) {
        var type = findPokemonTypeByName(name);
        if (type == null) {
            return null;
        }
        Hibernate.initialize(type.getPokemons());
        return type.getPokemons();
    }
}
