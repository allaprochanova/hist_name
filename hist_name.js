#!/usr/bin/env node
// hist_name.js
const { program } = require('commander');
const fs = require('fs');
const chalk = require('chalk');
const crypto = require('crypto');

const NAMES = {
    viking: {
        male: ['Эйнар','Рагнар','Бьёрн','Свен','Ульф','Харальд','Олаф','Эрик','Торгейр','Гуннар','Сигурд','Хакон','Кнуд','Орм','Кетиль','Асбьёрн','Торстейн','Эгиль','Гудмунд','Хельги'],
        female: ['Астрид','Брюнхильд','Гудрун','Сигриди','Гунхильд','Хельга','Ингеборг','Тордис','Тора','Альвильд','Герда','Фрейдис','Раннвейг','Сигрид','Эрна','Грета','Кристин','Хильда','Руна','Сванхильд'],
        surnames: ['Бьёрнсон','Эйнарсон','Харальдсон','Свенсон','Рагнарсон','Ульфсон','Олафсон','Гуннарсон','Сигурдсон','Торгерсон','Кетильсон','Асбьёрнсон','Торстейнсон','Эгильсон','Гудмундсон'],
        titles: ['Берсерк','Скальд','Молот','Боевой Топор','Кровавый','Северный','Грозный','Мудрый']
    },
    roman: {
        male: ['Гай','Луций','Марк','Квинт','Публий','Секст','Тит','Авл','Децим','Гней','Сервий','Нумерий','Август','Тиберий','Клавдий','Нерон','Траян','Адриан','Антоний','Юлий'],
        female: ['Юлия','Ливия','Валерия','Клавдия','Цецилия','Теренция','Октавия','Друзилла','Агриппина','Мессалина','Антония','Корнелия','Помпея','Вителлия','Германика','Фавстина','Луцилла','Криспина','Сабина','Вероника'],
        surnames: ['Цезарь','Август','Нерон','Клавдий','Тиберий','Траян','Адриан','Антоний','Юлий','Германик','Брут','Сципион','Катон','Цицерон','Сенека','Аврелий','Флавий','Константин'],
        titles: ['Император','Консул','Сенатор','Легат','Претор','Эдил','Цензор','Диктатор']
    },
    medieval: {
        male: ['Уильям','Генри','Ричард','Роберт','Эдуард','Джон','Томас','Джеффри','Роджер','Хью','Саймон','Уолтер','Гилберт','Реджинальд','Балдуин','Алан','Брайан','Филип','Джеймс','Дэвид'],
        female: ['Элеонора','Матильда','Алиенора','Агнес','Изабелла','Беатрис','Сибилла','Маргарет','Кэтрин','Джоан','Алиса','Сесилия','Ида','Мария','Клеменция','Амелия','Филиппа','Гвендолин','Аделаида','Энн'],
        surnames: ['Нормандский','Анжуйский','Ланкастерский','Йоркский','Плантагенет','Уэссекский','Глостерский','Кентский','Суффолкский','Ричмондский','Пемброк','Мортимер','Грей','Стюарт','Говард'],
        titles: ['Король','Герцог','Граф','Барон','Рыцарь','Лорд','Епископ','Маркиз']
    },
    greek: {
        male: ['Александр','Аристотель','Демокрит','Эпикур','Гераклит','Платон','Сократ','Пифагор','Гомер','Софокл','Еврипид','Эсхил','Перикл','Леонид','Фемистокл','Агамемнон','Ахилл','Одиссей','Гектор','Тесей'],
        female: ['Афина','Гера','Афродита','Артемида','Деметра','Персефона','Гестия','Ника','Ирида','Афродита','Кассандра','Андромеда','Елена','Пенелопа','Антигона','Электра','Ифигения','Медея','Сапфо','Гипполита'],
        surnames: ['Афинский','Спартанский','Коринфский','Фиванский','Македонский','Фессалийский','Критский','Родосский','Милетский','Эфесский'],
        titles: ['Царь','Тиран','Архонт','Стратег','Философ','Поэт','Воин','Герой']
    },
    slavic: {
        male: ['Владимир','Святослав','Ярослав','Всеволод','Изяслав','Мстислав','Олег','Игорь','Святополк','Вячеслав','Ярополк','Глеб','Борис','Андрей','Александр','Дмитрий','Михаил','Пётр','Иван','Василий'],
        female: ['Ольга','Ярославна','Владислава','Людмила','Мирослава','Светлана','Евдокия','Борислава','Велеслава','Добромира','Вера','Надежда','Любовь','Мария','Анна','Екатерина','Анастасия','Татьяна','Елена','Ирина'],
        surnames: ['Рюрикович','Владимирович','Святославич','Ярославич','Всеволодович','Изяславич','Мстиславич','Олегович','Игоревич','Святополкович'],
        titles: ['Князь','Воевода','Боярин','Дружинник','Мудрый','Грозный','Святой','Великий']
    },
    celtic: {
        male: ['Айдан','Брендан','Коннор','Деклан','Эйлин','Финн','Гэвин','Иан','Киран','Лайам','Мэлколм','Ниалл','Она','Патрик','Рори','Шон','Тиадг','Улисс','Эмон','Брайан'],
        female: ['Айслин','Бриджит','Кэтлин','Диана','Эйлиш','Финола','Грэйн','Иона','Кира','Лианна','Майв','Ниам','Она','Рона','Сиана','Тара','Уна','Фиона','Сиобан','Морриган'],
        surnames: ['МакКауд','О\'Брайен','О\'Салливан','МакКарти','О\'Доннелл','МакДонах','О\'Нейл','МакГрат','О\'Ши','МакМахон','О\'Коннор','МакДауэлл','О\'Киф','МакГиннис','О\'Флаэрти'],
        titles: ['Король','Вождь','Друид','Воин','Мудрец','Певец','Охотник','Мастер']
    }
};

