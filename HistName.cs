// HistName.cs
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace HistName
{
    class Program
    {
        static void Main(string[] args)
        {
            var opts = ParseArgs(args);
            var gen = new HistoricalNameGenerator(opts);
            var names = gen.Generate();
            if (opts.Output != null)
            {
                string fmt = opts.Format;
                if (string.IsNullOrEmpty(fmt))
                {
                    string ext = Path.GetExtension(opts.Output).ToLower().TrimStart('.');
                    fmt = ext == "json" ? "json" : ext == "csv" ? "csv" : "text";
                }
                switch (fmt)
                {
                    case "json": ExportJson(names, opts.Culture, opts.Gender, opts.Output); break;
                    case "csv": ExportCsv(names, opts.Output); break;
                    default: ExportText(names, opts.Output); break;
                }
                Console.WriteLine($"Результат сохранён в {opts.Output}");
            }
            else
            {
                PrintNames(names, opts.Culture, opts.Gender, !Console.IsOutputRedirected);
            }
        }

        static Options ParseArgs(string[] args)
        {
            var opts = new Options();
            for (int i = 0; i < args.Length; i++)
            {
                switch (args[i])
                {
                    case "--culture": opts.Culture = args[++i]; break;
                    case "--gender": opts.Gender = args[++i]; break;
                    case "--count": opts.Count = int.Parse(args[++i]); break;
                    case "--surname": opts.Surname = true; break;
                    case "--title": opts.Title = true; break;
                    case "--seed": opts.Seed = long.Parse(args[++i]); break;
                    case "--output": opts.Output = args[++i]; break;
                    case "--format": opts.Format = args[++i]; break;
                }
            }
            return opts;
        }

        class Options
        {
            public string Culture { get; set; } = "viking";
            public string Gender { get; set; } = "male";
            public int Count { get; set; } = 1;
            public bool Surname { get; set; } = false;
            public bool Title { get; set; } = false;
            public long? Seed { get; set; }
            public string Output { get; set; }
            public string Format { get; set; }
        }

        class HistoricalNameGenerator
        {
            private static readonly Dictionary<string, Dictionary<string, List<string>>> Names = new()
            {
                ["viking"] = new()
                {
                    ["male"] = new() { "Эйнар","Рагнар","Бьёрн","Свен","Ульф","Харальд","Олаф","Эрик","Торгейр","Гуннар","Сигурд","Хакон","Кнуд","Орм","Кетиль","Асбьёрн","Торстейн","Эгиль","Гудмунд","Хельги" },
                    ["female"] = new() { "Астрид","Брюнхильд","Гудрун","Сигриди","Гунхильд","Хельга","Ингеборг","Тордис","Тора","Альвильд","Герда","Фрейдис","Раннвейг","Сигрид","Эрна","Грета","Кристин","Хильда","Руна","Сванхильд" },
                    ["surnames"] = new() { "Бьёрнсон","Эйнарсон","Харальдсон","Свенсон","Рагнарсон","Ульфсон","Олафсон","Гуннарсон","Сигурдсон","Торгерсон","Кетильсон","Асбьёрнсон","Торстейнсон","Эгильсон","Гудмундсон" },
                    ["titles"] = new() { "Берсерк","Скальд","Молот","Боевой Топор","Кровавый","Северный","Грозный","Мудрый" }
                },
                ["roman"] = new()
                {
                    ["male"] = new() { "Гай","Луций","Марк","Квинт","Публий","Секст","Тит","Авл","Децим","Гней","Сервий","Нумерий","Август","Тиберий","Клавдий","Нерон","Траян","Адриан","Антоний","Юлий" },
                    ["female"] = new() { "Юлия","Ливия","Валерия","Клавдия","Цецилия","Теренция","Октавия","Друзилла","Агриппина","Мессалина","Антония","Корнелия","Помпея","Вителлия","Германика","Фавстина","Луцилла","Криспина","Сабина","Вероника" },
                    ["surnames"] = new() { "Цезарь","Август","Нерон","Клавдий","Тиберий","Траян","Адриан","Антоний","Юлий","Германик","Брут","Сципион","Катон","Цицерон","Сенека","Аврелий","Флавий","Константин" },
                    ["titles"] = new() { "Император","Консул","Сенатор","Легат","Претор","Эдил","Цензор","Диктатор" }
                },
                ["medieval"] = new()
                {
                    ["male"] = new() { "Уильям","Генри","Ричард","Роберт","Эдуард","Джон","Томас","Джеффри","Роджер","Хью","Саймон","Уолтер","Гилберт","Реджинальд","Балдуин","Алан","Брайан","Филип","Джеймс","Дэвид" },
                    ["female"] = new() { "Элеонора","Матильда","Алиенора","Агнес","Изабелла","Беатрис","Сибилла","Маргарет","Кэтрин","Джоан","Алиса","Сесилия","Ида","Мария","Клеменция","Амелия","Филиппа","Гвендолин","Аделаида","Энн" },
                    ["surnames"] = new() { "Нормандский","Анжуйский","Ланкастерский","Йоркский","Плантагенет","Уэссекский","Глостерский","Кентский","Суффолкский","Ричмондский","Пемброк","Мортимер","Грей","Стюарт","Говард" },
                    ["titles"] = new() { "Король","Герцог","Граф","Барон","Рыцарь","Лорд","Епископ","Маркиз" }
                },
                ["greek"] = new()
                {
                    ["male"] = new() { "Александр","Аристотель","Демокрит","Эпикур","Гераклит","Платон","Сократ","Пифагор","Гомер","Софокл","Еврипид","Эсхил","Перикл","Леонид","Фемистокл","Агамемнон","Ахилл","Одиссей","Гектор","Тесей" },
                    ["female"] = new() { "Афина","Гера","Афродита","Артемида","Деметра","Персефона","Гестия","Ника","Ирида","Афродита","Кассандра","Андромеда","Елена","Пенелопа","Антигона","Электра","Ифигения","Медея","Сапфо","Гипполита" },
                    ["surnames"] = new() { "Афинский","Спартанский","Коринфский","Фиванский","Македонский","Фессалийский","Критский","Родосский","Милетский","Эфесский" },
                    ["titles"] = new() { "Царь","Тиран","Архонт","Стратег","Философ","Поэт","Воин","Герой" }
                },
                ["slavic"] = new()
                {
                    ["male"] = new() { "Владимир","Святослав","Ярослав","Всеволод","Изяслав","Мстислав","Олег","Игорь","Святополк","Вячеслав","Ярополк","Глеб","Борис","Андрей","Александр","Дмитрий","Михаил","Пётр","Иван","Василий" },
                    ["female"] = new() { "Ольга","Ярославна","Владислава","Людмила","Мирослава","Светлана","Евдокия","Борислава","Велеслава","Добромира","Вера","Надежда","Любовь","Мария","Анна","Екатерина","Анастасия","Татьяна","Елена","Ирина" },
                    ["surnames"] = new() { "Рюрикович","Владимирович","Святославич","Ярославич","Всеволодович","Изяславич","Мстиславич","Олегович","Игоревич","Святополкович" },
                    ["titles"] = new() { "Князь","Воевода","Боярин","Дружинник","Мудрый","Грозный","Святой","Великий" }
                },
                ["celtic"] = new()
                {
                    ["male"] = new() { "Айдан","Брендан","Коннор","Деклан","Эйлин","Финн","Гэвин","Иан","Киран","Лайам","Мэлколм","Ниалл","Она","Патрик","Рори","Шон","Тиадг","Улисс","Эмон","Брайан" },
                    ["female"] = new() { "Айслин","Бриджит","Кэтлин","Диана","Эйлиш","Финола","Грэйн","Иона","Кира","Лианна","Майв","Ниам","Она","Рона","Сиана","Тара","Уна","Фиона","Сиобан","Морриган" },
                    ["surnames"] = new() { "МакКауд","О'Брайен","О'Салливан","МакКарти","О'Доннелл","МакДонах","О'Нейл","МакГрат","О'Ши","МакМахон","О'Коннор","МакДауэлл","О'Киф","МакГиннис","О'Флаэрти" },
                    ["titles"] = new() { "Король","Вождь","Друид","Воин","Мудрец","Певец","Охотник","Мастер" }
                }
            };

            private readonly string culture, gender;
            private readonly int count;
            private readonly bool surname, title;
            private readonly Random rng;
            private readonly Dictionary<string, List<string>> cultureData;

            public HistoricalNameGenerator(Options opts)
            {
                culture = opts.Culture;
                gender = opts.Gender;
                count = opts.Count;
                surname = opts.Surname;
                title = opts.Title;
                rng = opts.Seed.HasValue ? new Random((int)opts.Seed.Value) : new Random();
                cultureData = Names.GetValueOrDefault(culture, Names["viking"]);
            }

            public string GenerateName()
            {
                var names = cultureData.GetValueOrDefault(gender, cultureData["male"]);
                string name = names[rng.Next(names.Count)];
                if (surname && cultureData.ContainsKey("surnames"))
                {
                    var snames = cultureData["surnames"];
                    name += " " + snames[rng.Next(snames.Count)];
                }
                if (title && cultureData.ContainsKey("titles"))
                {
                    var titles = cultureData["titles"];
                    name += " " + titles[rng.Next(titles.Count)];
                }
                return name;
            }

            public List<string> Generate()
            {
                var list = new List<string>();
                for (int i = 0; i < count; i++) list.Add(GenerateName());
                return list;
            }
        }

        static void PrintNames(List<string> names, string culture, string gender, bool color)
        {
            var labels = new Dictionary<string, string>
            {
                ["viking"] = "викинг", ["roman"] = "римское", ["medieval"] = "средневековое",
                ["greek"] = "греческое", ["slavic"] = "славянское", ["celtic"] = "кельтское"
            };
            string label = labels.GetValueOrDefault(culture, culture);
            string genderLabel = gender == "male" ? "мужские" : "женские";
            if (color)
            {
                Console.ForegroundColor = ConsoleColor.Cyan;
                Console.WriteLine($"🏛️ Исторические имена ({label}, {genderLabel}):");
                Console.ResetColor();
                for (int i = 0; i < names.Count; i++)
                {
                    Console.ForegroundColor = ConsoleColor.Green;
                    Console.WriteLine($"{i+1}. {names[i]}");
                    Console.ResetColor();
                }
            }
            else
            {
                Console.WriteLine($"Исторические имена ({label}, {genderLabel}):");
                for (int i = 0; i < names.Count; i++) Console.WriteLine($"{i+1}. {names[i]}");
            }
        }

        static void ExportJson(List<string> names, string culture, string gender, string filename)
        {
            var data = new { culture, gender, names };
            string json = JsonSerializer.Serialize(data, new JsonSerializerOptions { WriteIndented = true });
            File.WriteAllText(filename, json);
        }

        static void ExportCsv(List<string> names, string filename)
        {
            using var sw = new StreamWriter(filename);
            sw.WriteLine("name");
            foreach (var n in names) sw.WriteLine(n);
        }

        static void ExportText(List<string> names, string filename)
        {
            File.WriteAllText(filename, string.Join("\n", names));
        }
    }
}
