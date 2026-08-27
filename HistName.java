// HistName.java
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class HistName {
    private static final Map<String, Map<String, List<String>>> NAMES = new HashMap<>();

    static {
        // Viking
        Map<String, List<String>> viking = new HashMap<>();
        viking.put("male", Arrays.asList("Эйнар","Рагнар","Бьёрн","Свен","Ульф","Харальд","Олаф","Эрик","Торгейр","Гуннар","Сигурд","Хакон","Кнуд","Орм","Кетиль","Асбьёрн","Торстейн","Эгиль","Гудмунд","Хельги"));
        viking.put("female", Arrays.asList("Астрид","Брюнхильд","Гудрун","Сигриди","Гунхильд","Хельга","Ингеборг","Тордис","Тора","Альвильд","Герда","Фрейдис","Раннвейг","Сигрид","Эрна","Грета","Кристин","Хильда","Руна","Сванхильд"));
        viking.put("surnames", Arrays.asList("Бьёрнсон","Эйнарсон","Харальдсон","Свенсон","Рагнарсон","Ульфсон","Олафсон","Гуннарсон","Сигурдсон","Торгерсон","Кетильсон","Асбьёрнсон","Торстейнсон","Эгильсон","Гудмундсон"));
        viking.put("titles", Arrays.asList("Берсерк","Скальд","Молот","Боевой Топор","Кровавый","Северный","Грозный","Мудрый"));
        NAMES.put("viking", viking);

        // Roman
        Map<String, List<String>> roman = new HashMap<>();
        roman.put("male", Arrays.asList("Гай","Луций","Марк","Квинт","Публий","Секст","Тит","Авл","Децим","Гней","Сервий","Нумерий","Август","Тиберий","Клавдий","Нерон","Траян","Адриан","Антоний","Юлий"));
        roman.put("female", Arrays.asList("Юлия","Ливия","Валерия","Клавдия","Цецилия","Теренция","Октавия","Друзилла","Агриппина","Мессалина","Антония","Корнелия","Помпея","Вителлия","Германика","Фавстина","Луцилла","Криспина","Сабина","Вероника"));
        roman.put("surnames", Arrays.asList("Цезарь","Август","Нерон","Клавдий","Тиберий","Траян","Адриан","Антоний","Юлий","Германик","Брут","Сципион","Катон","Цицерон","Сенека","Аврелий","Флавий","Константин"));
        roman.put("titles", Arrays.asList("Император","Консул","Сенатор","Легат","Претор","Эдил","Цензор","Диктатор"));
        NAMES.put("roman", roman);

        // Medieval
        Map<String, List<String>> medieval = new HashMap<>();
        medieval.put("male", Arrays.asList("Уильям","Генри","Ричард","Роберт","Эдуард","Джон","Томас","Джеффри","Роджер","Хью","Саймон","Уолтер","Гилберт","Реджинальд","Балдуин","Алан","Брайан","Филип","Джеймс","Дэвид"));
        medieval.put("female", Arrays.asList("Элеонора","Матильда","Алиенора","Агнес","Изабелла","Беатрис","Сибилла","Маргарет","Кэтрин","Джоан","Алиса","Сесилия","Ида","Мария","Клеменция","Амелия","Филиппа","Гвендолин","Аделаида","Энн"));
        medieval.put("surnames", Arrays.asList("Нормандский","Анжуйский","Ланкастерский","Йоркский","Плантагенет","Уэссекский","Глостерский","Кентский","Суффолкский","Ричмондский","Пемброк","Мортимер","Грей","Стюарт","Говард"));
        medieval.put("titles", Arrays.asList("Король","Герцог","Граф","Барон","Рыцарь","Лорд","Епископ","Маркиз"));
        NAMES.put("medieval", medieval);

        // Greek
        Map<String, List<String>> greek = new HashMap<>();
        greek.put("male", Arrays.asList("Александр","Аристотель","Демокрит","Эпикур","Гераклит","Платон","Сократ","Пифагор","Гомер","Софокл","Еврипид","Эсхил","Перикл","Леонид","Фемистокл","Агамемнон","Ахилл","Одиссей","Гектор","Тесей"));
        greek.put("female", Arrays.asList("Афина","Гера","Афродита","Артемида","Деметра","Персефона","Гестия","Ника","Ирида","Афродита","Кассандра","Андромеда","Елена","Пенелопа","Антигона","Электра","Ифигения","Медея","Сапфо","Гипполита"));
        greek.put("surnames", Arrays.asList("Афинский","Спартанский","Коринфский","Фиванский","Македонский","Фессалийский","Критский","Родосский","Милетский","Эфесский"));
        greek.put("titles", Arrays.asList("Царь","Тиран","Архонт","Стратег","Философ","Поэт","Воин","Герой"));
        NAMES.put("greek", greek);

        // Slavic
        Map<String, List<String>> slavic = new HashMap<>();
        slavic.put("male", Arrays.asList("Владимир","Святослав","Ярослав","Всеволод","Изяслав","Мстислав","Олег","Игорь","Святополк","Вячеслав","Ярополк","Глеб","Борис","Андрей","Александр","Дмитрий","Михаил","Пётр","Иван","Василий"));
        slavic.put("female", Arrays.asList("Ольга","Ярославна","Владислава","Людмила","Мирослава","Светлана","Евдокия","Борислава","Велеслава","Добромира","Вера","Надежда","Любовь","Мария","Анна","Екатерина","Анастасия","Татьяна","Елена","Ирина"));
        slavic.put("surnames", Arrays.asList("Рюрикович","Владимирович","Святославич","Ярославич","Всеволодович","Изяславич","Мстиславич","Олегович","Игоревич","Святополкович"));
        slavic.put("titles", Arrays.asList("Князь","Воевода","Боярин","Дружинник","Мудрый","Грозный","Святой","Великий"));
        NAMES.put("slavic", slavic);

        // Celtic
        Map<String, List<String>> celtic = new HashMap<>();
        celtic.put("male", Arrays.asList("Айдан","Брендан","Коннор","Деклан","Эйлин","Финн","Гэвин","Иан","Киран","Лайам","Мэлколм","Ниалл","Она","Патрик","Рори","Шон","Тиадг","Улисс","Эмон","Брайан"));
        celtic.put("female", Arrays.asList("Айслин","Бриджит","Кэтлин","Диана","Эйлиш","Финола","Грэйн","Иона","Кира","Лианна","Майв","Ниам","Она","Рона","Сиана","Тара","Уна","Фиона","Сиобан","Морриган"));
        celtic.put("surnames", Arrays.asList("МакКауд","О'Брайен","О'Салливан","МакКарти","О'Доннелл","МакДонах","О'Нейл","МакГрат","О'Ши","МакМахон","О'Коннор","МакДауэлл","О'Киф","МакГиннис","О'Флаэрти"));
        celtic.put("titles", Arrays.asList("Король","Вождь","Друид","Воин","Мудрец","Певец","Охотник","Мастер"));
        NAMES.put("celtic", celtic);
    }

    @Parameter(names = "--culture")
    private String culture = "viking";
    @Parameter(names = "--gender")
    private String gender = "male";
    @Parameter(names = "--count")
    private int count = 1;
    @Parameter(names = "--surname")
    private boolean surname = false;
    @Parameter(names = "--title")
    private boolean title = false;
    @Parameter(names = "--seed")
    private Long seed = null;
    @Parameter(names = "--output")
    private String outputFile;
    @Parameter(names = "--format")
    private String format;

    private Random rng;

    private void initRng() {
        rng = (seed != null) ? new Random(seed) : new Random();
    }

    private String generateName() {
        Map<String, List<String>> cultureData = NAMES.getOrDefault(culture, NAMES.get("viking"));
        List<String> names = cultureData.getOrDefault(gender, cultureData.get("male"));
        String name = names.get(rng.nextInt(names.size()));
        if (surname && cultureData.containsKey("surnames")) {
            List<String> snames = cultureData.get("surnames");
            name += " " + snames.get(rng.nextInt(snames.size()));
        }
        if (title && cultureData.containsKey("titles")) {
            List<String> titles = cultureData.get("titles");
            name += " " + titles.get(rng.nextInt(titles.size()));
        }
        return name;
    }

    private List<String> generate() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(generateName());
        }
        return list;
    }

    private void printNames(List<String> names) {
        Map<String, String> labels = new HashMap<>();
        labels.put("viking","викинг"); labels.put("roman","римское");
        labels.put("medieval","средневековое"); labels.put("greek","греческое");
        labels.put("slavic","славянское"); labels.put("celtic","кельтское");
        String label = labels.getOrDefault(culture, culture);
        String genderLabel = gender.equals("male") ? "мужские" : "женские";
        boolean color = System.console() != null;
        if (color) {
            System.out.println("\u001B[36m🏛️ Исторические имена (" + label + ", " + genderLabel + "):\u001B[0m");
            for (int i = 0; i < names.size(); i++) {
                System.out.println((i+1) + ". \u001B[32m" + names.get(i) + "\u001B[0m");
            }
        } else {
            System.out.println("Исторические имена (" + label + ", " + genderLabel + "):");
            for (int i = 0; i < names.size(); i++) {
                System.out.println((i+1) + ". " + names.get(i));
            }
        }
    }

    private void exportJson(List<String> names) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("culture", culture);
        data.put("gender", gender);
        data.put("names", names);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(data);
        Files.write(Paths.get(outputFile), json.getBytes());
    }

    private void exportCsv(List<String> names) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(outputFile))) {
            pw.println("name");
            for (String n : names) pw.println(n);
        }
    }

    private void exportText(List<String> names) throws IOException {
        Files.write(Paths.get(outputFile), String.join("\n", names).getBytes());
    }

    public void run() throws Exception {
        initRng();
        List<String> names = generate();
        if (outputFile != null) {
            String fmt = format;
            if (fmt == null) {
                String ext = outputFile.substring(outputFile.lastIndexOf('.') + 1);
                fmt = ext.equals("json") ? "json" : ext.equals("csv") ? "csv" : "text";
            }
            switch (fmt) {
                case "json": exportJson(names); break;
                case "csv": exportCsv(names); break;
                default: exportText(names); break;
            }
            System.out.println("Результат сохранён в " + outputFile);
        } else {
            printNames(names);
        }
    }

    public static void main(String[] args) throws Exception {
        HistName gen = new HistName();
        JCommander.newBuilder().addObject(gen).build().parse(args);
        gen.run();
    }
}
