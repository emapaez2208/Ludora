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

            // ---------------- ROCKSTAR GAMES ----------------
            new Juego("GTA V", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367925/gta5.jpg", "29.99", "Crimen, asaltos y caos en la ciudad de Los Santos.", "2013-09-17", "francoDev", "+18", List.of("Action", "Aventura"), false, false),
            new Juego("Red Dead Redemption 2", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367935/rdr2.jpg", "59.99", "La épica y cruda caída de la banda de forajidos de Arthur Morgan.", "2018-10-26", "francoDev", "+18", List.of("Action", "Aventura"), false, false),
            new Juego("GTA IV", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367923/gta4.jpg", "14.99", "Niko Bellic busca escapar de su pasado en Liberty City.", "2008-04-29", "francoDev", "+18", List.of("Action", "Aventura"), false, false),
            new Juego("L.A. Noire", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367927/lanoire.jpg", "19.99", "Investigaciones policiales y conspiraciones criminales en Los Ángeles de 1940.", "2011-05-17", "francoDev", "+18", List.of("Action", "Aventura"), false, false),
            new Juego("Max Payne 3", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367931/maxpayne3.jpg", "19.99", "Acción armada y tiempo bala en los suburbios de São Paulo.", "2012-05-15", "francoDev", "+18", List.of("Action", "Shooter"), false, false),

            // ---------------- NINTENDO ----------------
            new Juego("Super Mario Odyssey", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367944/super-mario-odyssey.avif", "59.99", "Aventura en 3D junto a Cappy para rescatar a la princesa Peach.", "2017-10-27", "shigeruDev", "ATP", List.of("Aventura"), false, false),
            new Juego("The Legend of Zelda: BotW", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367947/the-legend-of-zelda-breath-of-the-wild.jpg", "59.99", "Explorá el vasto reino de Hyrule en un mundo abierto sin límites.", "2017-03-03", "shigeruDev", "+13", List.of("Aventura", "RPG"), false, false),
            new Juego("Mario Kart 8 Deluxe", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367929/martio-kart-8.avif", "49.99", "Carreras vertiginosas desafiando la gravedad con tus personajes favoritos.", "2017-04-28", "shigeruDev", "ATP", List.of("Deporte"), false, false),
            new Juego("Animal Crossing: New Horizons", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367906/animal-crossing.jpg", "59.99", "Diseñá, construí y gestioná tu propia comunidad en una isla desierta.", "2020-03-20", "shigeruDev", "ATP", List.of("Aventura"), false, false),
            new Juego("Super Smash Bros. Ultimate", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367945/super-smash-bros.avif", "59.99", "El mayor crossover de peleas de la historia de los videojuegos.", "2018-12-07", "shigeruDev", "+13", List.of("Action"), false, false),

            // ---------------- CD PROJEKT RED ----------------
            new Juego("The Witcher 3: Wild Hunt", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367900/the-witcher3.jpg", "39.99", "Geralt de Rivia busca a la Niña de la Profecía en un continente en guerra.", "2015-05-18", "cdpDev", "+18", List.of("RPG", "Aventura"), false, false),
            new Juego("Cyberpunk 2077", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367910/cyberpunk.jpg", "59.99", "Un mercenario cibernético busca un implante que otorga la inmortalidad en Night City.", "2020-12-10", "cdpDev", "+18", List.of("RPG", "Shooter", "Action"), false, false),
            new Juego("Phantom Liberty", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367912/cyberpunk-phantom-liberty.jpg", "29.99", "Aventura de suspenso y espionaje en el distrito más peligroso de Night City: Dogtown.", "2023-09-26", "cdpDev", "+18", List.of("RPG", "Action"), false, false),
            new Juego("The Witcher 2", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367951/the-witcher2.jpg", "19.99", "Intrigas políticas y regicidio en los Reinos del Norte con Geralt.", "2011-05-17", "cdpDev", "+18", List.of("RPG", "Action"), false, false),
            new Juego("The Witcher 1", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367949/the-witcher1.jpg", "9.99", "El inicio de la leyenda de Geralt de Rivia en Vizima.", "2008-09-16", "cdpDev", "+18", List.of("RPG"), false, false),

            // ---------------- FROMSOFTWARE ----------------
            new Juego("Elden Ring", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367921/elden-ring.jpg", "59.99", "Levántate, Sinluz, y déjate guiar por la gracia hacia las Tierras Intermedias.", "2022-02-25", "hidetakaDev", "+18", List.of("Action", "RPG"), false, false),
            new Juego("Sekiro: Shadows Die Twice", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/814380/header.jpg", "59.99", "Venganza, sigilo y duelos a muerte con katanas en el Japón feudal.", "2019-03-22", "hidetakaDev", "+18", List.of("Action", "Aventura"), false, false),
            new Juego("Dark Souls III", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367915/dark-souls3.jpg", "59.99", "Las cenizas buscan un Señor en un mundo en descomposición total.", "2016-04-12", "hidetakaDev", "+18", List.of("Action", "RPG"), true, false),
            new Juego("Dark Souls Remastered", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367913/darksouls1.jpg", "39.99", "Y entonces hubo fuego. Vuelve a encender la llama en Lordran.", "2018-05-24", "hidetakaDev", "+18", List.of("Action", "RPG"), true, true),
            new Juego("Armored Core VI", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367908/armored-gore4.jpg", "59.99", "Combates vertiginosos de mechas gigantes personalizables en el planeta Rubicón 3.", "2023-08-25", "hidetakaDev", "+13", List.of("Action", "Shooter"), false, false),

            // ---------------- CAPCOM ----------------
            new Juego("Resident Evil 4 Remake", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367938/re4.jpg", "59.99", "Leon S. Kennedy rescata a la hija del presidente en una siniestra aldea europea.", "2023-03-24", "capcomDev", "+18", List.of("Action", "Survival Horror"), false, false),
            new Juego("Resident Evil 2 Remake", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/883710/header.jpg", "39.99", "Sobreviví al brote zombi en la comisaría de Raccoon City con Leon y Claire.", "2019-01-25", "capcomDev", "+18", List.of("Survival Horror", "Action"), false, false),
            new Juego("Resident Evil Village", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367936/re2.jpg", "39.99", "Ethan Winters busca a su hija secuestrada enfrentando los horrores del castillo Dimitrescu.", "2021-05-07", "capcomDev", "+18", List.of("Survival Horror", "Action"), false, false),
            new Juego("Devil May Cry 5", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367917/devilmaycray5.jpg", "29.99", "Dante, Nero y V cazan demonios con el combate hack and slash más estilizado.", "2019-03-08", "capcomDev", "+18", List.of("Action"), false, false),
            new Juego("Monster Hunter: World", "https://res.cloudinary.com/cbttd5ya/image/upload/v1793367933/monsterhunter.jpg", "29.99", "Caza bestias colosales en ecosistemas vivos para forjar armaduras y armas épicas.", "2018-08-09", "capcomDev", "+13", List.of("Action", "RPG"), false, false)
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