// hist_name.go
package main

import (
	"encoding/csv"
	"encoding/json"
	"flag"
	"fmt"
	"math/rand"
	"os"
	"strings"
	"time"
)

var namesData = map[string]map[string][]string{
	"viking": {
		"male":     {"Эйнар", "Рагнар", "Бьёрн", "Свен", "Ульф", "Харальд", "Олаф", "Эрик", "Торгейр", "Гуннар", "Сигурд", "Хакон", "Кнуд", "Орм", "Кетиль", "Асбьёрн", "Торстейн", "Эгиль", "Гудмунд", "Хельги"},
		"female":   {"Астрид", "Брюнхильд", "Гудрун", "Сигриди", "Гунхильд", "Хельга", "Ингеборг", "Тордис", "Тора", "Альвильд", "Герда", "Фрейдис", "Раннвейг", "Сигрид", "Эрна", "Грета", "Кристин", "Хильда", "Руна", "Сванхильд"},
		"surnames": {"Бьёрнсон", "Эйнарсон", "Харальдсон", "Свенсон", "Рагнарсон", "Ульфсон", "Олафсон", "Гуннарсон", "Сигурдсон", "Торгерсон", "Кетильсон", "Асбьёрнсон", "Торстейнсон", "Эгильсон", "Гудмундсон"},
		"titles":   {"Берсерк", "Скальд", "Молот", "Боевой Топор", "Кровавый", "Северный", "Грозный", "Мудрый"},
	},
	"roman": {
		"male":     {"Гай", "Луций", "Марк", "Квинт", "Публий", "Секст", "Тит", "Авл", "Децим", "Гней", "Сервий", "Нумерий", "Август", "Тиберий", "Клавдий", "Нерон", "Траян", "Адриан", "Антоний", "Юлий"},
		"female":   {"Юлия", "Ливия", "Валерия", "Клавдия", "Цецилия", "Теренция", "Октавия", "Друзилла", "Агриппина", "Мессалина", "Антония", "Корнелия", "Помпея", "Вителлия", "Германика", "Фавстина", "Луцилла", "Криспина", "Сабина", "Вероника"},
		"surnames": {"Цезарь", "Август", "Нерон", "Клавдий", "Тиберий", "Траян", "Адриан", "Антоний", "Юлий", "Германик", "Брут", "Сципион", "Катон", "Цицерон", "Сенека", "Аврелий", "Флавий", "Константин"},
		"titles":   {"Император", "Консул", "Сенатор", "Легат", "Претор", "Эдил", "Цензор", "Диктатор"},
	},
	"medieval": {
		"male":     {"Уильям", "Генри", "Ричард", "Роберт", "Эдуард", "Джон", "Томас", "Джеффри", "Роджер", "Хью", "Саймон", "Уолтер", "Гилберт", "Реджинальд", "Балдуин", "Алан", "Брайан", "Филип", "Джеймс", "Дэвид"},
		"female":   {"Элеонора", "Матильда", "Алиенора", "Агнес", "Изабелла", "Беатрис", "Сибилла", "Маргарет", "Кэтрин", "Джоан", "Алиса", "Сесилия", "Ида", "Мария", "Клеменция", "Амелия", "Филиппа", "Гвендолин", "Аделаида", "Энн"},
		"surnames": {"Нормандский", "Анжуйский", "Ланкастерский", "Йоркский", "Плантагенет", "Уэссекский", "Глостерский", "Кентский", "Суффолкский", "Ричмондский", "Пемброк", "Мортимер", "Грей", "Стюарт", "Говард"},
		"titles":   {"Король", "Герцог", "Граф", "Барон", "Рыцарь", "Лорд", "Епископ", "Маркиз"},
	},
	"greek": {
		"male":     {"Александр", "Аристотель", "Демокрит", "Эпикур", "Гераклит", "Платон", "Сократ", "Пифагор", "Гомер", "Софокл", "Еврипид", "Эсхил", "Перикл", "Леонид", "Фемистокл", "Агамемнон", "Ахилл", "Одиссей", "Гектор", "Тесей"},
		"female":   {"Афина", "Гера", "Афродита", "Артемида", "Деметра", "Персефона", "Гестия", "Ника", "Ирида", "Афродита", "Кассандра", "Андромеда", "Елена", "Пенелопа", "Антигона", "Электра", "Ифигения", "Медея", "Сапфо", "Гипполита"},
		"surnames": {"Афинский", "Спартанский", "Коринфский", "Фиванский", "Македонский", "Фессалийский", "Критский", "Родосский", "Милетский", "Эфесский"},
		"titles":   {"Царь", "Тиран", "Архонт", "Стратег", "Философ", "Поэт", "Воин", "Герой"},
	},
	"slavic": {
		"male":     {"Владимир", "Святослав", "Ярослав", "Всеволод", "Изяслав", "Мстислав", "Олег", "Игорь", "Святополк", "Вячеслав", "Ярополк", "Глеб", "Борис", "Андрей", "Александр", "Дмитрий", "Михаил", "Пётр", "Иван", "Василий"},
		"female":   {"Ольга", "Ярославна", "Владислава", "Людмила", "Мирослава", "Светлана", "Евдокия", "Борислава", "Велеслава", "Добромира", "Вера", "Надежда", "Любовь", "Мария", "Анна", "Екатерина", "Анастасия", "Татьяна", "Елена", "Ирина"},
		"surnames": {"Рюрикович", "Владимирович", "Святославич", "Ярославич", "Всеволодович", "Изяславич", "Мстиславич", "Олегович", "Игоревич", "Святополкович"},
		"titles":   {"Князь", "Воевода", "Боярин", "Дружинник", "Мудрый", "Грозный", "Святой", "Великий"},
	},
	"celtic": {
		"male":     {"Айдан", "Брендан", "Коннор", "Деклан", "Эйлин", "Финн", "Гэвин", "Иан", "Киран", "Лайам", "Мэлколм", "Ниалл", "Она", "Патрик", "Рори", "Шон", "Тиадг", "Улисс", "Эмон", "Брайан"},
		"female":   {"Айслин", "Бриджит", "Кэтлин", "Диана", "Эйлиш", "Финола", "Грэйн", "Иона", "Кира", "Лианна", "Майв", "Ниам", "Она", "Рона", "Сиана", "Тара", "Уна", "Фиона", "Сиобан", "Морриган"},
		"surnames": {"МакКауд", "О'Брайен", "О'Салливан", "МакКарти", "О'Доннелл", "МакДонах", "О'Нейл", "МакГрат", "О'Ши", "МакМахон", "О'Коннор", "МакДауэлл", "О'Киф", "МакГиннис", "О'Флаэрти"},
		"titles":   {"Король", "Вождь", "Друид", "Воин", "Мудрец", "Певец", "Охотник", "Мастер"},
	},
}

