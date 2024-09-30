import './App.js'
import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';

function PokemonDetail({pokeName}) {
  const [pokemon, setPokemon] = useState([])  // 포켓몬 리스트에서 해당 ID 찾기
  console.log(`ASD ? : ${pokeName}`)

  useEffect(()=>{
    axios({
      url: `/proxy/get/${pokeName}`
    })
    .then((res)=>{
      console.log(`PokeName : ${pokeName}`)
      console.log('AXIOS : ')
      console.log(res)
      setPokemon(res)
    })
    .catch((err)=>{
      return <div>포켓몬 정보를 찾을 수 없습니다.</div>
    })
  }, [])

  if (!pokemon) {
    return <div>포켓몬 정보를 찾을 수 없습니다.</div>;
  }
  console.log(pokemon)

  return (
    <div className="pokemonDetail">
      <Link to="/">← 돌아가기</Link>
      {/* <h1>{pokemon.name} (No.{'0'.repeat(4 - String(pokemon.pokedexNum).length) + pokemon.pokedexNum})</h1> */}
      <img src={pokemon.gifUrl || pokemon.imageUrl} alt={pokemon.name} width="200" height="200"/>
      <p>타입: {pokemon.types}</p>
      <p>키: {pokemon['Height (m)']} m</p>
      <p>무게: {pokemon['Weight (kg)']} kg</p>
    </div>
  );
}

export default PokemonDetail;