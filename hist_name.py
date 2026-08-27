
#!/usr/bin/env python3
# hist_name.py
import argparse
import json
import csv
import random
import sys
from colorama import init, Fore, Style

init(autoreset=True)

# Базы данных имён по культурам и полам
NAMES = {
    "viking": {
        "male": [
            "Эйнар", "Рагнар", "Бьёрн", "Свен", "Ульф", "Харальд", "Олаф", "Эрик",
            "Торгейр", "Гуннар", "Сигурд", "Хакон", "Кнуд", "Орм", "Кетиль",
            "Асбьёрн", "Торстейн", "Эгиль", "Гудмунд", "Хельги"
        ],
        "female": [
            "Астрид", "Брюнхильд", "Гудрун", "Сигриди", "Гунхильд", "Хельга",
            "Ингеборг", "Тордис", "Тора", "Альвильд", "Герда", "Фрейдис",
            "Раннвейг", "Сигрид", "Эрна", "Грета", "Кристин", "Хильда", "Руна", "Сванхильд"
        ],
        "surnames": [
            "Бьёрнсон", "Эйнарсон", "Харальдсон", "Свенсон", "Рагнарсон",
            "Ульфсон", "Олафсон", "Гуннарсон", "Сигурдсон", "Торгерсон",
            "Кетильсон", "Асбьёрнсон", "Торстейнсон", "Эгильсон", "Гудмундсон"
        ],
        "titles": ["Берсерк", "Скальд", "Молот", "Боевой Топор", "Кровавый", "Северный", "Грозный", "Мудрый"]
    },
    "roman": {
        "male": [
            "Гай", "Луций", "Марк", "Квинт", "Публий", "Секст", "Тит", "Авл",
            "Децим", "Гней", "Сервий", "Нумерий", "Август", "Тиберий", "Клавдий",
            "Нерон", "Траян", "Адриан", "Антоний", "Юлий"
        ],
        "female": [
            "Юлия", "Ливия", "Валерия", "Клавдия", "Цецилия", "Теренция",
            "Октавия", "Друзилла", "Агриппина", "Мессалина", "Антония", "Корнелия",
            "Помпея", "Вителлия", "Германика", "Фавстина", "Луцилла", "Криспина",
            "Сабина", "Вероника"
        ],
        "surnames": [
            "Цезарь", "Август", "Нерон", "Клавдий", "Тиберий", "Траян",
            "Адриан", "Антоний", "Юлий", "Германик", "Брут", "Сципион",
            "Катон", "Цицерон", "Сенека", "Аврелий", "Флавий", "Константин"
        ],
        "titles": ["Император", "Консул", "Сенатор", "Легат", "Претор", "Эдил", "Цензор", "Диктатор"]
    },
    "medieval": {
        "male": [
            "Уильям", "Генри", "Ричард", "Роберт", "Эдуард", "Джон", "Томас", "Джеффри",
            "Роджер", "Хью", "Саймон", "Уолтер", "Гилберт", "Реджинальд", "Балдуин",
            "Алан", "Брайан", "Филип", "Джеймс", "Дэвид"
        ],
        "female": [
            "Элеонора", "Матильда", "Алиенора", "Агнес", "Изабелла", "Беатрис",
            "Сибилла", "Маргарет", "Кэтрин", "Джоан", "Алиса", "Сесилия",
            "Ида", "Мария", "Клеменция", "Амелия", "Филиппа", "Гвендолин", "Аделаида", "Энн"
        ],
        "surnames": [
            "Нормандский", "Анжуйский", "Ланкастерский", "Йоркский", "Плантагенет",
            "Уэссекский", "Глостерский", "Кентский", "Суффолкский", "Ричмондский",
            "Пемброк", "Мортимер", "Грей", "Стюарт", "Говард"
        ],
        "titles": ["Король", "Герцог", "Граф", "Барон", "Рыцарь", "Лорд", "Епископ", "Маркиз"]
    },
    "greek": {
        "male": [
            "Александр", "Аристотель", "Демокрит", "Эпикур", "Гераклит", "Платон",
            "Сократ", "Пифагор", "Гомер", "Софокл", "Еврипид", "Эсхил",
            "Перикл", "Леонид", "Фемистокл", "Агамемнон", "Ахилл", "Одиссей",
            "Гектор", "Тесей"
        ],
        "female": [
            "Афина", "Гера", "Афродита", "Артемида", "Деметра", "Персефона",
            "Гестия", "Ника", "Ирида", "Афродита", "Кассандра", "Андромеда",
            "Елена", "Пенелопа", "Антигона", "Электра", "Ифигения", "Медея",
            "Сапфо", "Гипполита"
        ],
        "surnames": [
            "Афинский", "Спартанский", "Коринфский", "Фиванский", "Македонский",
            "Фессалийский", "Критский", "Родосский", "Милетский", "Эфесский"
        ],
        "titles": ["Царь", "Тиран", "Архонт", "Стратег", "Философ", "Поэт", "Воин", "Герой"]
    },
    "slavic": {
        "male": [
            "Владимир", "Святослав", "Ярослав", "Всеволод", "Изяслав", "Мстислав",
            "Олег", "Игорь", "Святополк", "Вячеслав", "Ярополк", "Глеб",
            "Борис", "Андрей", "Александр", "Дмитрий", "Михаил", "Пётр",
            "Иван", "Василий"
        ],
        "female": [
            "Ольга", "Ярославна", "Владислава", "Людмила", "Мирослава", "Светлана",
            "Евдокия", "Борислава", "Велеслава", "Добромира", "Вера", "Надежда",
            "Любовь", "Мария", "Анна", "Екатерина", "Анастасия", "Татьяна",
            "Елена", "Ирина"
        ],
        "surnames": [
            "Рюрикович", "Владимирович", "Святославич", "Ярославич", "Всеволодович",
            "Изяславич", "Мстиславич", "Олегович", "Игоревич", "Святополкович"
        ],
        "titles": ["Князь", "Воевода", "Боярин", "Дружинник", "Мудрый", "Грозный", "Святой", "Великий"]
    },
    "celtic": {
        "male": [
            "Айдан", "Брендан", "Коннор", "Деклан", "Эйлин", "Финн", "Гэвин",
            "Иан", "Киран", "Лайам", "Мэлколм", "Ниалл", "Она", "Патрик",
            "Рори", "Шон", "Тиадг", "Улисс", "Эмон", "Брайан"
        ],
        "female": [
            "Айслин", "Бриджит", "Кэтлин", "Диана", "Эйлиш", "Финола", "Грэйн",
            "Иона", "Кира", "Лианна", "Майв", "Ниам", "Она", "Рона",
            "Сиана", "Тара", "Уна", "Фиона", "Сиобан", "Морриган"
        ],
        "surnames": [
            "МакКауд", "О'Брайен", "О'Салливан", "МакКарти", "О'Доннелл",
            "МакДонах", "О'Нейл", "МакГрат", "О'Ши", "МакМахон",
            "О'Коннор", "МакДауэлл", "О'Киф", "МакГиннис", "О'Флаэрти"
        ],
        "titles": ["Король", "Вождь", "Друид", "Воин", "Мудрец", "Певец", "Охотник", "Мастер"]
    }
}

