import './App.css';
import React, { useEffect, useState, useRef, useCallback } from 'react'
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom'
import PokemonDetail from './PokemonDetail.js'
import axios from 'axios'

function App() {
  const [visiblePokemons, setVisiblePokemons] = useState([])  // 현재 화면에 보여줄 포켓몬
  const [page, setPage] = useState(0)  // 현재 페이지
  const observerRef = useRef()  // IntersectionObserver ref

  // Backend API ('/list') 를 통한 초기 데이터 획득
  useEffect(()=>{
    axios({
      url: '/proxy/list?page=0'
    })
    .then((res)=>res.data.content)
    .then((res)=>{
      console.log(res)
      setVisiblePokemons(res)
    })
    .catch((err)=>{
      console.log('API 통신 실패 : ', err)
    })
  }, [])

  // 스크롤이 페이지 끝에 도달하면 추가 데이터를 로드
  const loadMorePokemons = useCallback(()=>{
    const nextPage = page + 1

    axios({
      url: `/proxy/list?page=${nextPage}`
    })
    .then((res)=>res.data.content)
    .then((res)=>{
      setVisiblePokemons((previous)=>[...previous, ...res])
      setPage(nextPage)
    })
  }, [page])

  // IntersectionObserver를 이용한 무한 스크롤 구현
  const lastPokemonElementRef = useCallback((node)=>{
    if (observerRef.current)  observerRef.current.disconnect()
    
    observerRef.current = new IntersectionObserver((entries)=>{
      if (entries[0].isIntersecting && page < 21) {
        loadMorePokemons()  // 마지막 요소가 보이면 추가적으로 로드
      }
    })

    if (node) observerRef.current.observe(node)
  }, [loadMorePokemons, visiblePokemons.length])

  return (
    <Router>
      <Routes>
        {/* 메인 페이지 */}
        <Route path="/" element = {
          <div className="appContainer">
            <button type="button" className="Button">
              <img src="https://cdn-icons-png.flaticon.com/512/159/159833.png" width="50" height="50"/>
            </button>

            <div className='searchBar'>
              <select>
                <option>타입</option>
              </select>
              <input type="text" placeholder='포켓몬 검색'/>
              <input type="submit" value="검색"/>
            </div>

            <div className="pokemonGridContainer">
              <div className='pokemonGrid'>
                {visiblePokemons.map((pokemonInfo, index)=>{
                  const pokemonId = pokemonInfo.pokedexNum

                  if (index === visiblePokemons.length - 1) {
                    return (
                      <Link to={`/pokemon/${pokemonId}`} key={pokemonInfo.pokedexNum}>
                        <div ref={lastPokemonElementRef} className="pokemonCard" key={pokemonInfo.pokedexNum}>
                          <img src={pokemonInfo.gifUrl || pokemonInfo.imageUrl} width="100" height="100" alt={pokemonInfo.name}/>
                          <p className="pokemonId">No.{'0'.repeat(4 - String(pokemonInfo.pokedexNum).length) + pokemonInfo.pokedexNum}</p>
                          <p className="pokemonName">{pokemonInfo.name}</p>
                        </div>
                      </Link>
                    );
                  } else {
                    return (
                      <Link to={`/pokemon/${pokemonId}`} key={pokemonInfo.pokedexNum}>
                        {console.log(`pokemon : ${pokemonInfo.name}`)}
                        <div className="pokemonCard" key={pokemonInfo.pokedexNum}>
                          <img src={pokemonInfo.gifUrl || pokemonInfo.imageUrl} width="100" height="100" alt={pokemonInfo.name}/>
                          <p className="pokemonId">No.{'0'.repeat(4 - String(pokemonInfo.pokedexNum).length) + pokemonInfo.pokedexNum}</p>
                          <p className="pokemonName">{pokemonInfo.name}</p>
                        </div>
                      </Link>
                    );
                  }
                })}
              </div>
            </div>
          </div>
        }>
        </Route>

        {/* 포켓몬 상세 페이지 */}
        <Route path="/pokemon/:id" element={<PokemonDetail pokeList = {visiblePokemons}/>}></Route>
      </Routes>
    </Router>
  );
}

export default App;