class HistoricalNameGenerator {
    constructor(options) {
        this.culture = options.culture || 'viking';
        this.gender = options.gender || 'male';
        this.count = options.count || 1;
        this.surname = options.surname || false;
        this.title = options.title || false;
        this.seed = options.seed || null;
        this.color = options.color || process.stdout.isTTY;
        if (this.seed !== null) {
            let s = this.seed;
            this._random = () => {
                s = (s * 9301 + 49297) % 233280;
                return s / 233280;
            };
        } else {
            this._random = () => crypto.randomInt(0, 1e9) / 1e9;
        }
        this.cultureData = NAMES[this.culture] || NAMES.viking;
    }

    generateName() {
        const names = this.cultureData[this.gender] || this.cultureData.male;
        let name = names[Math.floor(this._random() * names.length)];
        if (this.surname && this.cultureData.surnames) {
            const sname = this.cultureData.surnames[Math.floor(this._random() * this.cultureData.surnames.length)];
            name += ' ' + sname;
        }
        if (this.title && this.cultureData.titles) {
            const title = this.cultureData.titles[Math.floor(this._random() * this.cultureData.titles.length)];
            name += ' ' + title;
        }
        return name;
    }

    generate() {
        return Array.from({ length: this.count }, () => this.generateName());
    }

    print(names) {
        const cultureLabels = { viking:'викинг', roman:'римское', medieval:'средневековое', greek:'греческое', slavic:'славянское', celtic:'кельтское' };
        const cultureLabel = cultureLabels[this.culture] || this.culture;
        const genderLabel = this.gender === 'male' ? 'мужские' : 'женские';
        if (this.color) {
            console.log(chalk.cyan(`🏛️ Исторические имена (${cultureLabel}, ${genderLabel}):`));
            names.forEach((n, i) => console.log(`${i+1}. ${chalk.green(n)}`));
        } else {
            console.log(`Исторические имена (${cultureLabel}, ${genderLabel}):`);
            names.forEach((n, i) => console.log(`${i+1}. ${n}`));
        }
    }

    exportJson(names, filename) {
        fs.writeFileSync(filename, JSON.stringify({ culture: this.culture, gender: this.gender, names }, null, 2));
    }

    exportCsv(names, filename) {
        const content = 'name\n' + names.join('\n');
        fs.writeFileSync(filename, content);
    }

    exportText(names, filename) {
        fs.writeFileSync(filename, names.join('\n'));
    }
}

program
    .option('-c, --culture <culture>', 'culture: viking, roman, medieval, greek, slavic, celtic', 'viking')
    .option('-g, --gender <gender>', 'gender: male, female', 'male')
    .option('--count <number>', 'Number of names', parseInt, 1)
    .option('--surname', 'Add surname')
    .option('--title', 'Add title')
    .option('--seed <number>', 'Seed', parseInt)
    .option('--output <file>', 'Output file')
    .option('--format <format>', 'Format: text, json, csv')
    .parse(process.argv);

const opts = program.opts();
const gen = new HistoricalNameGenerator(opts);
const names = gen.generate();

if (opts.output) {
    const ext = opts.output.split('.').pop().toLowerCase();
    const fmt = opts.format || (ext === 'json' ? 'json' : ext === 'csv' ? 'csv' : 'text');
    if (fmt === 'json') gen.exportJson(names, opts.output);
    else if (fmt === 'csv') gen.exportCsv(names, opts.output);
    else gen.exportText(names, opts.output);
    console.log(`Результат сохранён в ${opts.output}`);
} else {
    gen.print(names);
}