class HistoricalNameGenerator:
    def __init__(self, culture="viking", gender="male", count=1,
                 surname=False, title=False, seed=None, color=False):
        self.culture = culture
        self.gender = gender
        self.count = count
        self.surname = surname
        self.title = title
        self.color = color and sys.stdout.isatty()
        if seed is not None:
            random.seed(seed)
        self.culture_data = NAMES.get(culture, NAMES["viking"])

    def generate_name(self):
        names = self.culture_data.get(self.gender, self.culture_data["male"])
        name = random.choice(names)
        result = name
        if self.surname:
            surnames = self.culture_data.get("surnames", [])
            if surnames:
                surname = random.choice(surnames)
                result += f" {surname}"
        if self.title:
            titles = self.culture_data.get("titles", [])
            if titles:
                title = random.choice(titles)
                result += f" {title}"
        return result

    def generate(self):
        return [self.generate_name() for _ in range(self.count)]

    def print_names(self, names):
        culture_names = {
            "viking": "викинг",
            "roman": "римское",
            "medieval": "средневековое",
            "greek": "греческое",
            "slavic": "славянское",
            "celtic": "кельтское"
        }
        culture_label = culture_names.get(self.culture, self.culture)
        gender_label = "мужские" if self.gender == "male" else "женские"
        if self.color:
            print(Fore.CYAN + f"🏛️ Исторические имена ({culture_label}, {gender_label}):")
            for i, name in enumerate(names, 1):
                print(Fore.GREEN + f"{i}. {name}" + Style.RESET_ALL)
        else:
            print(f"Исторические имена ({culture_label}, {gender_label}):")
            for i, name in enumerate(names, 1):
                print(f"{i}. {name}")

    def export_json(self, names, filename):
        with open(filename, 'w', encoding='utf-8') as f:
            json.dump({"culture": self.culture, "gender": self.gender, "names": names}, f, ensure_ascii=False, indent=2)

    def export_csv(self, names, filename):
        with open(filename, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(["name"])
            for name in names:
                writer.writerow([name])

    def export_text(self, names, filename):
        with open(filename, 'w', encoding='utf-8') as f:
            f.write("\n".join(names))

def main():
    parser = argparse.ArgumentParser(description="Генератор исторических имён")
    parser.add_argument("--culture", choices=["viking", "roman", "medieval", "greek", "slavic", "celtic"],
                        default="viking", help="Историческая культура")
    parser.add_argument("--gender", choices=["male", "female"], default="male", help="Пол")
    parser.add_argument("--count", type=int, default=1, help="Количество имён")
    parser.add_argument("--surname", action="store_true", help="Добавить фамилию")
    parser.add_argument("--title", action="store_true", help="Добавить титул")
    parser.add_argument("--seed", type=int, help="Seed для генерации")
    parser.add_argument("--output", help="Файл для сохранения")
    parser.add_argument("--format", choices=["text", "json", "csv"], help="Формат вывода")
    parser.add_argument("--color", action="store_true", help="Принудительный цветной вывод")
    args = parser.parse_args()

    gen = HistoricalNameGenerator(
        culture=args.culture,
        gender=args.gender,
        count=args.count,
        surname=args.surname,
        title=args.title,
        seed=args.seed,
        color=args.color
    )
    names = gen.generate()

    if args.output:
        fmt = args.format
        if fmt is None:
            ext = args.output.split('.')[-1].lower()
            if ext == "json":
                fmt = "json"
            elif ext == "csv":
                fmt = "csv"
            else:
                fmt = "text"
        if fmt == "json":
            gen.export_json(names, args.output)
        elif fmt == "csv":
            gen.export_csv(names, args.output)
        else:
            gen.export_text(names, args.output)
        print(f"Результат сохранён в {args.output}")
    else:
        gen.print_names(names)

if __name__ == "__main__":
    main()
