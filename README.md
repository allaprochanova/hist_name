 Генератор случайных имен (исторические)

Многоязычная утилита для генерации аутентичных исторических имён из различных культур и эпох.  
Идеально подходит для писателей, разработчиков игр, исторических реконструкций и творческих проектов.

## Особенности
- Поддержка нескольких исторических культур:
  - **Викинги** (скандинавские имена)
  - **Древний Рим** (римские имена)
  - **Средневековая Англия** (англо-саксонские и нормандские имена)
  - **Древняя Греция** (греческие имена)
  - **Древняя Русь** (славянские имена)
  - **Кельты** (ирландские/валлийские имена)
- Генерация как мужских, так и женских имён.
- Опция добавления фамилий (родовых имён, прозвищ).
- Опция добавления титулов (король, герцог, ярл, конунг и т.д.).
- Воспроизводимость с помощью seed.
- Экспорт в форматы: текст, JSON, CSV.
- Цветной вывод в терминале (где поддерживается).
- Поддержка аргументов командной строки для автоматизации.

## Установка и запуск
Для каждого языка требуются соответствующие инструменты.

### Запуск на разных языках

1. **Python**  
   Установка: `pip install colorama` (опционально).  
   Запуск: `python hist_name.py --culture viking --gender male --count 5`

2. **JavaScript (Node.js)**  
   Установка: `npm install commander chalk`  
   Запуск: `node hist_name.js --culture roman --gender female --count 3`

3. **Go**  
   Запуск: `go run hist_name.go --culture celtic --gender male --count 5`

4. **Rust**  
   Сборка: `cargo build --release`  
   Запуск: `cargo run -- --culture medieval --gender female --count 3`

5. **Java**  
   Сборка: `javac -cp gson.jar HistName.java`  
   Запуск: `java -cp .;gson.jar HistName --culture viking --gender male --count 5`

6. **C# (.NET Core)**  
   Запуск: `dotnet run -- --culture roman --gender female --count 3`

7. **C++ (Linux)**  
   Сборка: `g++ -std=c++11 -o hist_name hist_name.cpp`  
   Запуск: `./hist_name --culture celtic --gender male --count 5`

8. **Kotlin (JVM)**  
   Сборка: `kotlinc -cp gson.jar HistName.kt`  
   Запуск: `kotlin -cp .;gson.jar HistNameKt --culture medieval --gender female --count 3`

## Использование

Общие аргументы командной строки (везде, где поддерживается):

- `--culture <культура>` – историческая культура: `viking`, `roman`, `medieval`, `greek`, `slavic`, `celtic` (по умолчанию `viking`).
- `--gender <пол>` – `male` или `female` (по умолчанию `male`).
- `--count <число>` – количество генерируемых имён (по умолчанию 1).
- `--surname` – добавить фамилию/родовое имя.
- `--title` – добавить титул.
- `--seed <число>` – seed для воспроизводимости.
- `--output <файл>` – сохранить результат в файл (расширение определяет формат: `.txt`, `.json`, `.csv`).
- `--format` – принудительно указать формат вывода: `text` (по умолчанию), `json`, `csv`.
- `--help` – справка.

Пример (Python):
```bash
python hist_name.py --culture viking --gender male --count 5 --surname --title --seed 42 --output vikings.json
Пример вывода (text):

text
🏛️ Исторические имена (викинг, мужские):
1. Эйнар Кровавый Молот
2. Рагнар Бьёрнсон
3. Свен Скальд
4. Ульф Берсерк
5. Харальд Боевой Топор
Структура репозитория
text
/
├── README.md
├── hist_name.py
├── hist_name.js
├── hist_name.go
├── hist_name.rs
├── HistName.java
├── HistName.cs
├── hist_name.cpp
└── HistName.kt
Лицензия
MIT
