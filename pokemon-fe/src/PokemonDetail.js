import './App.js'
import React from 'react';
import { useParams, Link } from 'react-router-dom';

function PokemonDetail({ pokemonList }) {
  const { id } = useParams(); // URL의 id 파라미터 가져오기
  const pokemon = pokemonList.find((p) => p.ID === id); // 포켓몬 리스트에서 해당 ID 찾기

  if (!pokemon) {
    return <div>포켓몬 정보를 찾을 수 없습니다.</div>;
  }

  return (
    <div className="pokemonDetail">
      <Link to="/">← 돌아가기</Link>
      <h1>{pokemon.Name} (No.{'0'.repeat(4-pokemon.ID.length) + pokemon.ID})</h1>
      <img src={pokemon.GIF_URL || pokemon.IMAGE_URL} alt={pokemon.Name} width="200" height="200"/>
      <p>타입: {pokemon.Types}</p>
      <p>키: {pokemon['Height (m)']} m</p>
      <p>무게: {pokemon['Weight (kg)']} kg</p>
    </div>
  );
}

export default PokemonDetail;