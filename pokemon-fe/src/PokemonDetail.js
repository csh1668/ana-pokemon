import './App.js'
import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';

<<<<<<< HEAD
function PokemonDetail({pokeList}) {
  const [pokemon, setPokemon] = useState([])  // 포켓몬 리스트에서 해당 ID 찾기
  const { id } = useParams()  // URL의 id 파라미터 가져오기
  useEffect(()=>{
    setPokemon(pokeList[id-1])
=======
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
>>>>>>> d435a5e08c4aad7101fdb588047c180110e8f517
  }, [])

  if (!pokemon) {
    return <div>포켓몬 정보를 찾을 수 없습니다.</div>;
  }
  console.log(pokemon)

  return (
    <div className="pokemonDetail">
      <Link to="/">← 돌아가기</Link>
<<<<<<< HEAD
      <h1>{pokemon.name} (No.{'0'.repeat(4 - String(id).length) + pokemon.pokedexNum})</h1>
      <img src={pokemon.gifUrl || pokemon.imageUrl} alt={pokemon.name} width="200" height="200"/>
      <p>타입: {pokemon.types}</p>
      <p>키: {pokemon.height} m</p>
      <p>무게: {pokemon.weight} kg</p>
=======
      {/* <h1>{pokemon.name} (No.{'0'.repeat(4 - String(pokemon.pokedexNum).length) + pokemon.pokedexNum})</h1> */}
      <img src={pokemon.gifUrl || pokemon.imageUrl} alt={pokemon.name} width="200" height="200"/>
      <p>타입: {pokemon.types}</p>
      <p>키: {pokemon['Height (m)']} m</p>
      <p>무게: {pokemon['Weight (kg)']} kg</p>
>>>>>>> d435a5e08c4aad7101fdb588047c180110e8f517
    </div>
  );
}

export default PokemonDetail;