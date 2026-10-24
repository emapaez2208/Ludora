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
            System.out.println("   Username: cdpDev        | Password: Cdp123      (CD Projekt Red)");
            System.out.println("   Username: hidetakaDev   | Password: Miyazaki123 (FromSoftware)");
            System.out.println("   Username: capcomDev     | Password: Capcom123   (Capcom)");
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
            // CD PROJEKT RED (cdpDev)
            if (dbDevs.stream().noneMatch(d -> d.getUserName().equals("cdpDev"))) {
                DeveloperEntity dev4 = new DeveloperEntity();
                dev4.setExternalId(java.util.UUID.randomUUID());
                dev4.setName("Marcin");
                dev4.setLastName("Iwinski");
                dev4.setUserName("cdpDev");
                dev4.setEmail(new Email("contact@cdprojektred.com"));
                dev4.setStatusBlocked(false);
                dev4.setCompany("CD Projekt Red");
                dev4 = developerRepository.save(dev4);

                CredentialsEntity creds4 = CredentialsEntity.builder()
                        .externalId(dev4.getExternalId())
                        .username(dev4.getUserName())
                        .password(passwordEncoder.encode("Cdp123"))
                        .enabled(true)
                        .accountNonLocked(true)
                        .user(dev4)
                        .roles(java.util.Set.of(devRole))
                        .build();
                credentialsRepository.save(creds4);
                System.out.println("====== Developer CD Projekt Red cargado con éxito ======");
            }

            // FROMSOFTWARE (hidetakaDev)
            if (dbDevs.stream().noneMatch(d -> d.getUserName().equals("hidetakaDev"))) {
                DeveloperEntity dev5 = new DeveloperEntity();
                dev5.setExternalId(java.util.UUID.randomUUID());
                dev5.setName("Hidetaka");
                dev5.setLastName("Miyazaki");
                dev5.setUserName("hidetakaDev");
                dev5.setEmail(new Email("miyazaki@fromsoftware.com"));
                dev5.setStatusBlocked(false);
                dev5.setCompany("FromSoftware");
                dev5 = developerRepository.save(dev5);

                CredentialsEntity creds5 = CredentialsEntity.builder()
                        .externalId(dev5.getExternalId())
                        .username(dev5.getUserName())
                        .password(passwordEncoder.encode("Miyazaki123"))
                        .enabled(true)
                        .accountNonLocked(true)
                        .user(dev5)
                        .roles(java.util.Set.of(devRole))
                        .build();
                credentialsRepository.save(creds5);
                System.out.println("====== Developer FromSoftware cargado con éxito ======");
            }

            // CAPCOM (capcomDev)
            if (dbDevs.stream().noneMatch(d -> d.getUserName().equals("capcomDev"))) {
                DeveloperEntity dev6 = new DeveloperEntity();
                dev6.setExternalId(java.util.UUID.randomUUID());
                dev6.setName("Kenzo");
                dev6.setLastName("Tsujimoto");
                dev6.setUserName("capcomDev");
                dev6.setEmail(new Email("kenzo@capcom.com"));
                dev6.setStatusBlocked(false);
                dev6.setCompany("Capcom");
                dev6 = developerRepository.save(dev6);

                CredentialsEntity creds6 = CredentialsEntity.builder()
                        .externalId(dev6.getExternalId())
                        .username(dev6.getUserName())
                        .password(passwordEncoder.encode("Capcom123"))
                        .enabled(true)
                        .accountNonLocked(true)
                        .user(dev6)
                        .roles(java.util.Set.of(devRole))
                        .build();
                credentialsRepository.save(creds6);
                System.out.println("====== Developer Capcom cargado con éxito ======");
            }

            dbDevs = developerRepository.findAll();
            DeveloperEntity manuelDev = dbDevs.stream().filter(d -> d.getUserName().equals("manuelDev")).findFirst().orElse(null);
            DeveloperEntity francoDev = dbDevs.stream().filter(d -> d.getUserName().equals("francoDev")).findFirst().orElse(null);
            DeveloperEntity shigeruDev = dbDevs.stream().filter(d -> d.getUserName().equals("shigeruDev")).findFirst().orElse(null);
            DeveloperEntity cdpDev = dbDevs.stream().filter(d -> d.getUserName().equals("cdpDev")).findFirst().orElse(null);
            DeveloperEntity hidetakaDev = dbDevs.stream().filter(d -> d.getUserName().equals("hidetakaDev")).findFirst().orElse(null);
            DeveloperEntity capcomDev = dbDevs.stream().filter(d -> d.getUserName().equals("capcomDev")).findFirst().orElse(null);
            System.out.println("====== 6 Entidades de Developers listas para el catálogo ======");

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
                gow.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/2322010/header.jpg");
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
                gow2018.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1593500/header.jpg");
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
                spider.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1817070/header.jpg");
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
                tlou.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1888930/header.jpg");
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
                unch.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1659420/header.jpg");
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
                gta.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/271590/header.jpg");
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
                rdr2.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1174180/header.jpg");
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
                gta4.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/12210/header.jpg");
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
                lanoire.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/110800/header.jpg");
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
                max.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/204100/header.jpg");
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
                mario.setFrontPage("https://assets.nintendo.com/image/upload/ar_16:9,c_lpad,w_1240/b_white/f_auto/q_auto/store/software/switch/70010000001130/c42553b4fd0312c31e70ec7468c6c9bccd739f340152925b9600631f2d29f8b5");
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
                zelda.setFrontPage("https://i.blogs.es/15da49/zelda00/1366_2000.jpg");
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
                mk8.setFrontPage("https://assets.nintendo.com/image/upload/q_auto/f_auto/store/software/switch/70010000000153/de697f487a36d802dd9a5ff0341f717c8486221f2f1219b675af37aca63bc453");
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
                ac.setFrontPage("https://imagen.nextn.es/wp-content/uploads/2019/06/1906-11-Animal-Crossing-New-Horizons-01.jpg?strip=all&sharp=1");
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
                smash.setFrontPage("https://assets.nintendo.com/image/upload/q_auto/f_auto/store/software/switch/70010000012332/ac4d1fc9824876ce756406f0525d50c57ded4b2a666f6dfe40a6ac5c3563fad9");
                smash.setPrice(new BigDecimal("59.99"));
                smash.setDescription("El mayor crossover de peleas de la historia de los videojuegos.");
                smash.setReleaseDate(LocalDate.parse("2018-12-07"));
                smash.setDeveloper(shigeruDev);
                smash.setAgeRange(mas13);
                smash.setGenres(List.of(action));
                smash.setStatusBlocked(false);
                gameRepository.save(smash);
            }
            // ----------------------------------------------------------
            // 5 JUEGOS DE CD PROJEKT RED (cdpDev)
            // ----------------------------------------------------------

            // Juego 16: The Witcher 3: Wild Hunt (Steam AppID: 292030)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("The Witcher 3: Wild Hunt"))) {
                GameEntity tw3 = new GameEntity();
                tw3.setName("The Witcher 3: Wild Hunt");
                tw3.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/292030/header.jpg");
                tw3.setPrice(new BigDecimal("39.99"));
                tw3.setDescription("Geralt de Rivia busca a la Niña de la Profecía en un continente en guerra.");
                tw3.setReleaseDate(LocalDate.parse("2015-05-18"));
                tw3.setDeveloper(cdpDev);
                tw3.setAgeRange(mas18);
                tw3.setGenres(List.of(rpg, adventure));
                tw3.setStatusBlocked(false);
                gameRepository.save(tw3);
            }

            // Juego 17: Cyberpunk 2077 (Steam AppID: 1091500)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Cyberpunk 2077"))) {
                GameEntity cp = new GameEntity();
                cp.setName("Cyberpunk 2077");
                cp.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1091500/header.jpg");
                cp.setPrice(new BigDecimal("59.99"));
                cp.setDescription("Un mercenario cibernético busca un implante que otorga la inmortalidad en Night City.");
                cp.setReleaseDate(LocalDate.parse("2020-12-10"));
                cp.setDeveloper(cdpDev);
                cp.setAgeRange(mas18);
                cp.setGenres(List.of(rpg, shooter, action));
                cp.setStatusBlocked(false);
                gameRepository.save(cp);
            }

            // Juego 18: Cyberpunk 2077: Phantom Liberty (Steam AppID: 2138330)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Phantom Liberty"))) {
                GameEntity pl = new GameEntity();
                pl.setName("Phantom Liberty");
                pl.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/2138330/header.jpg");
                pl.setPrice(new BigDecimal("29.99"));
                pl.setDescription("Aventura de suspenso y espionaje en el distrito más peligroso de Night City: Dogtown.");
                pl.setReleaseDate(LocalDate.parse("2023-09-26"));
                pl.setDeveloper(cdpDev);
                pl.setAgeRange(mas18);
                pl.setGenres(List.of(rpg, action));
                pl.setStatusBlocked(false);
                gameRepository.save(pl);
            }

            // Juego 19: The Witcher 2: Assassins of Kings (Steam AppID: 20920)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("The Witcher 2"))) {
                GameEntity tw2 = new GameEntity();
                tw2.setName("The Witcher 2");
                tw2.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/20920/header.jpg");
                tw2.setPrice(new BigDecimal("19.99"));
                tw2.setDescription("Intrigas políticas y regicidio en los Reinos del Norte con Geralt.");
                tw2.setReleaseDate(LocalDate.parse("2011-05-17"));
                tw2.setDeveloper(cdpDev);
                tw2.setAgeRange(mas18);
                tw2.setGenres(List.of(rpg, action));
                tw2.setStatusBlocked(false);
                gameRepository.save(tw2);
            }

            // Juego 20: The Witcher: Enhanced Edition (Steam AppID: 20900)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("The Witcher 1"))) {
                GameEntity tw1 = new GameEntity();
                tw1.setName("The Witcher 1");
                tw1.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/20900/header.jpg");
                tw1.setPrice(new BigDecimal("9.99"));
                tw1.setDescription("El inicio de la leyenda de Geralt de Rivia en Vizima.");
                tw1.setReleaseDate(LocalDate.parse("2008-09-16"));
                tw1.setDeveloper(cdpDev);
                tw1.setAgeRange(mas18);
                tw1.setGenres(List.of(rpg));
                tw1.setStatusBlocked(false);
                gameRepository.save(tw1);
            }

            // ----------------------------------------------------------
            // 5 JUEGOS DE FROMSOFTWARE (hidetakaDev)
            // ----------------------------------------------------------

            // Juego 21: Elden Ring (Steam AppID: 1245620)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Elden Ring"))) {
                GameEntity elden = new GameEntity();
                elden.setName("Elden Ring");
                elden.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1245620/header.jpg");
                elden.setPrice(new BigDecimal("59.99"));
                elden.setDescription("Levántate, Sinluz, y déjate guiar por la gracia hacia las Tierras Intermedias.");
                elden.setReleaseDate(LocalDate.parse("2022-02-25"));
                elden.setDeveloper(hidetakaDev);
                elden.setAgeRange(mas18);
                elden.setGenres(List.of(action, rpg));
                elden.setStatusBlocked(false);
                gameRepository.save(elden);
            }

            // Juego 22: Sekiro: Shadows Die Twice (Steam AppID: 814380)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Sekiro: Shadows Die Twice"))) {
                GameEntity sekiro = new GameEntity();
                sekiro.setName("Sekiro: Shadows Die Twice");
                sekiro.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/814380/header.jpg");
                sekiro.setPrice(new BigDecimal("59.99"));
                sekiro.setDescription("Venganza, sigilo y duelos a muerte con katanas en el Japón feudal.");
                sekiro.setReleaseDate(LocalDate.parse("2019-03-22"));
                sekiro.setDeveloper(hidetakaDev);
                sekiro.setAgeRange(mas18);
                sekiro.setGenres(List.of(action, adventure));
                sekiro.setStatusBlocked(false);
                gameRepository.save(sekiro);
            }

            // Juego 23: Dark Souls III (Steam AppID: 374320)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Dark Souls III"))) {
                GameEntity ds3 = new GameEntity();
                ds3.setName("Dark Souls III");
                ds3.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/374320/header.jpg");
                ds3.setPrice(new BigDecimal("59.99"));
                ds3.setDescription("Las cenizas buscan un Señor en un mundo en descomposición total.");
                ds3.setReleaseDate(LocalDate.parse("2016-04-12"));
                ds3.setDeveloper(hidetakaDev);
                ds3.setAgeRange(mas18);
                ds3.setGenres(List.of(action, rpg));
                ds3.setStatusBlocked(false);
                gameRepository.save(ds3);
            }

            // Juego 24: Dark Souls Remastered (Steam AppID: 570940)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Dark Souls Remastered"))) {
                GameEntity ds1 = new GameEntity();
                ds1.setName("Dark Souls Remastered");
                ds1.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/570940/header.jpg");
                ds1.setPrice(new BigDecimal("39.99"));
                ds1.setDescription("Y entonces hubo fuego. Vuelve a encender la llama en Lordran.");
                ds1.setReleaseDate(LocalDate.parse("2018-05-24"));
                ds1.setDeveloper(hidetakaDev);
                ds1.setAgeRange(mas18);
                ds1.setGenres(List.of(action, rpg));
                ds1.setStatusBlocked(false);
                gameRepository.save(ds1);
            }

            // Juego 25: Armored Core VI: Fires of Rubicon (Steam AppID: 1888160)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Armored Core VI"))) {
                GameEntity ac6 = new GameEntity();
                ac6.setName("Armored Core VI");
                ac6.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1888160/header.jpg");
                ac6.setPrice(new BigDecimal("59.99"));
                ac6.setDescription("Combates vertiginosos de mechas gigantes personalizables en el planeta Rubicón 3.");
                ac6.setReleaseDate(LocalDate.parse("2023-08-25"));
                ac6.setDeveloper(hidetakaDev);
                ac6.setAgeRange(mas13);
                ac6.setGenres(List.of(action, shooter));
                ac6.setStatusBlocked(false);
                gameRepository.save(ac6);
            }

            // ----------------------------------------------------------
            // 5 JUEGOS DE CAPCOM (capcomDev)
            // ----------------------------------------------------------

            // Juego 26: Resident Evil 4 Remake (Steam AppID: 2050650)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Resident Evil 4 Remake"))) {
                GameEntity re4 = new GameEntity();
                re4.setName("Resident Evil 4 Remake");
                re4.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/2050650/header.jpg");
                re4.setPrice(new BigDecimal("59.99"));
                re4.setDescription("Leon S. Kennedy rescata a la hija del presidente en una siniestra aldea europea.");
                re4.setReleaseDate(LocalDate.parse("2023-03-24"));
                re4.setDeveloper(capcomDev);
                re4.setAgeRange(mas18);
                re4.setGenres(List.of(action, terror));
                re4.setStatusBlocked(false);
                gameRepository.save(re4);
            }

            // Juego 27: Resident Evil 2 Remake (Steam AppID: 883710)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Resident Evil 2 Remake"))) {
                GameEntity re2 = new GameEntity();
                re2.setName("Resident Evil 2 Remake");
                re2.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/883710/header.jpg");
                re2.setPrice(new BigDecimal("39.99"));
                re2.setDescription("Sobreviví al brote zombi en la comisaría de Raccoon City con Leon y Claire.");
                re2.setReleaseDate(LocalDate.parse("2019-01-25"));
                re2.setDeveloper(capcomDev);
                re2.setAgeRange(mas18);
                re2.setGenres(List.of(terror, action));
                re2.setStatusBlocked(false);
                gameRepository.save(re2);
            }

            // Juego 28: Resident Evil Village (Steam AppID: 1196590)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Resident Evil Village"))) {
                GameEntity rev = new GameEntity();
                rev.setName("Resident Evil Village");
                rev.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1196590/header.jpg");
                rev.setPrice(new BigDecimal("39.99"));
                rev.setDescription("Ethan Winters busca a su hija secuestrada enfrentando los horrores del castillo Dimitrescu.");
                rev.setReleaseDate(LocalDate.parse("2021-05-07"));
                rev.setDeveloper(capcomDev);
                rev.setAgeRange(mas18);
                rev.setGenres(List.of(terror, action));
                rev.setStatusBlocked(false);
                gameRepository.save(rev);
            }

            // Juego 29: Devil May Cry 5 (Steam AppID: 601150)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Devil May Cry 5"))) {
                GameEntity dmc5 = new GameEntity();
                dmc5.setName("Devil May Cry 5");
                dmc5.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/601150/header.jpg");
                dmc5.setPrice(new BigDecimal("29.99"));
                dmc5.setDescription("Dante, Nero y V cazan demonios con el combate hack and slash más estilizado.");
                dmc5.setReleaseDate(LocalDate.parse("2019-03-08"));
                dmc5.setDeveloper(capcomDev);
                dmc5.setAgeRange(mas18);
                dmc5.setGenres(List.of(action));
                dmc5.setStatusBlocked(false);
                gameRepository.save(dmc5);
            }

            // Juego 30: Monster Hunter: World (Steam AppID: 582010)
            if (dbGames.stream().noneMatch(g -> g.getName().equals("Monster Hunter: World"))) {
                GameEntity mhw = new GameEntity();
                mhw.setName("Monster Hunter: World");
                mhw.setFrontPage("https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/582010/header.jpg");
                mhw.setPrice(new BigDecimal("29.99"));
                mhw.setDescription("Caza bestias colosales en ecosistemas vivos para forjar armaduras y armas épicas.");
                mhw.setReleaseDate(LocalDate.parse("2018-08-09"));
                mhw.setDeveloper(capcomDev);
                mhw.setAgeRange(mas13);
                mhw.setGenres(List.of(action, rpg));
                mhw.setStatusBlocked(false);
                gameRepository.save(mhw);
            }
            System.out.println("====== Catálogo de juegos verificado y cargado ======");