type Generator struct {
	culture  string
	gender   string
	count    int
	surname  bool
	title    bool
	rng      *rand.Rand
	cultureData map[string][]string
}

func NewGenerator(culture, gender string, count int, surname, title bool, seed int64) *Generator {
	var rng *rand.Rand
	if seed != 0 {
		rng = rand.New(rand.NewSource(seed))
	} else {
		rng = rand.New(rand.NewSource(time.Now().UnixNano()))
	}
	data := namesData[culture]
	if data == nil {
		data = namesData["viking"]
	}
	return &Generator{
		culture:     culture,
		gender:      gender,
		count:       count,
		surname:     surname,
		title:       title,
		rng:         rng,
		cultureData: data,
	}
}

func (g *Generator) generateName() string {
	names := g.cultureData[g.gender]
	if names == nil {
		names = g.cultureData["male"]
	}
	name := names[g.rng.Intn(len(names))]
	if g.surname && g.cultureData["surnames"] != nil {
		snames := g.cultureData["surnames"]
		name += " " + snames[g.rng.Intn(len(snames))]
	}
	if g.title && g.cultureData["titles"] != nil {
		titles := g.cultureData["titles"]
		name += " " + titles[g.rng.Intn(len(titles))]
	}
	return name
}

func (g *Generator) generate() []string {
	names := make([]string, g.count)
	for i := 0; i < g.count; i++ {
		names[i] = g.generateName()
	}
	return names
}

