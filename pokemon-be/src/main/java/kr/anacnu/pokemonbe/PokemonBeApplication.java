package kr.anacnu.pokemonbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PokemonBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokemonBeApplication.class, args);
	}

}
