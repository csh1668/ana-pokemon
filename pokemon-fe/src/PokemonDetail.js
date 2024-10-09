import './App.js'
import React, { useEffect, useState } from 'react';
import { useParams, Link, useLocation } from 'react-router-dom';
import axios from 'axios';
import './PokemonDetail.css'

function PokemonDetail({pokeList}) {
  const location = useLocation()
  const [pokemon, setPokemon] = useState(location.state?.pokemonData || [])  // 포켓몬 리스트에서 해당 ID 찾기
  const [vote, setVote] = useState(pokemon.vote || 0) // 포켓몬의 vote 변수
  const [typeArr, setTypeArr] = useState(pokemon.types || [])  // 포켓몬의 type 변수
  const { id } = useParams()  // URL의 id 파라미터 가져오기
  const token = localStorage.getItem("accessToken") // JWT Token

  useEffect(() => {
    if (!location.state?.pokemonData) {
      axios.get(`https://pokedex.anacnu.kr/get/${id}`)
        .then((res) => {
          const data = res.data;
          setPokemon(data);
          setVote(data.vote);
          setTypeArr(data.types);
        })
        .catch((err) => console.error('Error fetching Pokémon data:', err));
    }
  }, [id, location.state]);

  if (!pokemon) {
    return <div>포켓몬 정보를 찾을 수 없습니다.</div>;
  }

  const handleVote = ()=>{
    axios({
      headers: {
        Authorization: `Bearer ${token}`
      },
      url: `https://pokedex.anacnu.kr/vote/${pokemon.name}`,
      method: "POST"
    })
    .then((res)=>{
      setVote(res.data)
      alert('투표를 완료했습니다.')
    })
    .catch((err)=>{
      alert('투표에 실패했습니다. \n투표 기능은 로그인이 필요합니다. ', err)
    })
  }

  return (
    <div className='appContainer'>
      <div className="pokemonDetail">
        <Link to="/">← 돌아가기</Link>
        <h1>{pokemon.name} (No.{'0'.repeat(4 - String(id).length) + pokemon.pokedexNum})</h1>
        <img src={pokemon.gifUrl || pokemon.imageUrl} alt={pokemon.name} width="200" height="200"/>
        <button onClick={handleVote}>투표하기</button>
        <p>득표 수: {vote}</p>
        <p>타입: {typeArr.join(', ')}</p>
        <p>키: {pokemon.height} m</p>
        <p>무게: {pokemon.weight} kg</p>
      </div>
    </div>
  );
}

export default PokemonDetail;