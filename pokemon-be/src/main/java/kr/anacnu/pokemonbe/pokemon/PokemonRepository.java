package kr.anacnu.pokemonbe.pokemon;

import kr.anacnu.pokemonbe.pokemon_type.PokemonType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PokemonRepository extends JpaRepository<Pokemon, Long> {

    Page<Pokemon> findAll(Specification<Pokemon> spec, Pageable pageable);

    Optional<Pokemon> findByName(String name);

    List<Pokemon> findAllByPokedexNum(Long pokedexNum);

    /**
     * 특정 타입을 가지는 포켓몬들을 찾습니다.
     * @param type 찾을 타입
     * @return 해당 타입을 가지는 포켓몬들
     */
    List<Pokemon> findAllByTypes_Type(PokemonType type);

    /**
     * 특정 타입들을 가지는 포켓몬들을 찾습니다.
     * @param types 찾을 타입들
     * @return 해당 타입들을 가지는 포켓몬들
     */
    @Query("SELECT DISTINCT p FROM Pokemon p JOIN p.types t where t.type in :types")
    List<Pokemon> findAllByTypes(List<PokemonType> types);
}
