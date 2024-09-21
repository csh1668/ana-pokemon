package kr.anacnu.pokemonbe.pokemon_type;

import jakarta.persistence.*;
import kr.anacnu.pokemonbe.other_entities.BaseTimeEntity;
import kr.anacnu.pokemonbe.pokemon.Pokemon;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PokemonType extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(length = 10, nullable = false)
    private String name;

    /**
     * 이 타입에 속한 포켓몬들
     */
    @OneToMany(mappedBy = "type")
    private List<PokemonTypeRelation> pokemons;

    @Builder
    public PokemonType(String name) {
        this.name = name;
    }
}
