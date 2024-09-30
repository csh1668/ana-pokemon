import './App.css';
import React, { useEffect, useState, useRef, useCallback } from 'react'
import Papa from 'papaparse'  // CSV Parse를 위한 패키지
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom'
import PokemonDetail from './PokemonDetail.js'
import axios from 'axios'

function App() {
  const [pokemonList, setPokemonList] = useState([])  // public/pokemon_data.csv 에서 불러온 정보 저장용 (임시)
  const [visiblePokemons, setVisiblePokemons] = useState([])  // 현재 화면에 보여줄 포켓몬
  const [page, setPage] = useState(1)  // 현재 페이지
  const observerRef = useRef()  // IntersectionObserver ref
  const ITEMS_PER_PAGE = 18  // 한 번에 보여줄 포켓몬 수 (3*3)

  useEffect(()=>{
    fetch('/pokemon_data.csv')
    .then((result)=>result.text())  // CSV -> txt
    .then((csvText)=>{
      Papa.parse(csvText, {
        header: true,  // 첫 줄 키로 사용
        skipEmptyLines: true, // 빈 줄 무시
        complete: (result)=>{
          setPokemonList(result.data)  
          setVisiblePokemons(result.data.slice(0, ITEMS_PER_PAGE))
        },
        error: (error)=>{
          console.error('Error parsing CSV: ', error)
        }
      })
    })
    .catch((error)=>{
      console.error('Error fetching CSV file: ', error)
    })
  }, [])

  // Backend API ('/list') 를 통한 초기 데이터 획득
  useEffect(()=>{
    axios({
      url: '/proxy/list'
    })
    .then((res)=>res.data.content)
    .then((res)=>{
      console.log(res)
    })
    .catch((err)=>{
      console.log('API 통신 실패 : ', err)
    })
  }, [])

  // 스크롤이 페이지 끝에 도달하면 추가 데이터를 로드
  const loadMorePokemons = useCallback(()=>{
    const nextPage = page + 1
    const startIdx = page * ITEMS_PER_PAGE
    const endIdx = startIdx + ITEMS_PER_PAGE
    const morePokemons = pokemonList.slice(startIdx, endIdx)

    setVisiblePokemons((previous)=>[...previous, ...morePokemons])  // 기존 데이터에 추가
    setPage(nextPage)  // Page 추가
  }, [page, pokemonList])

  // IntersectionObserver를 이용한 무한 스크롤 구현
  const lastPokemonElementRef = useCallback((node)=>{
    if (observerRef.current)  observerRef.current.disconnect()
    
    observerRef.current = new IntersectionObserver((entries)=>{
      if (entries[0].isIntersecting && pokemonList.length > visiblePokemons.length) {
        loadMorePokemons()  // 마지막 요소가 보이면 추가적으로 로드
      }
    })

    if (node) observerRef.current.observe(node)
  }, [loadMorePokemons, pokemonList.length, visiblePokemons.length])

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
                  const pokemonId = pokemonInfo.ID

                  if (index === visiblePokemons.length - 1) {
                    return (
                      <Link to={`/pokemon/${pokemonId}`} key={pokemonInfo.ID}>
                        <div ref={lastPokemonElementRef} className="pokemonCard" key={pokemonInfo.ID}>
                          <img src={pokemonInfo.GIF_URL || pokemonInfo.IMAGE_URL} width="100" height="100" alt={pokemonInfo.Name}/>
                          <p className="pokemonId">No.{'0'.repeat(4-pokemonInfo.ID.length) + pokemonInfo.ID}</p>
                          <p className="pokemonName">{pokemonInfo.Name}</p>
                        </div>
                      </Link>
                    );
                  } else {
                    return (
                      <Link to={`/pokemon/${pokemonId}`} key={pokemonInfo.ID}>
                        <div className="pokemonCard" key={pokemonInfo.ID}>
                          <img src={pokemonInfo.GIF_URL || pokemonInfo.IMAGE_URL} width="100" height="100" alt={pokemonInfo.Name}/>
                          <p className="pokemonId">No.{'0'.repeat(4-pokemonInfo.ID.length) + pokemonInfo.ID}</p>
                          <p className="pokemonName">{pokemonInfo.Name}</p>
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
        <Route path="/pokemon/:id" element={<PokemonDetail pokemonList = {pokemonList}/>}></Route>
      </Routes>
    </Router>
  );
}

export default App;