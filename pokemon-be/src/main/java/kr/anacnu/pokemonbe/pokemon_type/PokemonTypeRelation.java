package kr.anacnu.pokemonbe.pokemon_type;

import jakarta.persistence.*;
import kr.anacnu.pokemonbe.other_entities.BaseTimeEntity;
import kr.anacnu.pokemonbe.pokemon.Pokemon;
import lombok.*;

/**
 * 포켓몬과 타입 관계 사이의 중간 테이블
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PokemonTypeRelation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pokemon_id")
    private Pokemon pokemon;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private PokemonType type;

    @Builder
    public PokemonTypeRelation(Pokemon pokemon, PokemonType type) {
        this.pokemon = pokemon;
        this.type = type;
    }

    public static PokemonTypeRelation of(Pokemon pokemon, PokemonType type){
        return PokemonTypeRelation.builder()
                .pokemon(pokemon)
                .type(type)
                .build();
    }
}
