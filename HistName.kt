// HistName.kt
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.google.gson.GsonBuilder
import java.io.File
import kotlin.random.Random

val NAMES = mapOf(
    "viking" to mapOf(
        "male" to listOf("Эйнар","Рагнар","Бьёрн","Свен","Ульф","Харальд","Олаф","Эрик","Торгейр","Гуннар","Сигурд","Хакон","Кнуд","Орм","Кетиль","Асбьёрн","Торстейн","Эгиль","Гудмунд","Хельги"),
        "female" to listOf("Астрид","Брюнхильд","Гудрун","Сигриди","Гунхильд","Хельга","Ингеборг","Тордис","Тора","Альвильд","Герда","Фрейдис","Раннвейг","Сигрид","Эрна","Грета","Кристин","Хильда","Руна","Сванхильд"),
        "surnames" to listOf("Бьёрнсон","Эйнарсон","Харальдсон","Свенсон","Рагнарсон","Ульфсон","Олафсон","Гуннарсон","Сигурдсон","Торгерсон","Кетильсон","Асбьёрнсон","Торстейнсон","Эгильсон","Гудмундсон"),
        "titles" to listOf("Берсерк","Скальд","Молот","Боевой Топор","Кровавый","Северный","Грозный","Мудрый")
    ),
    "roman" to mapOf(
        "male" to listOf("Гай","Луций","Марк","Квинт","Публий","Секст","Тит","Авл","Децим","Гней","Сервий","Нумерий","Август","Тиберий","Клавдий","Нерон","Траян","Адриан","Антоний","Юлий"),
        "female" to listOf("Юлия","Ливия","Валерия","Клавдия","Цецилия","Теренция","Октавия","Друзилла","Агриппина","Мессалина","Антония","Корнелия","Помпея","Вителлия","Германика","Фавстина","Луцилла","Криспина","Сабина","Вероника"),
        "surnames" to listOf("Цезарь","Август","Нерон","Клавдий","Тиберий","Траян","Адриан","Антоний","Юлий","Германик","Брут","Сципион","Катон","Цицерон","Сенека","Аврелий","Флавий","Константин"),
        "titles" to listOf("Император","Консул","Сенатор","Легат","Претор","Эдил","Цензор","Диктатор")
    ),
    "medieval" to mapOf(
        "male" to listOf("Уильям","Генри","Ричард","Роберт","Эдуард","Джон","Томас","Джеффри","Роджер","Хью","Саймон","Уолтер","Гилберт","Реджинальд","Балдуин","Алан","Брайан","Филип","Джеймс","Дэвид"),
        "female" to listOf("Элеонора","Матильда","Алиенора","Агнес","Изабелла","Беатрис","Сибилла","Маргарет","Кэтрин","Джоан","Алиса","Сесилия","Ида","Мария","Клеменция","Амелия","Филиппа","Гвендолин","Аделаида","Энн"),
        "surnames" to listOf("Нормандский","Анжуйский","Ланкастерский","Йоркский","Плантагенет","Уэссекский","Глостерский","Кентский","Суффолкский","Ричмондский","Пемброк","Мортимер","Грей","Стюарт","Говард"),
        "titles" to listOf("Король","Герцог","Граф","Барон","Рыцарь","Лорд","Епископ","Маркиз")
    ),
    "greek" to mapOf(
        "male" to listOf("Александр","Аристотель","Демокрит","Эпикур","Гераклит","Платон","Сократ","Пифагор","Гомер","Софокл","Еврипид","Эсхил","Перикл","Леонид","Фемистокл","Агамемнон","Ахилл","Одиссей","Гектор","Тесей"),
        "female" to listOf("Афина","Гера","Афродита","Артемида","Деметра","Персефона","Гестия","Ника","Ирида","Афродита","Кассандра","Андромеда","Елена","Пенелопа","Антигона","Электра","Ифигения","Медея","Сапфо","Гипполита"),
        "surnames" to listOf("Афинский","Спартанский","Коринфский","Фиванский","Македонский","Фессалийский","Критский","Родосский","Милетский","Эфесский"),
        "titles" to listOf("Царь","Тиран","Архонт","Стратег","Философ","Поэт","Воин","Герой")
    ),
    "slavic" to mapOf(
        "male" to listOf("Владимир","Святослав","Ярослав","Всеволод","Изяслав","Мстислав","Олег","Игорь","Святополк","Вячеслав","Ярополк","Глеб","Борис","Андрей","Александр","Дмитрий","Михаил","Пётр","Иван","Василий"),
        "female" to listOf("Ольга","Ярославна","Владислава","Людмила","Мирослава","Светлана","Евдокия","Борислава","Велеслава","Добромира","Вера","Надежда","Любовь","Мария","Анна","Екатерина","Анастасия","Татьяна","Елена","Ирина"),
        "surnames" to listOf("Рюрикович","Владимирович","Святославич","Ярославич","Всеволодович","Изяславич","Мстиславич","Олегович","Игоревич","Святополкович"),
        "titles" to listOf("Князь","Воевода","Боярин","Дружинник","Мудрый","Грозный","Святой","Великий")
    ),
    "celtic" to mapOf(
        "male" to listOf("Айдан","Брендан","Коннор","Деклан","Эйлин","Финн","Гэвин","Иан","Киран","Лайам","Мэлколм","Ниалл","Она","Патрик","Рори","Шон","Тиадг","Улисс","Эмон","Брайан"),
        "female" to listOf("Айслин","Бриджит","Кэтлин","Диана","Эйлиш","Финола","Грэйн","Иона","Кира","Лианна","Майв","Ниам","Она","Рона","Сиана","Тара","Уна","Фиона","Сиобан","Морриган"),
        "surnames" to listOf("МакКауд","О'Брайен","О'Салливан","МакКарти","О'Доннелл","МакДонах","О'Нейл","МакГрат","О'Ши","МакМахон","О'Коннор","МакДауэлл","О'Киф","МакГиннис","О'Флаэрти"),
        "titles" to listOf("Король","Вождь","Друид","Воин","Мудрец","Певец","Охотник","Мастер")
    )
)

