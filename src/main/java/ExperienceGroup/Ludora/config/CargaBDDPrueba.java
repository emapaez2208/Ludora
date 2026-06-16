package ExperienceGroup.Ludora.config;

import ExperienceGroup.Ludora.auth.credentials.CredentialsEntity;
import ExperienceGroup.Ludora.auth.credentials.CredentialsRepository;
import ExperienceGroup.Ludora.auth.permissions.RoleEntity;
import ExperienceGroup.Ludora.auth.permissions.RoleRepository;
import ExperienceGroup.Ludora.auth.permissions.RolesEnum;
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
import org.springframework.security.crypto.password.PasswordEncoder;

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
            IReviewRepository reviewRepository,
            CredentialsRepository credentialsRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("CREDENCIALES PARA PRUEBAS EN POSTMAN");
            System.out.println("=".repeat(70));
            System.out.println("   [ADMIN]");
            System.out.println("   Username: admin123      | Password: password123");
            System.out.println("   " + "-".repeat(60));
            System.out.println("   [DEVELOPERS]");
            System.out.println("   Username: manuelDev     | Password: Manu123     (Sony)");
            System.out.println("   Username: francoDev     | Password: Franco123   (Rockstar)");
            System.out.println("   Username: shigeruDev    | Password: Shigeru123  (Nintendo)");
            System.out.println("   " + "-".repeat(60));
            System.out.println("   [CLIENTES]");
            System.out.println("   Username: cosmejuega    | Password: Cosme123    (150 pts)");
            System.out.println("   Username: estebanquito  | Password: Esteban123  (99000 pts)");
            System.out.println("   Username: alanbrito     | Password: Alan123     (5 pts)");

            // 1. Con la base de datos vacía, arrancar programa con interruptor en false (levanta las tablas)
            // 2. Se vuelve a lanzar el programa, con el interruptor en true


            // INTERRUPTOR
            // true  -> Escanea la BD en memoria e inserta SOLO los faltantes
            // false -> Carga desactivada. Si se borra en postman, queda borrado. hasta que esté true nuevamente.
            boolean insertarTodo = true;

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

            // ==========================================================
            // USUARIOS DEVELOPERS
            // ==========================================================
            List<DeveloperEntity> dbDevs = developerRepository.findAll();

            // Buscamos el rol de DEVELOPER oficial una sola vez para los tres
            RoleEntity devRole = roleRepository.findByRole(RolesEnum.ROLE_DEVELOPER)
                    .orElseThrow(() -> new RuntimeException("Error: No se encontró el Rol DEVELOPER en las tablas de permisos."));

            //SANTA MONICA STUDIOS
            if (dbDevs.stream().noneMatch(d -> d.getUserName().equals("manuelDev"))) {
                DeveloperEntity dev1 = new DeveloperEntity();
                dev1.setExternalId(java.util.UUID.randomUUID());
                dev1.setName("Manuel");
                dev1.setLastName("Gonzales");
                dev1.setUserName("manuelDev");
                dev1.setEmail(new Email("manu@santamonica.com"));
                dev1.setStatusBlocked(false);
                dev1.setCompany("Santa Monica Studios");
                dev1 = developerRepository.save(dev1);

                CredentialsEntity creds1 = CredentialsEntity.builder()
                        .externalId(dev1.getExternalId())
                        .username(dev1.getUserName())
                        .password(passwordEncoder.encode("Manu123"))
                        .enabled(true)
                        .accountNonLocked(true)
                        .user(dev1)
                        .roles(java.util.Set.of(devRole))
                        .build();
                credentialsRepository.save(creds1);
                System.out.println("====== Developer de Santa Monica cargado con éxito ======");
            }

            // ROCKSTAR GAMES
            if (dbDevs.stream().noneMatch(d -> d.getUserName().equals("francoDev"))) {
                DeveloperEntity dev2 = new DeveloperEntity();
                dev2.setExternalId(java.util.UUID.randomUUID());
                dev2.setName("Franco");
                dev2.setLastName("Rossi");
                dev2.setUserName("francoDev");
                dev2.setEmail(new Email("franco@rockstargames.com"));
                dev2.setStatusBlocked(false);
                dev2.setCompany("Rockstar Games");
                dev2 = developerRepository.save(dev2);

                CredentialsEntity creds2 = CredentialsEntity.builder()
                        .externalId(dev2.getExternalId())
                        .username(dev2.getUserName())
                        .password(passwordEncoder.encode("Franco123"))
                        .enabled(true)
                        .accountNonLocked(true)
                        .user(dev2)
                        .roles(java.util.Set.of(devRole))
                        .build();
                credentialsRepository.save(creds2);
                System.out.println("====== Developer de Rockstar cargado con éxito ======");
            }

            // NINTENDO
            if (dbDevs.stream().noneMatch(d -> d.getUserName().equals("shigeruDev"))) {
                DeveloperEntity dev3 = new DeveloperEntity();
                dev3.setExternalId(java.util.UUID.randomUUID());
                dev3.setName("Shigeru");
                dev3.setLastName("Miyamoto");
                dev3.setUserName("shigeruDev");
                dev3.setEmail(new Email("miyamoto@nintendo.com"));
                dev3.setStatusBlocked(false);
                dev3.setCompany("Nintendo");
                dev3 = developerRepository.save(dev3);

                CredentialsEntity creds3 = CredentialsEntity.builder()
                        .externalId(dev3.getExternalId())
                        .username(dev3.getUserName())
                        .password(passwordEncoder.encode("Shigeru123"))
                        .enabled(true)
                        .accountNonLocked(true)
                        .user(dev3)
                        .roles(java.util.Set.of(devRole))
                        .build();
                credentialsRepository.save(creds3);
                System.out.println("====== Developer shigeruDev (Nintendo) cargado con éxito ======");
            }

            dbDevs = developerRepository.findAll();
            DeveloperEntity manuelDev = dbDevs.stream().filter(d -> d.getUserName().equals("manuelDev")).findFirst().orElse(null);
            DeveloperEntity francoDev = dbDevs.stream().filter(d -> d.getUserName().equals("francoDev")).findFirst().orElse(null);
            DeveloperEntity shigeruDev = dbDevs.stream().filter(d -> d.getUserName().equals("shigeruDev")).findFirst().orElse(null);
            System.out.println("====== 3 Entidades de Developers listas para el catálogo ======");

            // --- CLIENTE ---

            List<ClientEntity> dbClients = clientRepository.findAll();

            RoleEntity clientRole = roleRepository.findByRole(RolesEnum.ROLE_CLIENT)
                    .orElseThrow(() -> new RuntimeException("Error: No se encontró el Rol CLIENT en las tablas de permisos."));

            //Cliente 1: Cosme Fulanito
            if (dbClients.stream().noneMatch(c -> c.getUserName().equals("cosmejuega"))) {
                ClientEntity cl1 = new ClientEntity();
                cl1.setExternalId(java.util.UUID.randomUUID());
                cl1.setName("Cosme");
                cl1.setLastName("Fulanito");
                cl1.setUserName("cosmejuega");
                cl1.setEmail(new Email("cosme@gmail.com"));
                cl1.setStatusBlocked(false);
                cl1.setPhone(223123456L);
                cl1.setStreet("Av. Colón");
                cl1.setNumberStreet(1234);
                cl1.setBirthDate(LocalDate.parse("1995-04-12"));
                cl1.setPoints(150);
                cl1 = clientRepository.save(cl1);

                CredentialsEntity credsCliente = CredentialsEntity.builder()
                        .externalId(cl1.getExternalId())
                        .username(cl1.getUserName())
                        .password(passwordEncoder.encode("Cosme123"))
                        .enabled(true)
                        .accountNonLocked(true)
                        .user(cl1)
                        .roles(java.util.Set.of(clientRole))
                        .build();
                credentialsRepository.save(credsCliente);
                System.out.println("====== Cliente cosme_juega y sus credenciales de seguridad cargados ======");
            }

            //Cliente 2: Esteban Quito
            if (dbClients.stream().noneMatch(c -> c.getUserName().equals("estebanquito"))) {
                ClientEntity cl2 = new ClientEntity();
                cl2.setExternalId(java.util.UUID.randomUUID());
                cl2.setName("Esteban");
                cl2.setLastName("Quito");
                cl2.setUserName("estebanquito");
                cl2.setEmail(new Email("esteban@gmail.com"));
                cl2.setStatusBlocked(false);
                cl2.setPhone(223654321L);
                cl2.setStreet("San Martín");
                cl2.setNumberStreet(2580);
                cl2.setBirthDate(LocalDate.parse("1999-01-15"));
                cl2.setPoints(99000);
                cl2 = clientRepository.save(cl2);

                CredentialsEntity credsCliente2 = CredentialsEntity.builder()
                        .externalId(cl2.getExternalId())
                        .username(cl2.getUserName())
                        .password(passwordEncoder.encode("Esteban123"))
                        .enabled(true)
                        .accountNonLocked(true)
                        .user(cl2)
                        .roles(java.util.Set.of(clientRole))
                        .build();
                credentialsRepository.save(credsCliente2);
                System.out.println("====== Cliente estebanquito cargado con éxito ======");
            }
            //Cliente 3: Alan Brito
            if (dbClients.stream().noneMatch(c -> c.getUserName().equals("alanbrito"))) {
                ClientEntity cl3 = new ClientEntity();
                cl3.setExternalId(java.util.UUID.randomUUID());
                cl3.setName("Alan");
                cl3.setLastName("Brito");
                cl3.setUserName("alanbrito");
                cl3.setEmail(new Email("alan@gmail.com"));
                cl3.setStatusBlocked(false);
                cl3.setPhone(223987654L);
                cl3.setStreet("Av. Independencia");
                cl3.setNumberStreet(4321);
                cl3.setBirthDate(LocalDate.parse("2002-06-20"));
                cl3.setPoints(5);
                cl3 = clientRepository.save(cl3);

                CredentialsEntity credsCliente3 = CredentialsEntity.builder()
                        .externalId(cl3.getExternalId())
                        .username(cl3.getUserName())
                        .password(passwordEncoder.encode("Alan123"))
                        .enabled(true)
                        .accountNonLocked(true)
                        .user(cl3)
                        .roles(java.util.Set.of(clientRole))
                        .build();
                credentialsRepository.save(credsCliente3);
                System.out.println("====== Cliente alanbrito cargado con éxito ======");
            }

            // Recuperamos las 3 instancias globales limpias de la BDD
            dbClients = clientRepository.findAll();
            ClientEntity clientCosme = dbClients.stream().filter(c -> c.getUserName().equals("cosmejuega")).findFirst().orElse(null);
            ClientEntity clientEsteban = dbClients.stream().filter(c -> c.getUserName().equals("estebanquito")).findFirst().orElse(null);
            ClientEntity clientAlan = dbClients.stream().filter(c -> c.getUserName().equals("alanbrito")).findFirst().orElse(null);
            System.out.println("====== 3 Clientes verificados y listos para comentar ======");

            // ==========================================================
            // SECCION JUEGOS
            // ==========================================================
            List<GameEntity> dbGames = gameRepository.findAll();

            // ----------------------------------------------------------
            // JUEGOS DE SANTA MONICA STUDIOS
            // ----------------------------------------------------------

            // Juego 1: God of War Ragnarok
            if (dbGames.stream().noneMatch(g -> g.getName().equals("God of War Ragnarok"))) {
                GameEntity gow = new GameEntity();
                gow.setName("God of War Ragnarok");
                gow.setPrice(new BigDecimal("69.99"));
                gow.setDescription("Kratos y Atreus en el fin del mundo nórdico.");
                gow.setReleaseDate(LocalDate.parse("2022-11-09"));
                gow.setDeveloper(manuelDev);
                gow.setAgeRange(mas18);
                gow.setGenres(List.of(action, adventure));
                gow.setStatusBlocked(false);
                gameRepository.save(gow);
            }

            // Juego 2: God of War (2018)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("God of War"))) {
                GameEntity gow2018 = new GameEntity();
                gow2018.setName("God of War");
                gow2018.setPrice(new BigDecimal("19.99"));
                gow2018.setDescription("El reinicio de la saga nórdica en tierras salvajes.");
                gow2018.setReleaseDate(LocalDate.parse("2018-04-20"));
                gow2018.setDeveloper(manuelDev);
                gow2018.setAgeRange(mas18);
                gow2018.setGenres(List.of(action, adventure));
                gow2018.setStatusBlocked(false);
                gameRepository.save(gow2018);
            }

            // Juego 3: Marvel's Spider-Man 2
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Spider-Man 2"))) {
                GameEntity spider = new GameEntity();
                spider.setName("Spider-Man 2");
                spider.setPrice(new BigDecimal("69.99"));
                spider.setDescription("Peter Parker y Miles Morales defienden Nueva York.");
                spider.setReleaseDate(LocalDate.parse("2023-10-20"));
                spider.setDeveloper(manuelDev);
                spider.setAgeRange(mas13);
                spider.setGenres(List.of(action, adventure));
                spider.setStatusBlocked(false);
                gameRepository.save(spider);
            }

            // Juego 4: The Last of Us Part I
            if (dbGames.stream().noneMatch(g -> g.getName().equals("The Last of Us Part I"))) {
                GameEntity tlou = new GameEntity();
                tlou.setName("The Last of Us Part I");
                tlou.setPrice(new BigDecimal("59.99"));
                tlou.setDescription("Historia cruda de supervivencia y lazos humanos mutuos.");
                tlou.setReleaseDate(LocalDate.parse("2022-09-02"));
                tlou.setDeveloper(manuelDev);
                tlou.setAgeRange(mas18);
                tlou.setGenres(List.of(action, adventure));
                tlou.setStatusBlocked(false);
                gameRepository.save(tlou);
            }

            // Juego 5: Uncharted: Legacy of Thieves
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Uncharted: Legacy of Thieves"))) {
                GameEntity unch = new GameEntity();
                unch.setName("Uncharted: Legacy of Thieves");
                unch.setPrice(new BigDecimal("49.99"));
                unch.setDescription("Búsqueda de tesoros históricos con Nathan Drake y Chloe Frazer.");
                unch.setReleaseDate(LocalDate.parse("2022-01-28"));
                unch.setDeveloper(manuelDev);
                unch.setAgeRange(mas13);
                unch.setGenres(List.of(action, adventure));
                unch.setStatusBlocked(false);
                gameRepository.save(unch);
            }

            // ----------------------------------------------------------
            // JUEGOS DE ROCKSTAR GAMES
            // ----------------------------------------------------------

            // Juego 6: GTA V
            if (dbGames.stream().noneMatch(g -> g.getName().equals("GTA V"))) {
                GameEntity gta = new GameEntity();
                gta.setName("GTA V");
                gta.setPrice(new BigDecimal("29.99"));
                gta.setDescription("Crimen, asaltos y caos en la ciudad de Los Santos.");
                gta.setReleaseDate(LocalDate.parse("2013-09-17"));
                gta.setDeveloper(francoDev);
                gta.setAgeRange(mas18);
                gta.setGenres(List.of(action, adventure));
                gta.setStatusBlocked(false);
                gameRepository.save(gta);
            }

            // Juego 7: Red Dead Redemption 2
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Red Dead Redemption 2"))) {
                GameEntity rdr2 = new GameEntity();
                rdr2.setName("Red Dead Redemption 2");
                rdr2.setPrice(new BigDecimal("59.99"));
                rdr2.setDescription("La épica y cruda caída de la banda de forajidos de Arthur Morgan.");
                rdr2.setReleaseDate(LocalDate.parse("2018-10-26"));
                rdr2.setDeveloper(francoDev);
                rdr2.setAgeRange(mas18);
                rdr2.setGenres(List.of(action, adventure));
                rdr2.setStatusBlocked(false);
                gameRepository.save(rdr2);
            }

            // Juego 8: GTA IV
            if (dbGames.stream().noneMatch(g -> g.getName().equals("GTA IV"))) {
                GameEntity gta4 = new GameEntity();
                gta4.setName("GTA IV");
                gta4.setPrice(new BigDecimal("14.99"));
                gta4.setDescription("Niko Bellic busca escapar de su pasado en Liberty City.");
                gta4.setReleaseDate(LocalDate.parse("2008-04-29"));
                gta4.setDeveloper(francoDev);
                gta4.setAgeRange(mas18);
                gta4.setGenres(List.of(action, adventure));
                gta4.setStatusBlocked(false);
                gameRepository.save(gta4);
            }

            // Juego 9: L.A. Noire
            if (dbGames.stream().noneMatch(g -> g.getName().equals("L.A. Noire"))) {
                GameEntity lanoire = new GameEntity();
                lanoire.setName("L.A. Noire");
                lanoire.setPrice(new BigDecimal("19.99"));
                lanoire.setDescription("Investigaciones policiales y conspiraciones criminales en Los Ángeles de 1940.");
                lanoire.setReleaseDate(LocalDate.parse("2011-05-17"));
                lanoire.setDeveloper(francoDev);
                lanoire.setAgeRange(mas18);
                lanoire.setGenres(List.of(action, adventure));
                lanoire.setStatusBlocked(false);
                gameRepository.save(lanoire);
            }

            // Juego 10: Max Payne 3
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Max Payne 3"))) {
                GameEntity max = new GameEntity();
                max.setName("Max Payne 3");
                max.setPrice(new BigDecimal("19.99"));
                max.setDescription("Acción armada y tiempo bala en los suburbios de São Paulo.");
                max.setReleaseDate(LocalDate.parse("2012-05-15"));
                max.setDeveloper(francoDev);
                max.setAgeRange(mas18);
                max.setGenres(List.of(action, shooter));
                max.setStatusBlocked(false);
                gameRepository.save(max);
            }

            // ----------------------------------------------------------
            // JUEGOS DE NINTENDO (shigeruDev)
            // ----------------------------------------------------------

            // Juego 11: Super Mario Odyssey
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Super Mario Odyssey"))) {
                GameEntity mario = new GameEntity();
                mario.setName("Super Mario Odyssey");
                mario.setPrice(new BigDecimal("59.99"));
                mario.setDescription("Aventura en 3D junto a Cappy para rescatar a la princesa Peach.");
                mario.setReleaseDate(LocalDate.parse("2017-10-27"));
                mario.setDeveloper(shigeruDev);
                mario.setAgeRange(atp);
                mario.setGenres(List.of(adventure));
                mario.setStatusBlocked(false);
                gameRepository.save(mario);
            }

            // Juego 12: The Legend of Zelda: Breath of the Wild
            if (dbGames.stream().noneMatch(g -> g.getName().equals("The Legend of Zelda: BotW"))) {
                GameEntity zelda = new GameEntity();
                zelda.setName("The Legend of Zelda: BotW");
                zelda.setPrice(new BigDecimal("59.99"));
                zelda.setDescription("Explorá el vasto reino de Hyrule en un mundo abierto sin límites.");
                zelda.setReleaseDate(LocalDate.parse("2017-03-03"));
                zelda.setDeveloper(shigeruDev);
                zelda.setAgeRange(mas13);
                zelda.setGenres(List.of(adventure, rpg));
                zelda.setStatusBlocked(false);
                gameRepository.save(zelda);
            }

            // Juego 13: Mario Kart 8 Deluxe
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Mario Kart 8 Deluxe"))) {
                GameEntity mk8 = new GameEntity();
                mk8.setName("Mario Kart 8 Deluxe");
                mk8.setPrice(new BigDecimal("49.99"));
                mk8.setDescription("Carreras vertiginosas desafiando la gravedad con tus personajes favoritos.");
                mk8.setReleaseDate(LocalDate.parse("2017-04-28"));
                mk8.setDeveloper(shigeruDev);
                mk8.setAgeRange(atp);
                mk8.setGenres(List.of(sports));
                mk8.setStatusBlocked(false);
                gameRepository.save(mk8);
            }

            // Juego 14: Animal Crossing: New Horizons
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Animal Crossing: New Horizons"))) {
                GameEntity ac = new GameEntity();
                ac.setName("Animal Crossing: New Horizons");
                ac.setPrice(new BigDecimal("59.99"));
                ac.setDescription("Diseñá, construí y gestioná tu propia comunidad en una isla desierta.");
                ac.setReleaseDate(LocalDate.parse("2020-03-20"));
                ac.setDeveloper(shigeruDev);
                ac.setAgeRange(atp);
                ac.setGenres(List.of(adventure));
                ac.setStatusBlocked(false);
                gameRepository.save(ac);
            }

            // Juego 15: Super Smash Bros. Ultimate
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Super Smash Bros. Ultimate"))) {
                GameEntity smash = new GameEntity();
                smash.setName("Super Smash Bros. Ultimate");
                smash.setPrice(new BigDecimal("59.99"));
                smash.setDescription("El mayor crossover de peleas de la historia de los videojuegos.");
                smash.setReleaseDate(LocalDate.parse("2018-12-07"));
                smash.setDeveloper(shigeruDev);
                smash.setAgeRange(mas13);
                smash.setGenres(List.of(action));
                smash.setStatusBlocked(false);
                gameRepository.save(smash);
            }
            System.out.println("====== Catálogo de juegos verificado y cargado ======");

            // ==========================================================
            // REVIEWS
            // ==========================================================
            if (clientCosme != null && clientEsteban != null && clientAlan != null) {
                dbGames = gameRepository.findAll();
                List<ReviewEntity> dbReviews = reviewRepository.findAll();

                // ------------------------------------------------------
                // REVIEWS EN: God of War Ragnarok
                // ------------------------------------------------------
                GameEntity dbGow = dbGames.stream().filter(g -> g.getName().equals("God of War Ragnarok")).findFirst().orElse(null);
                if (dbGow != null) {
                    if (dbReviews.stream().noneMatch(r -> r.getGame().getId().equals(dbGow.getId()) && r.getClient().getId().equals(clientCosme.getId()))) {
                        ReviewEntity r1 = new ReviewEntity();
                        r1.setRating(5);
                        r1.setComment("God of War es una locura, el final me hizo lagrimear.");
                        r1.setDate(LocalDateTime.now().minusDays(2));
                        r1.setGame(dbGow);
                        r1.setClient(clientCosme);
                        reviewRepository.save(r1);
                    }
                    if (dbReviews.stream().noneMatch(r -> r.getGame().getId().equals(dbGow.getId()) && r.getClient().getId().equals(clientAlan.getId()))) {
                        ReviewEntity r2 = new ReviewEntity();
                        r2.setRating(3);
                        r2.setComment("Visualmente es hermoso, pero tiene demasiado diálogo para mi gusto.");
                        r2.setDate(LocalDateTime.now().minusDays(5));
                        r2.setGame(dbGow);
                        r2.setClient(clientAlan);
                        reviewRepository.save(r2);
                    }
                }

                // ------------------------------------------------------
                // REVIEWS EN: GTA V
                // ------------------------------------------------------
                GameEntity dbGta = dbGames.stream().filter(g -> g.getName().equals("GTA V")).findFirst().orElse(null);
                if (dbGta != null) {
                    if (dbReviews.stream().noneMatch(r -> r.getGame().getId().equals(dbGta.getId()) && r.getClient().getId().equals(clientCosme.getId()))) {
                        ReviewEntity r3 = new ReviewEntity();
                        r3.setRating(3);
                        r3.setComment("El juego base está bien, pero el online está lleno de hackers.");
                        r3.setDate(LocalDateTime.now().minusHours(5));
                        r3.setGame(dbGta);
                        r3.setClient(clientCosme);
                        reviewRepository.save(r3);
                    }
                    if (dbReviews.stream().noneMatch(r -> r.getGame().getId().equals(dbGta.getId()) && r.getClient().getId().equals(clientEsteban.getId()))) {
                        ReviewEntity r4 = new ReviewEntity();
                        r4.setRating(5);
                        r4.setComment("Un clásico. No me canso de recorrer Los Santos y hacer misiones.");
                        r4.setDate(LocalDateTime.now().minusDays(1));
                        r4.setGame(dbGta);
                        r4.setClient(clientEsteban);
                        reviewRepository.save(r4);
                    }
                }

                // ------------------------------------------------------
                // REVIEWS EN: Super Mario Odyssey
                // ------------------------------------------------------
                GameEntity dbMario = dbGames.stream().filter(g -> g.getName().equals("Super Mario Odyssey")).findFirst().orElse(null);
                if (dbMario != null) {
                    if (dbReviews.stream().noneMatch(r -> r.getGame().getId().equals(dbMario.getId()) && r.getClient().getId().equals(clientEsteban.getId()))) {
                        ReviewEntity r5 = new ReviewEntity();
                        r5.setRating(5);
                        r5.setComment("Mágico de principio a fin, puro estilo clásico de Nintendo.");
                        r5.setDate(LocalDateTime.now().minusHours(3));
                        r5.setGame(dbMario);
                        r5.setClient(clientEsteban);
                        reviewRepository.save(r5);
                    }
                    if (dbReviews.stream().noneMatch(r -> r.getGame().getId().equals(dbMario.getId()) && r.getClient().getId().equals(clientAlan.getId()))) {
                        ReviewEntity r6 = new ReviewEntity();
                        r6.setRating(4);
                        r6.setComment("Muy divertido para jugar en familia, los controles son una delicia.");
                        r6.setDate(LocalDateTime.now().minusHours(12));
                        r6.setGame(dbMario);
                        r6.setClient(clientAlan);
                        reviewRepository.save(r6);
                    }
                }

                // ------------------------------------------------------
                // REVIEWS EN: Red Dead Redemption 2
                // ------------------------------------------------------
                GameEntity dbRdr2 = dbGames.stream().filter(g -> g.getName().equals("Red Dead Redemption 2")).findFirst().orElse(null);
                if (dbRdr2 != null) {
                    if (dbReviews.stream().noneMatch(r -> r.getGame().getId().equals(dbRdr2.getId()) && r.getClient().getId().equals(clientEsteban.getId()))) {
                        ReviewEntity r7 = new ReviewEntity();
                        r7.setRating(5);
                        r7.setComment("La mejor historia y nivel de detalle del mundo abierto jamás creado.");
                        r7.setDate(LocalDateTime.now().minusDays(3));
                        r7.setGame(dbRdr2);
                        r7.setClient(clientEsteban);
                        reviewRepository.save(r7);
                    }
                }

                System.out.println("====== Matriz de reviews cruzadas verificadas y cargadas ======");
            }

        };

    }
}