func printNames(names []string, culture, gender string, color bool) {
	cultureLabels := map[string]string{
		"viking": "викинг", "roman": "римское", "medieval": "средневековое",
		"greek": "греческое", "slavic": "славянское", "celtic": "кельтское",
	}
	label := cultureLabels[culture]
	if label == "" {
		label = culture
	}
	genderLabel := "мужские"
	if gender == "female" {
		genderLabel = "женские"
	}
	if color {
		fmt.Printf("\033[36m🏛️ Исторические имена (%s, %s):\033[0m\n", label, genderLabel)
		for i, n := range names {
			fmt.Printf("%d. \033[32m%s\033[0m\n", i+1, n)
		}
	} else {
		fmt.Printf("Исторические имена (%s, %s):\n", label, genderLabel)
		for i, n := range names {
			fmt.Printf("%d. %s\n", i+1, n)
		}
	}
}

func exportJSON(names []string, culture, gender, filename string) error {
	data := map[string]interface{}{"culture": culture, "gender": gender, "names": names}
	jsonData, err := json.MarshalIndent(data, "", "  ")
	if err != nil {
		return err
	}
	return os.WriteFile(filename, jsonData, 0644)
}

func exportCSV(names []string, filename string) error {
	f, err := os.Create(filename)
	if err != nil {
		return err
	}
	defer f.Close()
	w := csv.NewWriter(f)
	defer w.Flush()
	w.Write([]string{"name"})
	for _, n := range names {
		w.Write([]string{n})
	}
	return nil
}

func exportText(names []string, filename string) error {
	return os.WriteFile(filename, []byte(strings.Join(names, "\n")), 0644)
}

func main() {
	var (
		culture string
		gender  string
		count   int
		surname bool
		title   bool
		seed    int64
		output  string
		format  string
	)
	flag.StringVar(&culture, "culture", "viking", "culture: viking, roman, medieval, greek, slavic, celtic")
	flag.StringVar(&gender, "gender", "male", "gender: male, female")
	flag.IntVar(&count, "count", 1, "Number of names")
	flag.BoolVar(&surname, "surname", false, "Add surname")
	flag.BoolVar(&title, "title", false, "Add title")
	flag.Int64Var(&seed, "seed", 0, "Seed")
	flag.StringVar(&output, "output", "", "Output file")
	flag.StringVar(&format, "format", "", "Format: text, json, csv")
	flag.Parse()

	gen := NewGenerator(culture, gender, count, surname, title, seed)
	names := gen.generate()

	if output != "" {
		var fmtType string
		if format != "" {
			fmtType = format
		} else {
			ext := output[strings.LastIndex(output, ".")+1:]
			if ext == "json" {
				fmtType = "json"
			} else if ext == "csv" {
				fmtType = "csv"
			} else {
				fmtType = "text"
			}
		}
		var err error
		switch fmtType {
		case "json":
			err = exportJSON(names, culture, gender, output)
		case "csv":
			err = exportCSV(names, output)
		default:
			err = exportText(names, output)
		}
		if err != nil {
			fmt.Fprintf(os.Stderr, "Ошибка сохранения: %v\n", err)
			os.Exit(1)
		}
		fmt.Printf("Результат сохранён в %s\n", output)
	} else {
		color := isTerminal()
		printNames(names, culture, gender, color)
	}
}

func isTerminal() bool {
	stat, _ := os.Stdout.Stat()
	return (stat.Mode() & os.ModeCharDevice) != 0
}