class Generator {
    @Parameter(names = ["--culture"])
    var culture: String = "viking"

    @Parameter(names = ["--gender"])
    var gender: String = "male"

    @Parameter(names = ["--count"])
    var count: Int = 1

    @Parameter(names = ["--surname"])
    var surname: Boolean = false

    @Parameter(names = ["--title"])
    var title: Boolean = false

    @Parameter(names = ["--seed"])
    var seed: Long? = null

    @Parameter(names = ["--output"])
    var outputFile: String? = null

    @Parameter(names = ["--format"])
    var format: String? = null

    private lateinit var rng: Random
    private lateinit var cultureData: Map<String, List<String>>

    fun initRng() {
        rng = if (seed != null) Random(seed!!) else Random(System.currentTimeMillis())
        cultureData = NAMES[culture] ?: NAMES["viking"]!!
    }

    private fun generateName(): String {
        val names = cultureData[gender] ?: cultureData["male"]!!
        var name = names[rng.nextInt(names.size)]
        if (surname) {
            val snames = cultureData["surnames"]
            if (snames != null) {
                name += " " + snames[rng.nextInt(snames.size)]
            }
        }
        if (title) {
            val titles = cultureData["titles"]
            if (titles != null) {
                name += " " + titles[rng.nextInt(titles.size)]
            }
        }
        return name
    }

    fun generate(): List<String> = List(count) { generateName() }

    private fun printNames(names: List<String>) {
        val labels = mapOf(
            "viking" to "викинг", "roman" to "римское", "medieval" to "средневековое",
            "greek" to "греческое", "slavic" to "славянское", "celtic" to "кельтское"
        )
        val label = labels[culture] ?: culture
        val genderLabel = if (gender == "male") "мужские" else "женские"
        val color = System.console() != null
        if (color) {
            println("\u001B[36m🏛️ Исторические имена ($label, $genderLabel):\u001B[0m")
            names.forEachIndexed { i, n ->
                println("${i+1}. \u001B[32m$n\u001B[0m")
            }
        } else {
            println("Исторические имена ($label, $genderLabel):")
            names.forEachIndexed { i, n -> println("${i+1}. $n") }
        }
    }

    private fun exportJson(names: List<String>) {
        val data = mapOf("culture" to culture, "gender" to gender, "names" to names)
        val gson = GsonBuilder().setPrettyPrinting().create()
        File(outputFile).writeText(gson.toJson(data))
    }

    private fun exportCsv(names: List<String>) {
        File(outputFile).printWriter().use { pw ->
            pw.println("name")
            names.forEach { pw.println(it) }
        }
    }

    private fun exportText(names: List<String>) {
        File(outputFile).writeText(names.joinToString("\n"))
    }

    fun run() {
        initRng()
        val names = generate()
        if (outputFile != null) {
            val fmt = format ?: when {
                outputFile!!.endsWith(".json") -> "json"
                outputFile!!.endsWith(".csv") -> "csv"
                else -> "text"
            }
            when (fmt) {
                "json" -> exportJson(names)
                "csv" -> exportCsv(names)
                else -> exportText(names)
            }
            println("Результат сохранён в $outputFile")
        } else {
            printNames(names)
        }
    }
}

fun main(args: Array<String>) {
    val gen = Generator()
    JCommander.newBuilder().addObject(gen).build().parse(*args)
    gen.run()
}
