// hist_name.cpp
#include <iostream>
#include <string>
#include <vector>
#include <map>
#include <random>
#include <fstream>
#include <sstream>
#include <algorithm>
#include <chrono>
#include <cstring>

using namespace std;

const map<string, map<string, vector<string>>> NAMES = {
    {"viking", {
        {"male", {"Эйнар","Рагнар","Бьёрн","Свен","Ульф","Харальд","Олаф","Эрик","Торгейр","Гуннар","Сигурд","Хакон","Кнуд","Орм","Кетиль","Асбьёрн","Торстейн","Эгиль","Гудмунд","Хельги"}},
        {"female", {"Астрид","Брюнхильд","Гудрун","Сигриди","Гунхильд","Хельга","Ингеборг","Тордис","Тора","Альвильд","Герда","Фрейдис","Раннвейг","Сигрид","Эрна","Грета","Кристин","Хильда","Руна","Сванхильд"}},
        {"surnames", {"Бьёрнсон","Эйнарсон","Харальдсон","Свенсон","Рагнарсон","Ульфсон","Олафсон","Гуннарсон","Сигурдсон","Торгерсон","Кетильсон","Асбьёрнсон","Торстейнсон","Эгильсон","Гудмундсон"}},
        {"titles", {"Берсерк","Скальд","Молот","Боевой Топор","Кровавый","Северный","Грозный","Мудрый"}}
    }},
    {"roman", {
        {"male", {"Гай","Луций","Марк","Квинт","Публий","Секст","Тит","Авл","Децим","Гней","Сервий","Нумерий","Август","Тиберий","Клавдий","Нерон","Траян","Адриан","Антоний","Юлий"}},
        {"female", {"Юлия","Ливия","Валерия","Клавдия","Цецилия","Теренция","Октавия","Друзилла","Агриппина","Мессалина","Антония","Корнелия","Помпея","Вителлия","Германика","Фавстина","Луцилла","Криспина","Сабина","Вероника"}},
        {"surnames", {"Цезарь","Август","Нерон","Клавдий","Тиберий","Траян","Адриан","Антоний","Юлий","Германик","Брут","Сципион","Катон","Цицерон","Сенека","Аврелий","Флавий","Константин"}},
        {"titles", {"Император","Консул","Сенатор","Легат","Претор","Эдил","Цензор","Диктатор"}}
    }},
    {"medieval", {
        {"male", {"Уильям","Генри","Ричард","Роберт","Эдуард","Джон","Томас","Джеффри","Роджер","Хью","Саймон","Уолтер","Гилберт","Реджинальд","Балдуин","Алан","Брайан","Филип","Джеймс","Дэвид"}},
        {"female", {"Элеонора","Матильда","Алиенора","Агнес","Изабелла","Беатрис","Сибилла","Маргарет","Кэтрин","Джоан","Алиса","Сесилия","Ида","Мария","Клеменция","Амелия","Филиппа","Гвендолин","Аделаида","Энн"}},
        {"surnames", {"Нормандский","Анжуйский","Ланкастерский","Йоркский","Плантагенет","Уэссекский","Глостерский","Кентский","Суффолкский","Ричмондский","Пемброк","Мортимер","Грей","Стюарт","Говард"}},
        {"titles", {"Король","Герцог","Граф","Барон","Рыцарь","Лорд","Епископ","Маркиз"}}
    }},
    {"greek", {
        {"male", {"Александр","Аристотель","Демокрит","Эпикур","Гераклит","Платон","Сократ","Пифагор","Гомер","Софокл","Еврипид","Эсхил","Перикл","Леонид","Фемистокл","Агамемнон","Ахилл","Одиссей","Гектор","Тесей"}},
        {"female", {"Афина","Гера","Афродита","Артемида","Деметра","Персефона","Гестия","Ника","Ирида","Афродита","Кассандра","Андромеда","Елена","Пенелопа","Антигона","Электра","Ифигения","Медея","Сапфо","Гипполита"}},
        {"surnames", {"Афинский","Спартанский","Коринфский","Фиванский","Македонский","Фессалийский","Критский","Родосский","Милетский","Эфесский"}},
        {"titles", {"Царь","Тиран","Архонт","Стратег","Философ","Поэт","Воин","Герой"}}
    }},
    {"slavic", {
        {"male", {"Владимир","Святослав","Ярослав","Всеволод","Изяслав","Мстислав","Олег","Игорь","Святополк","Вячеслав","Ярополк","Глеб","Борис","Андрей","Александр","Дмитрий","Михаил","Пётр","Иван","Василий"}},
        {"female", {"Ольга","Ярославна","Владислава","Людмила","Мирослава","Светлана","Евдокия","Борислава","Велеслава","Добромира","Вера","Надежда","Любовь","Мария","Анна","Екатерина","Анастасия","Татьяна","Елена","Ирина"}},
        {"surnames", {"Рюрикович","Владимирович","Святославич","Ярославич","Всеволодович","Изяславич","Мстиславич","Олегович","Игоревич","Святополкович"}},
        {"titles", {"Князь","Воевода","Боярин","Дружинник","Мудрый","Грозный","Святой","Великий"}}
    }},
    {"celtic", {
        {"male", {"Айдан","Брендан","Коннор","Деклан","Эйлин","Финн","Гэвин","Иан","Киран","Лайам","Мэлколм","Ниалл","Она","Патрик","Рори","Шон","Тиадг","Улисс","Эмон","Брайан"}},
        {"female", {"Айслин","Бриджит","Кэтлин","Диана","Эйлиш","Финола","Грэйн","Иона","Кира","Лианна","Майв","Ниам","Она","Рона","Сиана","Тара","Уна","Фиона","Сиобан","Морриган"}},
        {"surnames", {"МакКауд","О'Брайен","О'Салливан","МакКарти","О'Доннелл","МакДонах","О'Нейл","МакГрат","О'Ши","МакМахон","О'Коннор","МакДауэлл","О'Киф","МакГиннис","О'Флаэрти"}},
        {"titles", {"Король","Вождь","Друид","Воин","Мудрец","Певец","Охотник","Мастер"}}
    }}
};

