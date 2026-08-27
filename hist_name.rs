// hist_name.rs
use clap::{App, Arg};
use rand::prelude::*;
use rand::SeedableRng;
use rand::rngs::StdRng;
use serde_json;
use std::collections::HashMap;
use std::fs;
use std::io::Write;

lazy_static::lazy_static! {
    static ref NAMES_DATA: HashMap<String, HashMap<String, Vec<String>>> = {
        let mut data = HashMap::new();

        let mut viking = HashMap::new();
        viking.insert("male".to_string(), vec!["Эйнар","Рагнар","Бьёрн","Свен","Ульф","Харальд","Олаф","Эрик","Торгейр","Гуннар","Сигурд","Хакон","Кнуд","Орм","Кетиль","Асбьёрн","Торстейн","Эгиль","Гудмунд","Хельги"].iter().map(|s| s.to_string()).collect());
        viking.insert("female".to_string(), vec!["Астрид","Брюнхильд","Гудрун","Сигриди","Гунхильд","Хельга","Ингеборг","Тордис","Тора","Альвильд","Герда","Фрейдис","Раннвейг","Сигрид","Эрна","Грета","Кристин","Хильда","Руна","Сванхильд"].iter().map(|s| s.to_string()).collect());
        viking.insert("surnames".to_string(), vec!["Бьёрнсон","Эйнарсон","Харальдсон","Свенсон","Рагнарсон","Ульфсон","Олафсон","Гуннарсон","Сигурдсон","Торгерсон","Кетильсон","Асбьёрнсон","Торстейнсон","Эгильсон","Гудмундсон"].iter().map(|s| s.to_string()).collect());
        viking.insert("titles".to_string(), vec!["Берсерк","Скальд","Молот","Боевой Топор","Кровавый","Северный","Грозный","Мудрый"].iter().map(|s| s.to_string()).collect());
        data.insert("viking".to_string(), viking);

        let mut roman = HashMap::new();
        roman.insert("male".to_string(), vec!["Гай","Луций","Марк","Квинт","Публий","Секст","Тит","Авл","Децим","Гней","Сервий","Нумерий","Август","Тиберий","Клавдий","Нерон","Траян","Адриан","Антоний","Юлий"].iter().map(|s| s.to_string()).collect());
        roman.insert("female".to_string(), vec!["Юлия","Ливия","Валерия","Клавдия","Цецилия","Теренция","Октавия","Друзилла","Агриппина","Мессалина","Антония","Корнелия","Помпея","Вителлия","Германика","Фавстина","Луцилла","Криспина","Сабина","Вероника"].iter().map(|s| s.to_string()).collect());
        roman.insert("surnames".to_string(), vec!["Цезарь","Август","Нерон","Клавдий","Тиберий","Траян","Адриан","Антоний","Юлий","Германик","Брут","Сципион","Катон","Цицерон","Сенека","Аврелий","Флавий","Константин"].iter().map(|s| s.to_string()).collect());
        roman.insert("titles".to_string(), vec!["Император","Консул","Сенатор","Легат","Претор","Эдил","Цензор","Диктатор"].iter().map(|s| s.to_string()).collect());
        data.insert("roman".to_string(), roman);

        let mut medieval = HashMap::new();
        medieval.insert("male".to_string(), vec!["Уильям","Генри","Ричард","Роберт","Эдуард","Джон","Томас","Джеффри","Роджер","Хью","Саймон","Уолтер","Гилберт","Реджинальд","Балдуин","Алан","Брайан","Филип","Джеймс","Дэвид"].iter().map(|s| s.to_string()).collect());
        medieval.insert("female".to_string(), vec!["Элеонора","Матильда","Алиенора","Агнес","Изабелла","Беатрис","Сибилла","Маргарет","Кэтрин","Джоан","Алиса","Сесилия","Ида","Мария","Клеменция","Амелия","Филиппа","Гвендолин","Аделаида","Энн"].iter().map(|s| s.to_string()).collect());
        medieval.insert("surnames".to_string(), vec!["Нормандский","Анжуйский","Ланкастерский","Йоркский","Плантагенет","Уэссекский","Глостерский","Кентский","Суффолкский","Ричмондский","Пемброк","Мортимер","Грей","Стюарт","Говард"].iter().map(|s| s.to_string()).collect());
        medieval.insert("titles".to_string(), vec!["Король","Герцог","Граф","Барон","Рыцарь","Лорд","Епископ","Маркиз"].iter().map(|s| s.to_string()).collect());
        data.insert("medieval".to_string(), medieval);

        let mut greek = HashMap::new();
        greek.insert("male".to_string(), vec!["Александр","Аристотель","Демокрит","Эпикур","Гераклит","Платон","Сократ","Пифагор","Гомер","Софокл","Еврипид","Эсхил","Перикл","Леонид","Фемистокл","Агамемнон","Ахилл","Одиссей","Гектор","Тесей"].iter().map(|s| s.to_string()).collect());
        greek.insert("female".to_string(), vec!["Афина","Гера","Афродита","Артемида","Деметра","Персефона","Гестия","Ника","Ирида","Афродита","Кассандра","Андромеда","Елена","Пенелопа","Антигона","Электра","Ифигения","Медея","Сапфо","Гипполита"].iter().map(|s| s.to_string()).collect());
        greek.insert("surnames".to_string(), vec!["Афинский","Спартанский","Коринфский","Фиванский","Македонский","Фессалийский","Критский","Родосский","Милетский","Эфесский"].iter().map(|s| s.to_string()).collect());
        greek.insert("titles".to_string(), vec!["Царь","Тиран","Архонт","Стратег","Философ","Поэт","Воин","Герой"].iter().map(|s| s.to_string()).collect());
        data.insert("greek".to_string(), greek);

        let mut slavic = HashMap::new();
        slavic.insert("male".to_string(), vec!["Владимир","Святослав","Ярослав","Всеволод","Изяслав","Мстислав","Олег","Игорь","Святополк","Вячеслав","Ярополк","Глеб","Борис","Андрей","Александр","Дмитрий","Михаил","Пётр","Иван","Василий"].iter().map(|s| s.to_string()).collect());
        slavic.insert("female".to_string(), vec!["Ольга","Ярославна","Владислава","Людмила","Мирослава","Светлана","Евдокия","Борислава","Велеслава","Добромира","Вера","Надежда","Любовь","Мария","Анна","Екатерина","Анастасия","Татьяна","Елена","Ирина"].iter().map(|s| s.to_string()).collect());
        slavic.insert("surnames".to_string(), vec!["Рюрикович","Владимирович","Святославич","Ярославич","Всеволодович","Изяславич","Мстиславич","Олегович","Игоревич","Святополкович"].iter().map(|s| s.to_string()).collect());
        slavic.insert("titles".to_string(), vec!["Князь","Воевода","Боярин","Дружинник","Мудрый","Грозный","Святой","Великий"].iter().map(|s| s.to_string()).collect());
        data.insert("slavic".to_string(), slavic);

        let mut celtic = HashMap::new();
        celtic.insert("male".to_string(), vec!["Айдан","Брендан","Коннор","Деклан","Эйлин","Финн","Гэвин","Иан","Киран","Лайам","Мэлколм","Ниалл","Она","Патрик","Рори","Шон","Тиадг","Улисс","Эмон","Брайан"].iter().map(|s| s.to_string()).collect());
        celtic.insert("female".to_string(), vec!["Айслин","Бриджит","Кэтлин","Диана","Эйлиш","Финола","Грэйн","Иона","Кира","Лианна","Майв","Ниам","Она","Рона","Сиана","Тара","Уна","Фиона","Сиобан","Морриган"].iter().map(|s| s.to_string()).collect());
        celtic.insert("surnames".to_string(), vec!["МакКауд","О'Брайен","О'Салливан","МакКарти","О'Доннелл","МакДонах","О'Нейл","МакГрат","О'Ши","МакМахон","О'Коннор","МакДауэлл","О'Киф","МакГиннис","О'Флаэрти"].iter().map(|s| s.to_string()).collect());
        celtic.insert("titles".to_string(), vec!["Король","Вождь","Друид","Воин","Мудрец","Певец","Охотник","Мастер"].iter().map(|s| s.to_string()).collect());
        data.insert("celtic".to_string(), celtic);

        data
    };
}

