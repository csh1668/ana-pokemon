package kr.anacnu.pokemonbe.pokemon;

import kr.anacnu.pokemonbe.pokemon_type.PokemonType;
import kr.anacnu.pokemonbe.pokemon_type.PokemonTypeRelation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PokemonDto {
    private Long pokedexNum;
    private String name;
    private List<String> types;
    private Float height;
    private Float weight;
    private String imageUrl;
    private String gifUrl;

    public PokemonDto(Pokemon pokemon) {
        this.pokedexNum = pokemon.getPokedexNum();
        this.name = pokemon.getName();
        this.height = pokemon.getHeight();
        this.weight = pokemon.getWeight();
        this.imageUrl = pokemon.getImageUrl();
        this.gifUrl = pokemon.getGifUrl();
        this.types = pokemon.getTypes().stream()
                .map(PokemonTypeRelation::getType)
                .map(PokemonType::getName).toList();
    }

    @Builder
    public PokemonDto(Long pokedexNum, String name, List<String> types, Float height, Float weight, String imageUrl, String gifUrl) {
        this.pokedexNum = pokedexNum;
        this.name = name;
        this.types = types;
        this.height = height;
        this.weight = weight;
        this.imageUrl = imageUrl;
        this.gifUrl = gifUrl;
    }

    @Override
    public String toString() {
        return "PokemonDto(pokedexNum=" + this.getPokedexNum()
                + ", name=" + this.getName()
                + ", types=" + this.getTypes()
                + ", height=" + this.getHeight()
                + ", weight=" + this.getWeight()
                + ", imageUrl=" + this.getImageUrl()
                + ", gifUrl=" + this.getGifUrl()
                + ")";
    }
}
