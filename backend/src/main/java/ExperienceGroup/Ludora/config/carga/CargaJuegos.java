package ExperienceGroup.Ludora.config.carga;

import ExperienceGroup.Ludora.features.ageRange.IAgeRangeRepository;
import ExperienceGroup.Ludora.features.ageRange.domain.AgeRangeEntity;
import ExperienceGroup.Ludora.features.developer.IDeveloperRepository;
import ExperienceGroup.Ludora.features.developer.domain.DeveloperEntity;
import ExperienceGroup.Ludora.features.game.IGameRepository;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.genre.IGenreRepository;
import ExperienceGroup.Ludora.features.genre.domain.GenreEntity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Order(3)
public class CargaJuegos implements CargaDatos {

    private final IGameRepository gameRepository;
    private final IDeveloperRepository developerRepository;
    private final IAgeRangeRepository ageRangeRepository;
    private final IGenreRepository genreRepository;

    public CargaJuegos(IGameRepository gameRepository,
                       IDeveloperRepository developerRepository,
                       IAgeRangeRepository ageRangeRepository,
                       IGenreRepository genreRepository) {
        this.gameRepository = gameRepository;
        this.developerRepository = developerRepository;
        this.ageRangeRepository = ageRangeRepository;
        this.genreRepository = genreRepository;
    }

    // developer = userName del developer | rangoEdad = nombre del rango (ATP, +13, +16, +18)
    // generos = nombres de los géneros
    private record Juego(String nombre, String portada, String precio, String descripcion,
                         String lanzamiento, String developer, String rangoEdad,
                         List<String> generos, boolean bloqueado, boolean enRevision) {}

