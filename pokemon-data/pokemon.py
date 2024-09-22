import requests
from multiprocessing import Pool
import csv



type_translation = {
    "normal": "노말",
    "fire": "불꽃",
    "water": "물",
    "electric": "전기",
    "grass": "풀",
    "ice": "얼음",
    "fighting": "격투",
    "poison": "독",
    "ground": "땅",
    "flying": "비행",
    "psychic": "에스퍼",
    "bug": "벌레",
    "rock": "바위",
    "ghost": "고스트",
    "dragon": "드래곤",
    "dark": "악",
    "steel": "강철",
    "fairy": "페어리"
}



def get_pokemon_data(pokemon_id):
    pokemon_url = f"https://pokeapi.co/api/v2/pokemon/{pokemon_id}"
    species_url = f"https://pokeapi.co/api/v2/pokemon-species/{pokemon_id}"


    pokemon_response = requests.get(pokemon_url)
    species_response = requests.get(species_url)
    

    if pokemon_response.status_code == 200 and species_response.status_code == 200:
        pokemon_data = pokemon_response.json()
        species_data = species_response.json()


        #API로 데이터 받기
        image_url = pokemon_data["sprites"]["front_default"]
        gif_url = pokemon_data['sprites']['versions']['generation-v']['black-white']['animated'].get('front_default', "N/A")
        name = pokemon_data["name"]
        pokemon_id = pokemon_data["id"]
        height = pokemon_data["height"] / 10  
        weight = pokemon_data["weight"] / 10  
        types = [t["type"]["name"] for t in pokemon_data["types"]]
        translated_types = [type_translation.get(t, t) for t in types]


        korean_name_entry = next((name for name in species_data["names"] if name["language"]["name"] == "ko"), None)
        korean_name = korean_name_entry["name"] if korean_name_entry else name


        print(f"{pokemon_id}번째 데이터 수집중...")



        return {
            "name": korean_name,
            "id": pokemon_id,
            "height": height,
            "weight": weight,
            "types": ', '.join(translated_types),
            "image_url": image_url,
            "gif_url": gif_url
        }
    



#멀티 프로세스
def collect_pokemon_data(pokemon_ids):
    with Pool(processes=8) as pool:
        pokemon_data = pool.map(get_pokemon_data, pokemon_ids)
    return pokemon_data



#csv에 데이터 저장
def save_to_csv(pokemon_list, filename):
    with open(filename, mode='w', newline='', encoding='utf-8') as file:
        writer = csv.writer(file)
        writer.writerow(["ID", "이름", "키 (m)", "몸무게 (kg)", "타입", "IMAGE_URL", "GIF_URL"])


        for pokemon in pokemon_list:
            writer.writerow([pokemon["id"], pokemon["name"], pokemon["height"], pokemon["weight"], pokemon["types"], pokemon["image_url"], pokemon["gif_url"]])

            print(f"{filename}에 데이터 저장 완료")







if __name__ == "__main__":
    pokemon_ids = list(range(1, 1010)) 
    pokemon_list = collect_pokemon_data(pokemon_ids)

    save_to_csv(pokemon_list, "pokemon_data.csv")