struct Generator {
    culture: String,
    gender: String,
    count: usize,
    surname: bool,
    title: bool,
    rng: StdRng,
}

impl Generator {
    fn new(culture: &str, gender: &str, count: usize, surname: bool, title: bool, seed: u64) -> Self {
        let rng = if seed != 0 { StdRng::seed_from_u64(seed) } else { StdRng::from_entropy() };
        Generator {
            culture: culture.to_string(),
            gender: gender.to_string(),
            count,
            surname,
            title,
            rng,
        }
    }

    fn generate_name(&mut self) -> String {
        let culture_data = NAMES_DATA.get(&self.culture).unwrap_or_else(|| NAMES_DATA.get("viking").unwrap());
        let names = culture_data.get(&self.gender).unwrap_or_else(|| culture_data.get("male").unwrap());
        let mut name = names[self.rng.gen_range(0..names.len())].clone();
        if self.surname {
            if let Some(surnames) = culture_data.get("surnames") {
                name.push(' ');
                name.push_str(&surnames[self.rng.gen_range(0..surnames.len())]);
            }
        }
        if self.title {
            if let Some(titles) = culture_data.get("titles") {
                name.push(' ');
                name.push_str(&titles[self.rng.gen_range(0..titles.len())]);
            }
        }
        name
    }

