import './App.css'
import React, { useEffect, useState, useRef, useCallback } from 'react'
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom'
import PokemonDetail from './PokemonDetail.js'
import axios from 'axios'

function App() {
  const [visiblePokemons, setVisiblePokemons] = useState([])  // 현재 화면에 보여줄 포켓몬
  const [page, setPage] = useState(0)  // 현재 페이지
  const observerRef = useRef()  // IntersectionObserver ref

  const [isOpen, setIsOpen] = useState(false) //  팝업창 유무
  const [isSignUp, setIsSignUp] = useState(false)  // 회원가입or로그인 구별
  const [userId, setUserId] = useState('')    //  입력받은 아이디
  const [password, setPassword] = useState('')  // 입력받은 비밀번호
  const [password2, setPassword2] = useState('')  // 입력받은 비밀번호2 (회원가입시 사용)

  const [isLogin, setIsLogin] = useState(false)  // 로그인 여부
  const [memberId, setMemberId] = useState('')  // 현재 로그인 된 아이디
  const token = localStorage.getItem("accessToken") // JWT Token

  const [option, setOption] = useState('name')  // 검색 옵션
  const [keyWord, setKeyWord] = useState('')  //  검색 키워드
  const [isSearch, setIsSearch] = useState(true)  // 검색
  const [order, setOrder] = useState('asc') // 정렬 기준

  const [isMaxPage, setIsMaxPage] = useState(false) // 마지막 페이지 확인
  const [loading, setLoading] = useState(false) // 로딩 상태 관리

  // 페이지 로드 할 때마다 JWT Token으로 로그인 여부 확인 
  useEffect(()=>{
    axios({
      headers: {
        Authorization: `Bearer ${token}`
      },
      url: 'https://pokedex.anacnu.kr/me'
    })
    .then((res)=>{
      setIsLogin(true)
      setMemberId(res.data)
    })
    .catch((err)=>{

    })
  }, [])

  // Backend API ('/list') 를 통한 초기 데이터 획득 및 검색 시 데이터 획득
  useEffect(()=>{
    setVisiblePokemons([])
    axios({
      url: `https://pokedex.anacnu.kr/list?page=0&kw=${keyWord}&kind=${option}&order=${order}`
    })
    .then((res)=>res.data.content)
    .then((res)=>{
      console.log(`page : ${page} kw : ${keyWord} kind : ${option}`)
      setVisiblePokemons(res)
      setPage(0)
      setIsMaxPage(false)
    })
    .catch((err)=>{
      console.log('API 통신 실패 : ', err)
    })

  }, [isSearch])

   // page가 넘어갈 때 마다 데이터를 로드하도록 함.
   useEffect(()=>{
    console.log(`Page. ${page} 에서 로드 시도중`)
    axios({
      url: `https://pokedex.anacnu.kr/list?page=${page}&kw=${keyWord}&kind=${option}&order=${order}`
    })
    .then((res)=>{
      res = res.data.content;
      if (page === 0) {
        setVisiblePokemons(res)
      } else {
        setVisiblePokemons((previous)=>[...previous, ...res])
      }
      if (visiblePokemons.length % 50 != 0) setIsMaxPage(true)
    })
    .catch((err)=>{
      console.log('API 통신 실패 : ', err)
    })
    .finally(()=>{
      setLoading(false)
      console.log(`Loading 종료, 현재 페이지 : ${page}`)
    })
  }, [page])

  // 스크롤이 페이지 끝에 도달하면 추가 데이터를 로드
  const loadMorePokemons = useCallback(()=>{
    console.log(`현재 위치 : ${page}, 로딩중? : ${loading}`)
    if (loading) {
      console.log(`Loading 중... 현재 페이지 : ${page}`)
      return
    }
    console.log(`Page. ${page} 에서 시도 할걸?`)
    setLoading(true)
    setPage((page)=>page+1)
  }, [page, loading])

  // IntersectionObserver를 이용한 무한 스크롤 구현
  const lastPokemonElementRef = useCallback((node)=>{
    if (observerRef.current)  observerRef.current.disconnect()
    
    observerRef.current = new IntersectionObserver((entries)=>{
      if (entries[0].isIntersecting && !isMaxPage) {
        loadMorePokemons()  // 마지막 요소가 보이면 추가적으로 로드
      }
    })

    if (node) observerRef.current.observe(node)
  }, [loadMorePokemons, visiblePokemons.length])

  // 회원가입/로그인으로 창 전환
  const setSignMode = () => {
    setUserId('')
    setPassword('')
    setPassword2('')
    setIsSignUp(!isSignUp)
  }

  // 팝업
  const togglePopup = () => {
    setIsOpen(!isOpen)
  }

  // 팝업 바깥을 클릭했을 때 닫기
  const closePopup = (e) => {
    if (e.target.className === 'popup') {
      setIsOpen(false)
    }
  }

  // 입력 처리
  const handleSign = (e) => {
    if (isSignUp) {
      if (password !== password2) {
        alert('잘못된 비밀번호를 입력했습니다.')
        return
      }
      handleSignup(e)
    } else {
      handleSignin(e)
    }

    setUserId('')
    setPassword('')
    setPassword2('')
    setIsSignUp(false)
  }

  // 회원가입 처리
  const handleSignup = (e) => {
    e.preventDefault()
    console.log(`id : ${userId}, pw : ${password}`)
    // 회원가입 로직
    axios({
      method: "POST",
      url: `https://pokedex.anacnu.kr/sign-up`,
      data: {
        studentId: userId,
        password: password
      }
    })
    .then(()=>{
      alert('회원가입 성공')
    })
    .catch((err)=>{
      alert('회원가입 실패')
    })

    setIsOpen(false)
  }

  // 로그인 처리
  const handleSignin = (e) => {
    e.preventDefault()
    // 로그인 로직 작성
    axios({
      method: "POST",
      url: `https://pokedex.anacnu.kr/sign-in`,
      data: {
        studentId: userId,
        password: password
      }
    })
    .then((res)=>{
      res = res.data;
      console.log(res)
      console.log(`token : ${res.accessToken}`)

      localStorage.setItem('accessToken', res.accessToken)
      setMemberId(userId)
      setIsLogin(true)

      alert('로그인 성공')
    })
    .catch((err)=>{
      localStorage.setItem('accessToken', '')
      alert('로그인 실패')
    })

    console.log(`Id : ${userId}, Pw : ${password}`)
    setIsOpen(false) // 팝업 닫기
  }

  const clickSearch = ()=>{
    if(option === 'name' || option === 'type') {setOrder('asc')}
    else {setKeyWord('')}
    setIsSearch(!isSearch)
    setPage(0)
  }

  // 검색 처리
  const handleClick = () => {
    clickSearch()
  }

  const handleSignOut = () => {
    setIsLogin(false)
    setMemberId(false)
    localStorage.setItem('accessToken', '')
    togglePopup()
  }

  // 로그인 및 회원정보 창 
  const handleMemberInfo = () => {
    if (isLogin) {
      return memberInfo()
    } else {
      return signInOrUp()
    }
  }

  // 로그인 및 회원가입 창 구현
  const signInOrUp = () => {
    if (isSignUp) {
      return signUpComp()
    } else {
      return signInComp()
    }
  }

  // 로그인 컴포넌트
const signInComp = () => {
  return (
    <div className="popup" onClick={closePopup}>
      <div className="popup-inner" onClick={(e) => e.stopPropagation()}>
        <h2>로그인</h2>
        <form onSubmit={handleSign}>
          <label>
            아이디: 
            <input
              type="text"
              value={userId}
              onChange={(e) => setUserId(e.target.value)}
              required
              className="inputField"
            />
          </label>
          <br />
          <label>
            비밀번호: 
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="inputField"
            />
          </label>
          <br />
          <button type="button" className="button-secondary" onClick={setSignMode}>
            회원가입
          </button>
          <button type="submit" className="button-primary">로그인 하기</button>
        </form>
      </div>
    </div>
  );
}

// 회원가입 컴포넌트
const signUpComp = () => {
  return (
    <div className="popup" onClick={closePopup}>
      <div className="popup-inner" onClick={(e) => e.stopPropagation()}>
        <h2>회원가입</h2>
        <form onSubmit={handleSign}>
          <label>
            아이디: 
            <input
              type="text"
              value={userId}
              onChange={(e) => setUserId(e.target.value)}
              required
              className="inputField"
            />
          </label>
          <br />
          <label>
            비밀번호: 
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="inputField"
            />
          </label>
          <br />
          <label>
            비밀번호 확인: 
            <input
              type="password"
              value={password2}
              onChange={(e) => setPassword2(e.target.value)}
              required
              className="inputField"
            />
          </label>
          <br />
          <button type="button" className="button-secondary" onClick={setSignMode}>
            로그인
          </button>
          <button type="submit" className="button-primary">회원가입 하기</button>
        </form>
      </div>
    </div>
  );
}

// 회원 정보 컴포넌트
const memberInfo = () => {
  return (
    <div className="popup" onClick={closePopup}>
      <div className="popup-inner" onClick={(e) => e.stopPropagation()}>
        <form onSubmit={handleSignOut}>
          <h2>회원 정보</h2>
          <label className="memberInfoLabel">아이디: {memberId}</label>
          <br />
          <br />
          <button type="submit" className="button-primary">로그아웃</button>
        </form>
      </div>
    </div>
  );
}

  return (
    <Router>
      <Routes>
        {/* 메인 페이지 */}
        <Route path="/" element = {
          <div className="appContainer">
            {/* <button type="button" className="Button" onClick={loginPrompt}> */}
              {(isOpen && handleMemberInfo()) || ( // https://cdn-icons-png.flaticon.com/512/159/159833.png
                <button type='button' className='Button' onClick={togglePopup}>
                  <img src="https://i.namu.wiki/i/J-YFRHGRSfHZaBNzCNhaswI9HQurtpL7v2Vk76StYNixgwxk3uUtRQKtsuHx1zEMk3h1o66bjdJ8x8Yw5rPdosWNWnbvLGpEQhxM5K4qqhF5mWl7vhXUX9iF64tm_h5nzZIWM075FbZxzq3QvGhRfw.webp" width="72" height="72"/>
                </button>
              )}

            <div className="searchBar">
              <select 
                value={option} 
                onChange={(e) => setOption(e.target.value)}
                className="searchSelect"
              >
                <option>name</option>
                <option>type</option>
                <option>height</option>
                <option>weight</option>
                <option>vote</option>
              </select>
              
              {(!(option === 'height' || option === 'weight' || option === 'vote') && (
                <input 
                  type="text" 
                  placeholder="포켓몬 검색" 
                  value={keyWord}
                  onChange={(e) => setKeyWord(e.target.value)}
                  className="searchInput"
                />
              )) || (
                <select 
                  value={order === 'asc' ? '오름차순' : '내림차순'}
                  onChange={(e) => { setOrder(e.target.value === '오름차순' ? 'asc' : 'desc'); }}
                  className="orderSelect"
                >
                  <option value="오름차순">오름차순</option>
                  <option value="내림차순">내림차순</option>
                </select>
              )}
              
              <input 
                type="submit" 
                value="검색" 
                onClick={handleClick}
                className="searchButton"
              />
            </div>
            <div className="pokemonGridContainer">
              <div className='pokemonGrid'>
                {visiblePokemons.map((pokemonInfo, index)=>{
                  const pokemonId = pokemonInfo.pokedexNum

                  if (index === visiblePokemons.length - 1) {
                    return (
                      <Link to={`/pokemon/${pokemonId}`} key={pokemonInfo.pokedexNum}>
                        <div ref={lastPokemonElementRef} className="pokemonCard">
                          <img src={pokemonInfo.gifUrl || pokemonInfo.imageUrl} width="100" height="100" alt={pokemonInfo.name}/>
                          <p className="pokemonId">No.{'0'.repeat(4 - String(pokemonInfo.pokedexNum).length) + pokemonInfo.pokedexNum}</p>
                          <p className="pokemonName">{pokemonInfo.name}</p>
                        </div>
                      </Link>
                    )
                  } else {
                    return (
                      <Link to={`/pokemon/${pokemonId}`} key={pokemonInfo.pokedexNum}>
                        <div className="pokemonCard">
                          <img src={pokemonInfo.gifUrl || pokemonInfo.imageUrl} width="100" height="100" alt={pokemonInfo.name}/>
                          <p className="pokemonId">No.{'0'.repeat(4 - String(pokemonInfo.pokedexNum).length) + pokemonInfo.pokedexNum}</p>
                          <p className="pokemonName">{pokemonInfo.name}</p>
                        </div>
                      </Link>
                    )
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
  )
}

export default App