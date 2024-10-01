package kr.anacnu.pokemonbe.pokemon;

import jakarta.persistence.*;
import kr.anacnu.pokemonbe.other_entities.BaseTimeEntity;
import kr.anacnu.pokemonbe.pokemon_type.PokemonType;
import kr.anacnu.pokemonbe.pokemon_type.PokemonTypeRelation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "idx_pokedex_num", columnList = "pokedexNum"),
        @Index(name = "idx_name", columnList = "name")
})
public class Pokemon extends BaseTimeEntity {
    /**
     * DB에 저장되는 ID. 실제 포켓몬 도감 번호와는 전혀 다릅니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 포켓몬 이름
     */
    @Column(length = 100, nullable = false)
    private String name;

    /**
     * 포켓몬 도감 번호. 리전 폼 등으로 인해 중복될 수 있으므로 Id로 사용하지 않습니다.
     */
    @Column(nullable = false)
    private Long pokedexNum;

    /**
     * 포켓몬의 타입 목록
     */
    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PokemonTypeRelation> types;

    /**
     * 포켓몬의 키(m)
     */
    @Column(nullable = false)
    private Float height;

    /**
     * 포켓몬의 무게(kg)
     */
    @Column(nullable = false)
    private Float weight;

    /**
     * 이미지 주소
     */
    @Column(length = 500, nullable = true)
    private String imageUrl;

    @Column(length = 500, nullable = true)
    private String gifUrl;

    @Column(nullable = false)
    private Long vote;

    @Builder
    public Pokemon(String name, Long pokedexNum, List<PokemonType> types, Float height, Float weight, String imageUrl, String gifUrl, Long vote) {
        this.name = name;
        this.pokedexNum = pokedexNum;
        this.types = types.stream().map(type -> PokemonTypeRelation.of(this, type)).toList();
        this.height = height;
        this.weight = weight;
        this.imageUrl = imageUrl;
        this.gifUrl = gifUrl;
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "Pokemon(id=" + this.getId()
                + ", name=" + this.getName()
                + ", pokedexNum=" + this.getPokedexNum()
                + ", types=" + this.getTypes()
                + ", height=" + this.getHeight()
                + ", weight=" + this.getWeight()
                + ", imageUrl=" + this.getImageUrl()
                + ", gifUrl=" + this.getGifUrl()
                + ", vote=" + this.getVote()
                + ")";
    }
}
