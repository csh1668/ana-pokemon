package kr.anacnu.pokemonbe.pokemon_type;

import jakarta.persistence.*;
import kr.anacnu.pokemonbe.other_entities.BaseTimeEntity;
import kr.anacnu.pokemonbe.pokemon.Pokemon;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

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
    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    private Set<PokemonTypeRelation> pokemons;

    @Column(length = 500, nullable = false)
    private String imageUrl;

    @Builder
    public PokemonType(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "PokemonType(id=" + this.getId()
                + ", name=" + this.getName()
                + ", imageUrl=" + this.getImageUrl()
                + ")";
    }
}
