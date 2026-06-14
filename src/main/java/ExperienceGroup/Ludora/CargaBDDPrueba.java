package ExperienceGroup.Ludora;

import ExperienceGroup.Ludora.features.ageRange.IAgeRangeRepository;
import ExperienceGroup.Ludora.features.ageRange.domain.AgeRangeEntity;
import ExperienceGroup.Ludora.features.genre.IGenreRepository;
import ExperienceGroup.Ludora.features.genre.domain.GenreEntity;
import ExperienceGroup.Ludora.features.game.IGameRepository;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.client.IClientRepository;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.developer.IDeveloperRepository;
import ExperienceGroup.Ludora.features.developer.domain.DeveloperEntity;
import ExperienceGroup.Ludora.features.review.IReviewRepository;
import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import ExperienceGroup.Ludora.common.utils.Email;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class CargaBDDPrueba {

    @Bean
    CommandLineRunner initCargaPrueba(
            IGenreRepository genreRepository,
            IAgeRangeRepository ageRangeRepository,
            IGameRepository gameRepository,
            IClientRepository clientRepository,
            IDeveloperRepository developerRepository,
            IReviewRepository reviewRepository) {

        return args -> {
            // INTERRUPTOR
            // true  -> Escanea la BD en memoria e inserta SOLO los faltantes
            // false -> Carga desactivada. Si se borra en postman, queda borrado. hasta que esté true nuevamente.
            boolean insertarTodo = false;

            if (!insertarTodo) {
                System.out.println("====== CARGA DESACTIVADA MANUALMENTE ======");
                return;
            }

            System.out.println("====== INICIANDO ESCANEO Y CARGA SELECTIVA DE DATOS ======");

            // RANGOS DE EDAD
            List<AgeRangeEntity> dbRanges = ageRangeRepository.findAll();

            if (dbRanges.stream().noneMatch(r -> r.getRangeName().equals("ATP"))) {
                ageRangeRepository.save(new AgeRangeEntity(null, null, "ATP", 0, "Apta para todo público.", null));
            }
            if (dbRanges.stream().noneMatch(r -> r.getRangeName().equals("+13"))) {
                ageRangeRepository.save(new AgeRangeEntity(null, null, "+13", 13, "Violencia moderada o fantasía.", null));
            }
            if (dbRanges.stream().noneMatch(r -> r.getRangeName().equals("+16"))) {
                ageRangeRepository.save(new AgeRangeEntity(null, null, "+16", 16, "Contenido fuerte o lenguaje explícito.", null));
            }
            if (dbRanges.stream().noneMatch(r -> r.getRangeName().equals("+18"))) {
                ageRangeRepository.save(new AgeRangeEntity(null, null, "+18", 18, "Violencia extrema o madura.", null));
            }

            // Volvemos a leer para asegurar las referencias de los objetos
            dbRanges = ageRangeRepository.findAll();
            AgeRangeEntity atp = dbRanges.stream().filter(r -> r.getRangeName().equals("ATP")).findFirst().orElse(null);
            AgeRangeEntity mas13 = dbRanges.stream().filter(r -> r.getRangeName().equals("+13")).findFirst().orElse(null);
            AgeRangeEntity mas18 = dbRanges.stream().filter(r -> r.getRangeName().equals("+18")).findFirst().orElse(null);
            System.out.println("====== Rangos de edad verificados ======");

            // GÉNEROS
            List<GenreEntity> dbGenres = genreRepository.findAll();

            if (dbGenres.stream().noneMatch(g -> g.getName().equals("Action"))) {
                genreRepository.save(new GenreEntity(null, "Action", "Juegos de combate y reflejos."));
            }
            if (dbGenres.stream().noneMatch(g -> g.getName().equals("RPG"))) {
                genreRepository.save(new GenreEntity(null, "RPG", "Juegos de rol y progresión."));
            }
            if (dbGenres.stream().noneMatch(g -> g.getName().equals("Aventura"))) {
                genreRepository.save(new GenreEntity(null, "Aventura", "Exploración e historia."));
            }
            if (dbGenres.stream().noneMatch(g -> g.getName().equals("Shooter"))) {
                genreRepository.save(new GenreEntity(null, "Shooter", "Acción armada en primera/tercera persona."));
            }
            if (dbGenres.stream().noneMatch(g -> g.getName().equals("Deporte"))) {
                genreRepository.save(new GenreEntity(null, "Deporte", "Simulación deportiva."));
            }
            if (dbGenres.stream().noneMatch(g -> g.getName().equals("Survival Horror"))) {
                genreRepository.save(new GenreEntity(null, "Survival Horror", "Supervivencia y terror."));
            }

            dbGenres = genreRepository.findAll();
            GenreEntity action = dbGenres.stream().filter(g -> g.getName().equals("Action")).findFirst().orElse(null);
            GenreEntity rpg = dbGenres.stream().filter(g -> g.getName().equals("RPG")).findFirst().orElse(null);
            GenreEntity adventure = dbGenres.stream().filter(g -> g.getName().equals("Aventura")).findFirst().orElse(null);
            GenreEntity shooter = dbGenres.stream().filter(g -> g.getName().equals("Shooter")).findFirst().orElse(null);
            GenreEntity sports = dbGenres.stream().filter(g -> g.getName().equals("Deporte")).findFirst().orElse(null);
            GenreEntity terror = dbGenres.stream().filter(g -> g.getName().equals("Survival Horror")).findFirst().orElse(null);
            System.out.println("====== Géneros verificados ======");

            // USUARIOS (Developer y Cliente)
            List<DeveloperEntity> dbDevs = developerRepository.findAll();

            if (dbDevs.stream().noneMatch(d -> d.getUserName().equals("santamonica_dev"))) {
                DeveloperEntity dev1 = new DeveloperEntity();
                dev1.setName("Sony Santa Monica");
                dev1.setLastName("Studio");
                dev1.setUserName("santamonica_dev");
                dev1.setEmail(new Email("dev@santa.com"));
                dev1.setStatusBlocked(false);
                dev1.setCompany("Sony Interactive Entertainment");
                developerRepository.save(dev1);
            }
            if (dbDevs.stream().noneMatch(d -> d.getUserName().equals("rockstar_dev"))) {
                DeveloperEntity dev2 = new DeveloperEntity();
                dev2.setName("Rockstar");
                dev2.setLastName("North");
                dev2.setUserName("rockstar_dev");
                dev2.setEmail(new Email("dev@rockstar.com"));
                dev2.setStatusBlocked(false);
                dev2.setCompany("Rockstar Games");
                developerRepository.save(dev2);
            }
            if (dbDevs.stream().noneMatch(d -> d.getUserName().equals("from_dev"))) {
                DeveloperEntity dev3 = new DeveloperEntity();
                dev3.setName("Hidetaka");
                dev3.setLastName("Miyazaki");
                dev3.setUserName("from_dev");
                dev3.setEmail(new Email("dev@fromsoftware.com"));
                dev3.setStatusBlocked(false);
                dev3.setCompany("FromSoftware");
                developerRepository.save(dev3);
            }
            if (dbDevs.stream().noneMatch(d -> d.getUserName().equals("ea_dev"))) {
                DeveloperEntity dev4 = new DeveloperEntity();
                dev4.setName("EA Electronic");
                dev4.setLastName("Arts");
                dev4.setUserName("ea_dev");
                dev4.setEmail(new Email("dev@ea.com"));
                dev4.setStatusBlocked(false);
                dev4.setCompany("EA Sports");
                developerRepository.save(dev4);
            }

            dbDevs = developerRepository.findAll();
            DeveloperEntity sonyDev = dbDevs.stream().filter(d -> d.getUserName().equals("santamonica_dev")).findFirst().orElse(null);
            DeveloperEntity rockstarDev = dbDevs.stream().filter(d -> d.getUserName().equals("rockstar_dev")).findFirst().orElse(null);
            DeveloperEntity fromDev = dbDevs.stream().filter(d -> d.getUserName().equals("from_dev")).findFirst().orElse(null);
            DeveloperEntity eaDev = dbDevs.stream().filter(d -> d.getUserName().equals("ea_dev")).findFirst().orElse(null);
            System.out.println("====== 4 Developers verificados ======");

            List<ClientEntity> dbClients = clientRepository.findAll();
            if (dbClients.stream().noneMatch(c -> c.getUserName().equals("cosme_juega"))) {
                ClientEntity cl1 = new ClientEntity();
                cl1.setName("Cosme");
                cl1.setLastName("Fulanito");
                cl1.setUserName("cosme_juega");
                cl1.setEmail(new Email("cosme@gmail.com"));
                cl1.setStatusBlocked(false);
                cl1.setPhone(223123456L);
                cl1.setStreet("Av. Colón");
                cl1.setNumberStreet(1234);
                cl1.setBirthDate(LocalDate.parse("1995-04-12"));
                cl1.setPoints(150);
                clientRepository.save(cl1);
            }

            dbClients = clientRepository.findAll();
            ClientEntity clientCosme = dbClients.stream().filter(c -> c.getUserName().equals("cosme_juega")).findFirst().orElse(null);
            System.out.println("====== Cliente verificado ======");

            // 12 JUEGOS
            List<GameEntity> dbGames = gameRepository.findAll();

            // 1. God of War (Sony)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("God of War Ragnarok"))) {
                GameEntity gow = new GameEntity();
                gow.setName("God of War Ragnarok");
                gow.setPrice(new BigDecimal("69.99"));
                gow.setDescription("Kratos y Atreus en el fin del mundo nórdico.");
                gow.setReleaseDate(LocalDate.parse("2022-11-09"));
                gow.setDeveloper(sonyDev);
                gow.setAgeRange(mas18);
                gow.setGenres(List.of(action, adventure));
                gow.setStatusBlocked(false);
                gameRepository.save(gow);
            }

            // 2. Elden Ring (FromSoftware)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Elden Ring"))) {
                GameEntity elden = new GameEntity();
                elden.setName("Elden Ring");
                elden.setPrice(new BigDecimal("59.99"));
                elden.setDescription("Aventura épica en las Tierras Intermedias.");
                elden.setReleaseDate(LocalDate.parse("2022-02-25"));
                elden.setDeveloper(fromDev);
                elden.setAgeRange(mas18);
                elden.setGenres(List.of(action, rpg));
                elden.setStatusBlocked(false);
                gameRepository.save(elden);
            }

            // 3. GTA V (Rockstar)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("GTA V"))) {
                GameEntity gta = new GameEntity();
                gta.setName("GTA V");
                gta.setPrice(new BigDecimal("29.99"));
                gta.setDescription("Crimen y caos en Los Santos.");
                gta.setReleaseDate(LocalDate.parse("2013-09-17"));
                gta.setDeveloper(rockstarDev);
                gta.setAgeRange(mas18);
                gta.setGenres(List.of(action, adventure));
                gta.setStatusBlocked(false);
                gameRepository.save(gta);
            }

            // 4. Spider-Man 2 (Sony)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Spider-Man 2"))) {
                GameEntity spider = new GameEntity();
                spider.setName("Spider-Man 2");
                spider.setPrice(new BigDecimal("69.99"));
                spider.setDescription("Peter Parker y Miles Morales defienden Nueva York.");
                spider.setReleaseDate(LocalDate.parse("2023-10-20"));
                spider.setDeveloper(sonyDev);
                spider.setAgeRange(mas13);
                spider.setGenres(List.of(action, adventure));
                spider.setStatusBlocked(false);
                gameRepository.save(spider);
            }

            // 5. Resident Evil 4 Remake (FromSoftware)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Resident Evil 4 Remake"))) {
                GameEntity re4 = new GameEntity();
                re4.setName("Resident Evil 4 Remake");
                re4.setPrice(new BigDecimal("59.99"));
                re4.setDescription("Leon S. Kennedy rescata a la hija del presidente.");
                re4.setReleaseDate(LocalDate.parse("2023-03-24"));
                re4.setDeveloper(fromDev);
                re4.setAgeRange(mas18);
                re4.setGenres(List.of(action, terror));
                re4.setStatusBlocked(false);
                gameRepository.save(re4);
            }

            // 6. Cyberpunk 2077 (Rockstar)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Cyberpunk 2077"))) {
                GameEntity cp = new GameEntity();
                cp.setName("Cyberpunk 2077");
                cp.setPrice(new BigDecimal("49.99"));
                cp.setDescription("RPG de acción futurista en Night City.");
                cp.setReleaseDate(LocalDate.parse("2020-12-10"));
                cp.setDeveloper(rockstarDev);
                cp.setAgeRange(mas18);
                cp.setGenres(List.of(action, rpg));
                cp.setStatusBlocked(false);
                gameRepository.save(cp);
            }

            // 7. Minecraft (Sony)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Minecraft"))) {
                GameEntity minecraft = new GameEntity();
                minecraft.setName("Minecraft");
                minecraft.setPrice(new BigDecimal("19.99"));
                minecraft.setDescription("Construcción y supervivencia libre.");
                minecraft.setReleaseDate(LocalDate.parse("2011-11-18"));
                minecraft.setDeveloper(sonyDev);
                minecraft.setAgeRange(atp);
                minecraft.setGenres(List.of(adventure));
                minecraft.setStatusBlocked(false);
                gameRepository.save(minecraft);
            }

            // 8. Red Dead Redemption 2 (Rockstar)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Red Dead Redemption 2"))) {
                GameEntity rdr2 = new GameEntity();
                rdr2.setName("Red Dead Redemption 2");
                rdr2.setPrice(new BigDecimal("39.99"));
                rdr2.setDescription("La caída de la banda de Arthur Morgan.");
                rdr2.setReleaseDate(LocalDate.parse("2018-10-26"));
                rdr2.setDeveloper(rockstarDev);
                rdr2.setAgeRange(mas18);
                rdr2.setGenres(List.of(action, adventure));
                rdr2.setStatusBlocked(false);
                gameRepository.save(rdr2);
            }

            // 9. EA Sports FC 26 (EA Sports)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("EA Sports FC 26"))) {
                GameEntity fc26 = new GameEntity();
                fc26.setName("EA Sports FC 26");
                fc26.setPrice(new BigDecimal("69.99"));
                fc26.setDescription("El simulador de fútbol definitivo.");
                fc26.setReleaseDate(LocalDate.parse("2025-09-26"));
                fc26.setDeveloper(eaDev);
                fc26.setAgeRange(atp);
                fc26.setGenres(List.of(sports));
                fc26.setStatusBlocked(false);
                gameRepository.save(fc26);
            }

            // 10. The Last of Us Part I (Sony)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("The Last of Us Part I"))) {
                GameEntity tlou = new GameEntity();
                tlou.setName("The Last of Us Part I");
                tlou.setPrice(new BigDecimal("59.99"));
                tlou.setDescription("Historia cruda de supervivencia y lazos humanos.");
                tlou.setReleaseDate(LocalDate.parse("2022-09-02"));
                tlou.setDeveloper(sonyDev);
                tlou.setAgeRange(mas18);
                tlou.setGenres(List.of(action, adventure));
                tlou.setStatusBlocked(false);
                gameRepository.save(tlou);
            }

            // 11. Hogwarts Legacy (EA Sports)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Hogwarts Legacy"))) {
                GameEntity hogwarts = new GameEntity();
                hogwarts.setName("Hogwarts Legacy");
                hogwarts.setPrice(new BigDecimal("49.99"));
                hogwarts.setDescription("Viví tu propia historia en el mundo mágico.");
                hogwarts.setReleaseDate(LocalDate.parse("2023-02-10"));
                hogwarts.setDeveloper(eaDev);
                hogwarts.setAgeRange(mas13);
                hogwarts.setGenres(List.of(rpg, adventure));
                hogwarts.setStatusBlocked(false);
                gameRepository.save(hogwarts);
            }

            // 12. DOOM Eternal (Rockstar)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("DOOM Eternal"))) {
                GameEntity doom = new GameEntity();
                doom.setName("DOOM Eternal");
                doom.setPrice(new BigDecimal("39.99"));
                doom.setDescription("Matá demonios a un ritmo frenético.");
                doom.setReleaseDate(LocalDate.parse("2020-03-20"));
                doom.setDeveloper(rockstarDev);
                doom.setAgeRange(mas18);
                doom.setGenres(List.of(action, shooter));
                doom.setStatusBlocked(false);
                gameRepository.save(doom);
            }

            System.out.println("====== Catálogo de juegos verificado y cargado ======");

            // REVIEWS
            if (clientCosme != null) {
                // Volvemos a leer los juegos finales de la BD para engancharlos a las reviews
                dbGames = gameRepository.findAll();
                List<ReviewEntity> dbReviews = reviewRepository.findAll();

                // Review 1: God of War
                GameEntity dbGow = dbGames.stream().filter(g -> g.getName().equals("God of War Ragnarok")).findFirst().orElse(null);
                if (dbGow != null && dbReviews.stream().noneMatch(r -> r.getGame().getId().equals(dbGow.getId()) && r.getClient().getId().equals(clientCosme.getId()))) {
                    ReviewEntity r1 = new ReviewEntity();
                    r1.setRating(5);
                    r1.setComment("God of War es una locura, el final me hizo lagrimear.");
                    r1.setDate(LocalDateTime.now().minusDays(2));
                    r1.setGame(dbGow);
                    r1.setClient(clientCosme);
                    reviewRepository.save(r1);
                }

                // Review 2: Elden Ring
                GameEntity dbElden = dbGames.stream().filter(g -> g.getName().equals("Elden Ring")).findFirst().orElse(null);
                if (dbElden != null && dbReviews.stream().noneMatch(r -> r.getGame().getId().equals(dbElden.getId()) && r.getClient().getId().equals(clientCosme.getId()))) {
                    ReviewEntity r2 = new ReviewEntity();
                    r2.setRating(5);
                    r2.setComment("Miyazaki lo hizo de nuevo. El mejor mundo abierto de la historia.");
                    r2.setDate(LocalDateTime.now().minusDays(1));
                    r2.setGame(dbElden);
                    r2.setClient(clientCosme);
                    reviewRepository.save(r2);
                }

                // Review 3: GTA V
                GameEntity dbGta = dbGames.stream().filter(g -> g.getName().equals("GTA V")).findFirst().orElse(null);
                if (dbGta != null && dbReviews.stream().noneMatch(r -> r.getGame().getId().equals(dbGta.getId()) && r.getClient().getId().equals(clientCosme.getId()))) {
                    ReviewEntity r3 = new ReviewEntity();
                    r3.setRating(3);
                    r3.setComment("El juego base está bien, pero el online está lleno de hackers.");
                    r3.setDate(LocalDateTime.now().minusHours(5));
                    r3.setGame(dbGta);
                    r3.setClient(clientCosme);
                    reviewRepository.save(r3);
                }

                // Review 4: EA Sports FC 26
                GameEntity dbFc26 = dbGames.stream().filter(g -> g.getName().equals("EA Sports FC 26")).findFirst().orElse(null);
                if (dbFc26 != null && dbReviews.stream().noneMatch(r -> r.getGame().getId().equals(dbFc26.getId()) && r.getClient().getId().equals(clientCosme.getId()))) {
                    ReviewEntity r4 = new ReviewEntity();
                    r4.setRating(1);
                    r4.setComment("Misma jugabilidad de todos los años y los servidores andan para el revés.");
                    r4.setDate(LocalDateTime.now());
                    r4.setGame(dbFc26);
                    r4.setClient(clientCosme);
                    reviewRepository.save(r4);
                }

                System.out.println("====== 4 Reviews verificadas y cargadas ======");
            }
        };
    }
}