// ==========================================================
            // REVIEWS
            // ==========================================================
            if (clientCosme != null && clientEsteban != null && clientAlan != null) {
                dbGames = gameRepository.findAll();
                List<ReviewEntity> dbReviews = reviewRepository.findAll();

                // ------------------------------------------------------
                // REVIEWS: God of War Ragnarok
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
                // REVIEWS: GTA V
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
                // REVIEWS: Super Mario Odyssey
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
                // REVIEWS: Red Dead Redemption 2
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

                // ------------------------------------------------------
                // REVIEWS: Elden Ring (FromSoftware)
                // ------------------------------------------------------
                GameEntity dbElden = dbGames.stream().filter(g -> g.getName().equals("Elden Ring")).findFirst().orElse(null);
                if (dbElden != null) {
                    if (dbReviews.stream().noneMatch(r -> r.getGame().getId().equals(dbElden.getId()) && r.getClient().getId().equals(clientCosme.getId()))) {
                        ReviewEntity r8 = new ReviewEntity();
                        r8.setRating(5);
                        r8.setComment("Obra maestra absoluta. Morí 200 veces contra Malenia pero valió cada segundo.");
                        r8.setDate(LocalDateTime.now().minusDays(1));
                        r8.setGame(dbElden);
                        r8.setClient(clientCosme);
                        reviewRepository.save(r8);
                    }
                    if (dbReviews.stream().noneMatch(r -> r.getGame().getId().equals(dbElden.getId()) && r.getClient().getId().equals(clientAlan.getId()))) {
                        ReviewEntity r9 = new ReviewEntity();
                        r9.setRating(2);
                        r9.setComment("Demasiado difícil y no te explica nada. La ambientación es genial pero frustra.");
                        r9.setDate(LocalDateTime.now().minusDays(4));
                        r9.setGame(dbElden);
                        r9.setClient(clientAlan);
                        reviewRepository.save(r9);
                    }
                }

                // ------------------------------------------------------
                // REVIEWS: Cyberpunk 2077 (CD Projekt Red)
                // ------------------------------------------------------
                GameEntity dbCp = dbGames.stream().filter(g -> g.getName().equals("Cyberpunk 2077")).findFirst().orElse(null);
                if (dbCp != null) {
                    if (dbReviews.stream().noneMatch(r -> r.getGame().getId().equals(dbCp.getId()) && r.getClient().getId().equals(clientEsteban.getId()))) {
                        ReviewEntity r10 = new ReviewEntity();
                        r10.setRating(4);
                        r10.setComment("Después de los parches quedó tremendo juego. Night City no tiene comparación.");
                        r10.setDate(LocalDateTime.now().minusHours(8));
                        r10.setGame(dbCp);
                        r10.setClient(clientEsteban);
                        reviewRepository.save(r10);
                    }
                }

                // ------------------------------------------------------
                // REVIEWS: Resident Evil 4 Remake (Capcom)
                // ------------------------------------------------------
                GameEntity dbRe4 = dbGames.stream().filter(g -> g.getName().equals("Resident Evil 4 Remake")).findFirst().orElse(null);
                if (dbRe4 != null) {
                    if (dbReviews.stream().noneMatch(r -> r.getGame().getId().equals(dbRe4.getId()) && r.getClient().getId().equals(clientCosme.getId()))) {
                        ReviewEntity r11 = new ReviewEntity();
                        r11.setRating(5);
                        r11.setComment("El mejor remake de la historia. Respetaron el clásico y modernizaron el combate a la perfección.");
                        r11.setDate(LocalDateTime.now().minusDays(6));
                        r11.setGame(dbRe4);
                        r11.setClient(clientCosme);
                        reviewRepository.save(r11);
                    }
                }

                // ------------------------------------------------------
                // REVIEWS: Dark Souls Remastered (FromSoftware)
                // ------------------------------------------------------
                GameEntity dbDs1 = dbGames.stream().filter(g -> g.getName().equals("Dark Souls Remastered")).findFirst().orElse(null);
                if (dbDs1 != null) {
                    if (dbReviews.stream().noneMatch(r -> r.getGame().getId().equals(dbDs1.getId()) && r.getClient().getId().equals(clientAlan.getId()))) {
                        ReviewEntity r12 = new ReviewEntity();
                        r12.setRating(1);
                        r12.setComment("Me caí en Blighttown cinco veces seguidas y desinstalé el juego. No es para mí.");
                        r12.setDate(LocalDateTime.now().minusHours(18));
                        r12.setGame(dbDs1);
                        r12.setClient(clientAlan);
                        reviewRepository.save(r12);
                    }
                }

                System.out.println("====== Matriz de reviews cruzadas verificadas y cargadas ======");
            }

        };

    }
}