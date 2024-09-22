import logo from './logo.svg';
import './App.css';
import data from './views/data'

function App() {
  const dt = data
  console.log("data : ", dt)
  return (
    <div className="App">
      <button type="button">
        <img src="https://cdn-icons-png.flaticon.com/512/159/159833.png" width="50" height="50"/>
      </button>
        <table>
            <tr>
                <td><select>
                    <option>타입</option>
                    <option>특성</option>
                </select></td>
                <td><input type="text"/></td>
                <td><input type="submit" value="검색"/></td>
            </tr>
        </table>
        <table id="pokemon_table">
          <thead>
            <tr>
              <td>img</td>
              <td>name</td>
              <td>number</td>
              <td>type</td>
              <td>ability</td>
              <td>height</td>
              <td>weight</td>
            </tr>
          </thead>
          <tbody>
            {dt.map((d)=>(
              <tr key={d.number}>
                <td>
                  <img src={d.img} width="50" height="50"></img>
                </td>
                <td>{d.name}</td>
                <td>{d.number}</td>
                <td>{d.type}</td>
                <td>{d.ability}</td>
                <td>{d.height}</td>
                <td>{d.weight}</td>
              </tr>
            ))}
          </tbody>
        </table>
    </div>
  );
}

export default App;