class Generator {
private:
    string culture, gender;
    int count;
    bool surname, title;
    mt19937 rng;
    map<string, vector<string>> cultureData;

public:
    Generator(const string& cult, const string& gend, int cnt, bool sur, bool tit, unsigned seed)
        : culture(cult), gender(gend), count(cnt), surname(sur), title(tit) {
        if (seed != 0) rng = mt19937(seed);
        else rng = mt19937(chrono::steady_clock::now().time_since_epoch().count());
        auto it = NAMES.find(culture);
        if (it != NAMES.end()) cultureData = it->second;
        else cultureData = NAMES.at("viking");
    }

    string generateName() {
        auto namesIt = cultureData.find(gender);
        if (namesIt == cultureData.end()) namesIt = cultureData.find("male");
        const vector<string>& names = namesIt->second;
        uniform_int_distribution<size_t> dist(0, names.size()-1);
        string name = names[dist(rng)];
        if (surname) {
            auto surIt = cultureData.find("surnames");
            if (surIt != cultureData.end()) {
                const vector<string>& snames = surIt->second;
                uniform_int_distribution<size_t> sdist(0, snames.size()-1);
                name += " " + snames[sdist(rng)];
            }
        }
        if (title) {
            auto titIt = cultureData.find("titles");
            if (titIt != cultureData.end()) {
                const vector<string>& titles = titIt->second;
                uniform_int_distribution<size_t> tdist(0, titles.size()-1);
                name += " " + titles[tdist(rng)];
            }
        }
        return name;
    }

    vector<string> generate() {
        vector<string> result;
        for (int i = 0; i < count; ++i) {
            result.push_back(generateName());
        }
        return result;
    }
};

void printNames(const vector<string>& names, const string& culture, const string& gender, bool color) {
    map<string, string> labels = {
        {"viking","викинг"}, {"roman","римское"}, {"medieval","средневековое"},
        {"greek","греческое"}, {"slavic","славянское"}, {"celtic","кельтское"}
    };
    string label = labels.count(culture) ? labels.at(culture) : culture;
    string genderLabel = gender == "male" ? "мужские" : "женские";
    if (color) {
        cout << "\033[36m🏛️ Исторические имена (" << label << ", " << genderLabel << "):\033[0m" << endl;
        for (size_t i = 0; i < names.size(); ++i) {
            cout << (i+1) << ". \033[32m" << names[i] << "\033[0m" << endl;
        }
    } else {
        cout << "Исторические имена (" << label << ", " << genderLabel << "):" << endl;
        for (size_t i = 0; i < names.size(); ++i) {
            cout << (i+1) << ". " << names[i] << endl;
        }
    }
}

void exportJSON(const vector<string>& names, const string& culture, const string& gender, const string& filename) {
    ofstream ofs(filename);
    ofs << "{";
    ofs << "\"culture\":\"" << culture << "\",";
    ofs << "\"gender\":\"" << gender << "\",";
    ofs << "\"names\":[";
    for (size_t i = 0; i < names.size(); ++i) {
        if (i > 0) ofs << ",";
        ofs << "\"" << names[i] << "\"";
    }
    ofs << "]}";
}

void exportCSV(const vector<string>& names, const string& filename) {
    ofstream ofs(filename);
    ofs << "name\n";
    for (const auto& n : names) ofs << n << "\n";
}

void exportText(const vector<string>& names, const string& filename) {
    ofstream ofs(filename);
    for (size_t i = 0; i < names.size(); ++i) {
        if (i > 0) ofs << "\n";
        ofs << names[i];
    }
}

int main(int argc, char* argv[]) {
    string culture = "viking", gender = "male", output, format;
    int count = 1;
    bool surname = false, title = false;
    unsigned seed = 0;

    for (int i = 1; i < argc; ++i) {
        string arg = argv[i];
        if (arg == "--culture" && i+1 < argc) culture = argv[++i];
        else if (arg == "--gender" && i+1 < argc) gender = argv[++i];
        else if (arg == "--count" && i+1 < argc) count = stoi(argv[++i]);
        else if (arg == "--surname") surname = true;
        else if (arg == "--title") title = true;
        else if (arg == "--seed" && i+1 < argc) seed = stoul(argv[++i]);
        else if (arg == "--output" && i+1 < argc) output = argv[++i];
        else if (arg == "--format" && i+1 < argc) format = argv[++i];
    }

    Generator gen(culture, gender, count, surname, title, seed);
    vector<string> names = gen.generate();

    if (!output.empty()) {
        string fmt = format;
        if (fmt.empty()) {
            size_t dot = output.find_last_of('.');
            if (dot != string::npos) {
                string ext = output.substr(dot+1);
                if (ext == "json") fmt = "json";
                else if (ext == "csv") fmt = "csv";
                else fmt = "text";
            } else fmt = "text";
        }
        if (fmt == "json") exportJSON(names, culture, gender, output);
        else if (fmt == "csv") exportCSV(names, output);
        else exportText(names, output);
        cout << "Результат сохранён в " << output << endl;
    } else {
        bool color = isatty(fileno(stdout));
        printNames(names, culture, gender, color);
    }
    return 0;
}