    fn generate(&mut self) -> Vec<String> {
        (0..self.count).map(|_| self.generate_name()).collect()
    }
}

fn print_names(names: &[String], culture: &str, gender: &str, color: bool) {
    let labels = vec![
        ("viking", "викинг"), ("roman", "римское"), ("medieval", "средневековое"),
        ("greek", "греческое"), ("slavic", "славянское"), ("celtic", "кельтское")
    ];
    let label = labels.iter().find(|(k,_)| *k == culture).map(|(_,v)| *v).unwrap_or(culture);
    let gender_label = if gender == "male" { "мужские" } else { "женские" };
    if color {
        println!("\x1b[36m🏛️ Исторические имена ({}, {}):\x1b[0m", label, gender_label);
        for (i, n) in names.iter().enumerate() {
            println!("{}. \x1b[32m{}\x1b[0m", i+1, n);
        }
    } else {
        println!("Исторические имена ({}, {}):", label, gender_label);
        for (i, n) in names.iter().enumerate() {
            println!("{}. {}", i+1, n);
        }
    }
}

fn export_json(names: &[String], culture: &str, gender: &str, filename: &str) {
    let data = serde_json::json!({
        "culture": culture,
        "gender": gender,
        "names": names
    });
    let json = serde_json::to_string_pretty(&data).unwrap();
    fs::write(filename, json).unwrap();
}

fn export_csv(names: &[String], filename: &str) {
    let mut csv = String::from("name\n");
    for n in names {
        csv.push_str(n);
        csv.push('\n');
    }
    fs::write(filename, csv).unwrap();
}

fn export_text(names: &[String], filename: &str) {
    fs::write(filename, names.join("\n")).unwrap();
}

fn main() {
    let matches = App::new("Historical Name Generator")
        .arg(Arg::with_name("culture").long("culture").takes_value(true).default_value("viking"))
        .arg(Arg::with_name("gender").long("gender").takes_value(true).default_value("male"))
        .arg(Arg::with_name("count").long("count").takes_value(true).default_value("1"))
        .arg(Arg::with_name("surname").long("surname"))
        .arg(Arg::with_name("title").long("title"))
        .arg(Arg::with_name("seed").long("seed").takes_value(true))
        .arg(Arg::with_name("output").long("output").takes_value(true))
        .arg(Arg::with_name("format").long("format").takes_value(true))
        .get_matches();

    let culture = matches.value_of("culture").unwrap();
    let gender = matches.value_of("gender").unwrap();
    let count: usize = matches.value_of("count").unwrap().parse().unwrap();
    let surname = matches.is_present("surname");
    let title = matches.is_present("title");
    let seed: u64 = matches.value_of("seed").unwrap_or("0").parse().unwrap();
    let output = matches.value_of("output");
    let format = matches.value_of("format");

    let mut gen = Generator::new(culture, gender, count, surname, title, seed);
    let names = gen.generate();

    if let Some(out) = output {
        let fmt = if let Some(f) = format { f } else {
            let ext = out.split('.').last().unwrap_or("txt");
            if ext == "json" { "json" } else if ext == "csv" { "csv" } else { "text" }
        };
        match fmt {
            "json" => export_json(&names, culture, gender, out),
            "csv" => export_csv(&names, out),
            _ => export_text(&names, out),
        }
        println!("Результат сохранён в {}", out);
    } else {
        let color = atty::is(atty::Stream::Stdout);
        print_names(&names, culture, gender, color);
    }
}