    private static final List<Juego> JUEGOS = List.of(
            // ---------------- SANTA MONICA STUDIOS ----------------
            new Juego("God of War Ragnarok", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367919/gow-ragnarok.jpg", "69.99", "Kratos y Atreus en el fin del mundo nórdico.", "2022-11-09", "manuelDev", "+18", List.of("Action", "Aventura"), false, false),
            new Juego("God of War", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793198636/god-of-war-portada.webp", "19.99", "El reinicio de la saga nórdica en tierras salvajes.", "2018-04-20", "manuelDev", "+18", List.of("Action", "Aventura"), false, false),
            new Juego("Spider-Man 2", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367942/spiderman.jpg", "69.99", "Peter Parker y Miles Morales defienden Nueva York.", "2023-10-20", "manuelDev", "+13", List.of("Action", "Aventura"), false, false),
            new Juego("The Last of Us Part I", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367901/tlou1.avif", "59.99", "Historia cruda de supervivencia y lazos humanos mutuos.", "2022-09-02", "manuelDev", "+18", List.of("Action", "Aventura"), false, false),
            new Juego("Uncharted: Legacy of Thieves", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367904/uncharted-legacy-of-thieves.webp", "49.99", "Búsqueda de tesoros históricos con Nathan Drake y Chloe Frazer.", "2022-01-28", "manuelDev", "+13", List.of("Action", "Aventura"), false, false),
            new Juego("God of War III Remastered", null, "19.99", "La venganza final de Kratos contra los dioses del Olimpo.", "2015-07-14", "manuelDev", "+18", List.of("Action", "Aventura"), false, false),
            new Juego("Helldivers 2", null, "39.99", "Shooter cooperativo galáctico luchando por la libertad y la democracia.", "2024-02-08", "manuelDev", "+18", List.of("Action", "Shooter", "Multijugador"), false, false),

            // ---------------- ROCKSTAR GAMES ----------------
            new Juego("GTA V", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367925/gta5.jpg", "29.99", "Crimen, asaltos y caos en la ciudad de Los Santos.", "2013-09-17", "francoDev", "+18", List.of("Action", "Aventura"), false, false),
            new Juego("Red Dead Redemption 2", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367935/rdr2.jpg", "59.99", "La épica y cruda caída de la banda de forajidos de Arthur Morgan.", "2018-10-26", "francoDev", "+18", List.of("Action", "Aventura"), false, false),
            new Juego("GTA IV", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367923/gta4.jpg", "14.99", "Niko Bellic busca escapar de su pasado en Liberty City.", "2008-04-29", "francoDev", "+18", List.of("Action", "Aventura"), false, false),
            new Juego("L.A. Noire", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367927/lanoire.jpg", "19.99", "Investigaciones policiales y conspiraciones criminales en Los Ángeles de 1940.", "2011-05-17", "francoDev", "+18", List.of("Action", "Aventura"), false, false),
            new Juego("Max Payne 3", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367931/maxpayne3.jpg", "19.99", "Acción armada y tiempo bala en los suburbios de São Paulo.", "2012-05-15", "francoDev", "+18", List.of("Action", "Shooter"), false, false),
            new Juego("Red Dead Redemption", null, "49.99", "La historia de John Marston persiguiendo a su antigua banda en la frontera americana.", "2010-05-18", "francoDev", "+18", List.of("Action", "Aventura"), false, false),
            new Juego("Bully", null, "14.99", "Navegá la jerarquía social del internado Bullworth Academy como Jimmy Hopkins.", "2006-10-17", "francoDev", "+16", List.of("Action", "Aventura"), false, false),

            // ---------------- NINTENDO ----------------
            new Juego("Super Mario Odyssey", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367944/super-mario-odyssey.avif", "59.99", "Aventura en 3D junto a Cappy para rescatar a la princesa Peach.", "2017-10-27", "shigeruDev", "ATP", List.of("Aventura"), false, false),
            new Juego("The Legend of Zelda: BotW", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367947/the-legend-of-zelda-breath-of-the-wild.jpg", "59.99", "Explorá el vasto reino de Hyrule en un mundo abierto sin límites.", "2017-03-03", "shigeruDev", "+13", List.of("Aventura", "RPG"), false, false),
            new Juego("Mario Kart 8 Deluxe", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367929/martio-kart-8.avif", "49.99", "Carreras vertiginosas desafiando la gravedad con tus personajes favoritos.", "2017-04-28", "shigeruDev", "ATP", List.of("Deporte"), false, false),
            new Juego("Animal Crossing: New Horizons", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367906/animal-crossing.jpg", "59.99", "Diseñá, construí y gestioná tu propia comunidad en una isla desierta.", "2020-03-20", "shigeruDev", "ATP", List.of("Aventura"), false, false),
            new Juego("Super Smash Bros. Ultimate", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367945/super-smash-bros.avif", "59.99", "El mayor crossover de peleas de la historia de los videojuegos.", "2018-12-07", "shigeruDev", "+13", List.of("Action"), false, false),
            new Juego("The Legend of Zelda: TotK", null, "69.99", "Explorá los cielos y el subsuelo de Hyrule en la secuela directa de BotW.", "2023-05-12", "shigeruDev", "+13", List.of("Aventura", "RPG"), false, false),
            new Juego("Super Mario Bros. Wonder", null, "59.99", "El regreso de Mario en 2D con transformaciones inesperadas y efectos Maravilla.", "2023-10-20", "shigeruDev", "ATP", List.of("Plataformas", "Aventura"), false, false),
            new Juego("Metroid Dread", null, "59.99", "Samus Aran explora el peligroso planeta ZDR perseguida por los mortales robots E.M.M.I.", "2021-10-08", "shigeruDev", "+13", List.of("Action", "Aventura"), false, false),

            // ---------------- CD PROJEKT RED ----------------
            new Juego("The Witcher 3: Wild Hunt", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367900/the-witcher3.jpg", "39.99", "Geralt de Rivia busca a la Niña de la Profecía en un continente en guerra.", "2015-05-18", "cdpDev", "+18", List.of("RPG", "Aventura"), false, false),
            new Juego("Cyberpunk 2077", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367910/cyberpunk.jpg", "59.99", "Un mercenario cibernético busca un implante que otorga la inmortalidad en Night City.", "2020-12-10", "cdpDev", "+18", List.of("RPG", "Shooter", "Action"), false, false),
            new Juego("Phantom Liberty", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367912/cyberpunk-phantom-liberty.jpg", "29.99", "Aventura de suspenso y espionaje en el distrito más peligroso de Night City: Dogtown.", "2023-09-26", "cdpDev", "+18", List.of("RPG", "Action"), false, false),
            new Juego("The Witcher 2", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367951/the-witcher2.jpg", "19.99", "Intrigas políticas y regicidio en los Reinos del Norte con Geralt.", "2011-05-17", "cdpDev", "+18", List.of("RPG", "Action"), false, false),
            new Juego("The Witcher 1", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367949/the-witcher1.jpg", "9.99", "El inicio de la leyenda de Geralt de Rivia en Vizima.", "2008-09-16", "cdpDev", "+18", List.of("RPG"), false, false),
            new Juego("Cyberpunk 2077: Ultimate Edition", null, "79.99", "La experiencia definitiva de Night City incluyendo el juego base y Phantom Liberty.", "2023-12-05", "cdpDev", "+18", List.of("RPG", "Shooter", "Action"), false, false),

            // ---------------- FROMSOFTWARE ----------------
            new Juego("Elden Ring", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367921/elden-ring.jpg", "59.99", "Levántate, Sinluz, y déjate guiar por la gracia hacia las Tierras Intermedias.", "2022-02-25", "hidetakaDev", "+18", List.of("Action", "RPG"), false, false),
            new Juego("Sekiro: Shadows Die Twice", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/814380/header.jpg", "59.99", "Venganza, sigilo y duelos a muerte con katanas en el Japón feudal.", "2019-03-22", "hidetakaDev", "+18", List.of("Action", "Aventura"), false, false),
            new Juego("Dark Souls III", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367915/dark-souls3.jpg", "59.99", "Las cenizas buscan un Señor en un mundo en descomposición total.", "2016-04-12", "hidetakaDev", "+18", List.of("Action", "RPG"), true, false),
            new Juego("Dark Souls Remastered", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367913/darksouls1.jpg", "39.99", "Y entonces hubo fuego. Vuelve a encender la llama en Lordran.", "2018-05-24", "hidetakaDev", "+18", List.of("Action", "RPG"), true, true),
            new Juego("Armored Core VI", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367908/armored-gore4.jpg", "59.99", "Combates vertiginosos de mechas gigantes personalizables en el planeta Rubicón 3.", "2023-08-25", "hidetakaDev", "+13", List.of("Action", "Shooter"), false, false),
            new Juego("Bloodborne", null, "19.99", "Cazá tus pesadillas en la ciudad gótica y mítica de Yharnam.", "2015-03-24", "hidetakaDev", "+18", List.of("Action", "RPG"), false, false),
            new Juego("Dark Souls II: Scholar of the First Sin", null, "39.99", "Viajá al reino en ruinas de Drangleic para romper una maldición eterna.", "2015-04-01", "hidetakaDev", "+18", List.of("Action", "RPG"), false, false),

            // ---------------- CAPCOM ----------------
            new Juego("Resident Evil 4 Remake", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367938/re4.jpg", "59.99", "Leon S. Kennedy rescata a la hija del presidente en una siniestra aldea europea.", "2023-03-24", "capcomDev", "+18", List.of("Action", "Survival Horror"), false, false),
            new Juego("Resident Evil 2 Remake", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/883710/header.jpg", "39.99", "Sobreviví al brote zombi en la comisaría de Raccoon City con Leon y Claire.", "2019-01-25", "capcomDev", "+18", List.of("Survival Horror", "Action"), false, false),
            new Juego("Resident Evil Village", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367936/re2.jpg", "39.99", "Ethan Winters busca a su hija secuestrada enfrentando los horrores del castillo Dimitrescu.", "2021-05-07", "capcomDev", "+18", List.of("Survival Horror", "Action"), false, false),
            new Juego("Devil May Cry 5", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367917/devilmaycray5.jpg", "29.99", "Dante, Nero y V cazan demonios con el combate hack and slash más estilizado.", "2019-03-08", "capcomDev", "+18", List.of("Action"), false, false),
            new Juego("Monster Hunter: World", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367933/monsterhunter.jpg", "29.99", "Caza bestias colosales en ecosistemas vivos para forjar armaduras y armas épicas.", "2018-08-09", "capcomDev", "+13", List.of("Action", "RPG"), false, false),
            new Juego("Street Fighter 6", null, "59.99", "La evolución del juego de peleas con modos World Tour, Fighting Ground y Battle Hub.", "2023-06-02", "capcomDev", "+13", List.of("Lucha", "Action"), false, false),
            new Juego("Monster Hunter Wilds", null, "69.99", "Cacerías en entornos dinámicos e impredecibles con cambios climáticos constantes.", "2025-02-28", "capcomDev", "+13", List.of("Action", "RPG"), false, false),
              // ---------------- VALVE (6) ----------------
            new Juego("Half-Life 2", null, "9.99", "Gordon Freeman lidera la resistencia humana contra la alianza alienígena Combine.", "2004-11-16", "valveDev", "+16", List.of("Action", "Shooter"), false, false),
            new Juego("Portal 2", null, "9.99", "Resolución de acertijos espaciales mediante portales y física junto a la IA GLaDOS.", "2011-04-18", "valveDev", "+13", List.of("Puzzles", "Aventura"), false, false),
            new Juego("Counter-Strike 2", null, "0.00", "El shooter táctico competitivo por equipos definitivo impulsado por Source 2.", "2023-09-27", "valveDev", "+18", List.of("Shooter", "Multijugador"), false, false),
            new Juego("Left 4 Dead 2", null, "9.99", "Supervivencia cooperativa de cuatro jugadores contra hordas masivas de infectados.", "2009-11-17", "valveDev", "+18", List.of("Shooter", "Survival Horror", "Multijugador"), false, false),
            new Juego("Dota 2", null, "0.00", "Estrategia y acción multijugador masivo en arena donde dos equipos destruyen el ancestro rival.", "2013-07-09", "valveDev", "+13", List.of("Estrategia", "Multijugador"), false, false),
            new Juego("Half-Life: Alyx", null, "59.99", "La experiencia en realidad virtual definitiva combatiendo a la alianza Combine.", "2020-03-23", "valveDev", "+16", List.of("Action", "Shooter", "Aventura"), false, false),

            // ---------------- BETHESDA (6) ----------------
            new Juego("The Elder Scrolls V: Skyrim", null, "39.99", "El Sangre de Dragón busca derrotar al dragón Alduin en un vasto mundo nórdico.", "2011-11-11", "bethesdaDev", "+18", List.of("RPG", "Aventura"), false, false),
            new Juego("Fallout 4", null, "19.99", "Sobreviví al yermo posapocalíptico de Boston en busca de tu hijo secuestrado.", "2015-11-10", "bethesdaDev", "+18", List.of("RPG", "Shooter"), false, false),
            new Juego("DOOM Eternal", null, "39.99", "El Doom Slayer regresa a la Tierra para erradicar las fuerzas del Infierno.", "2020-03-20", "bethesdaDev", "+18", List.of("Action", "Shooter"), false, false),
            new Juego("Fallout: New Vegas", null, "9.99", "Navegá las facciones en guerra y la diplomacia del peligroso yermo de Nevada.", "2010-10-19", "bethesdaDev", "+18", List.of("RPG", "Shooter"), false, false),
            new Juego("Starfield", null, "69.99", "Exploración espacial masiva descubriendo los misterios de la humanidad entre las estrellas.", "2023-09-06", "bethesdaDev", "+18", List.of("RPG", "Aventura"), false, false),
            new Juego("Dishonored 2", null, "29.99", "Infiltración, sigilo y poderes sobrenaturales en la colorida y decadente ciudad de Karnaca.", "2016-11-11", "bethesdaDev", "+18", List.of("Action", "Aventura"), false, false),

            // ---------------- ELECTRONIC ARTS (6) ----------------
            new Juego("EA SPORTS FC 24", null, "69.99", "La simulación del deporte rey con HyperMotionV y licencias oficiales del fútbol mundial.", "2023-09-29", "eaDev", "ATP", List.of("Deporte", "Multijugador"), false, false),
            new Juego("Dead Space Remake", null, "59.99", "Isaac Clarke intenta sobrevivir al horror biológico a bordo de la USG Ishimura.", "2023-01-27", "eaDev", "+18", List.of("Survival Horror", "Action"), false, false),
            new Juego("Apex Legends", null, "0.00", "Battle royale gratuito donde personajes con habilidades únicas luchan por gloria y fama.", "2019-02-04", "eaDev", "+13", List.of("Shooter", "Multijugador"), false, false),
            new Juego("Mass Effect Legendary Edition", null, "59.99", "Trilogía remasterizada comandando al comandante Shepard para salvar la galaxia.", "2021-05-14", "eaDev", "+18", List.of("RPG", "Shooter", "Aventura"), false, false),
            new Juego("Need for Speed Unbound", null, "69.99", "Carreras callejeras nocturnas, tuning extremo y evasión policial con estilo grafiti.", "2022-12-02", "eaDev", "+13", List.of("Carreras", "Action"), false, false),
            new Juego("Battlefield 1", null, "39.99", "Guerra a gran escala inspirada en los eventos y batallas épicas de la Primera Guerra Mundial.", "2016-10-21", "eaDev", "+18", List.of("Shooter", "Multijugador"), false, false),

            // ---------------- SQUARE ENIX (6) ----------------
            new Juego("Final Fantasy VII Remake", null, "69.99", "Cloud Strife combate a la corporación Shinra en la metrópolis de Midgar.", "2020-04-10", "squareDev", "+13", List.of("RPG", "Action"), false, false),
            new Juego("NieR: Automata", null, "39.99", "Los androides 2B, 9S y A2 luchan para recuperar la Tierra de los alienígenas.", "2017-03-07", "squareDev", "+18", List.of("Action", "RPG"), false, false),
            new Juego("Final Fantasy XVI", null, "69.99", "Clive Rosfield busca venganza en la oscura y bélica tierra de Valisthea.", "2023-06-22", "squareDev", "+18", List.of("Action", "RPG"), false, false),
            new Juego("Kingdom Hearts III", null, "49.99", "Sora, Donald y Goofy viajan por mundos de Disney y Pixar para vencer la oscuridad.", "2019-01-25", "squareDev", "+13", List.of("RPG", "Aventura"), false, false),
            new Juego("Dragon Quest XI S", null, "39.99", "El Luminoso emprende un viaje legendario para salvar al mundo de Erdrea.", "2019-09-27", "squareDev", "+13", List.of("RPG", "Aventura"), false, false),
            new Juego("Tomb Raider Definitive Edition", null, "19.99", "El origen de Lara Croft sobreviviendo a los peligros de una isla mística.", "2014-01-28", "squareDev", "+18", List.of("Action", "Aventura"), false, false),

            // ---------------- EPIC GAMES ----------------
            new Juego("Fortnite", null, "0.00", "El battle royale fenómeno mundial con construcción y eventos en vivo.", "2017-07-21", "epicDev", "+13", List.of("Shooter", "Multijugador"), false, false),
            new Juego("Rocket League", null, "0.00", "Fútbol de alta velocidad impulsado por autos con propulsores.", "2015-07-07", "epicDev", "ATP", List.of("Deporte", "Multijugador"), false, false),
            new Juego("Fall Guys", null, "0.00", "Carreras de obstáculos multitudinarias y alocadas con físicas divertidas.", "2020-08-04", "epicDev", "ATP", List.of("Plataformas", "Multijugador"), false, false),
            
            // ---------------- RIOT GAMES ----------------
            new Juego("League of Legends", null, "0.00", "El MOBA competitivo 5v5 más importante del mundo de los esports.", "2009-10-27", "riotDev", "+13", List.of("Estrategia", "Multijugador"), false, false),
            new Juego("Valorant", null, "0.00", "Shooter táctico 5v5 basado en personajes con habilidades únicas y precisión de tiro.", "2020-06-02", "riotDev", "+16", List.of("Shooter", "Multijugador"), false, false),
            new Juego("Teamfight Tactics", null, "0.00", "Auto-battler estratégico con los campeones y elementos del universo de LoL.", "2019-06-26", "riotDev", "+13", List.of("Estrategia"), false, false),
            
            // ---------------- KONAMI ----------------
            new Juego("eFootball 2024", null, "0.00", "Simulación futbolística gratuita enfocada en el armado de planteles Dream Team.", "2021-09-30", "konamiDev", "ATP", List.of("Deporte", "Multijugador"), false, false),
            new Juego("Yu-Gi-Oh! Master Duel", null, "0.00", "El juego de cartas coleccionables definitivo con las reglas oficiales y duelos globales.", "2022-01-19", "konamiDev", "ATP", List.of("Puzzles", "Estrategia"), false, false),
            
            // ---------------- BLIZZARD ----------------
            new Juego("Overwatch 2", null, "0.00", "Shooter por equipos con héroes divididos en tanques, daño y soporte.", "2022-10-04", "blizzardDev", "+13", List.of("Shooter", "Multijugador"), false, false),
            new Juego("Hearthstone", null, "0.00", "Juego estratégico de cartas ambientado en el universo fantástico de Warcraft.", "2014-03-11", "blizzardDev", "ATP", List.of("Estrategia", "Puzzles"), false, false),
            
            // ---------------- UBISOFT ----------------
            new Juego("Brawlhalla", null, "0.00", "Juego de peleas y plataformas 2D donde leyendas de la historia luchan por gloria.", "2017-10-17", "ubisoftDev", "ATP", List.of("Lucha", "Multijugador"), false, false),
            new Juego("XDefiant", null, "0.00", "Shooter en primera persona competitivo rápido con facciones del universo Ubisoft.", "2024-05-21", "ubisoftDev", "+16", List.of("Shooter", "Multijugador"), false, false),
            
            // ---------------- ELECTRONIC ARTS (Free to Play) ----------------
            new Juego("The Sims 4", null, "0.00", "Crea personas únicas, construye casas de ensueño y controla sus vidas.", "2014-09-02", "eaDev", "+13", List.of("Simulación"), false, false)
    );

    @Override
    public void cargar() {
        Set<String> existentes = gameRepository.findAll().stream()
                .map(GameEntity::getName)
                .collect(Collectors.toSet());

        Map<String, DeveloperEntity> developers = developerRepository.findAll().stream()
                .collect(Collectors.toMap(DeveloperEntity::getUserName, Function.identity(), (a, b) -> a));
        Map<String, AgeRangeEntity> rangos = ageRangeRepository.findAll().stream()
                .collect(Collectors.toMap(AgeRangeEntity::getRangeName, Function.identity(), (a, b) -> a));
        Map<String, GenreEntity> generos = genreRepository.findAll().stream()
                .collect(Collectors.toMap(GenreEntity::getName, Function.identity(), (a, b) -> a));

        int nuevos = 0;
        for (Juego j : JUEGOS) {
            if (existentes.contains(j.nombre())) continue;

            GameEntity juego = new GameEntity();
            juego.setName(j.nombre());
            juego.setFrontPage(j.portada());
            juego.setPrice(new BigDecimal(j.precio()));
            juego.setDescription(j.descripcion());
            juego.setReleaseDate(LocalDate.parse(j.lanzamiento()));
            juego.setDeveloper(obtener(developers, j.developer(), "developer"));
            juego.setAgeRange(obtener(rangos, j.rangoEdad(), "rango de edad"));
            juego.setGenres(j.generos().stream()
                    .map(nombre -> obtener(generos, nombre, "género"))
                    .collect(Collectors.toCollection(ArrayList::new)));
            juego.setStatusBlocked(j.bloqueado());
            juego.setNeedRevision(j.enRevision());
            gameRepository.save(juego);
            nuevos++;
        }

        System.out.println("    Juegos nuevos: " + nuevos);
    }
    // Si una clave está mal escrita en la lista, falla con un mensaje claro
    private static <T> T obtener(Map<String, T> mapa, String clave, String tipo) {
        T valor = mapa.get(clave);
        if (valor == null) {
            throw new IllegalStateException("CargaJuegos: no existe el " + tipo + " '" + clave + "'");
        }
        return valor;
    }
}