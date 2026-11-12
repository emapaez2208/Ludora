package ExperienceGroup.Ludora.config.carga;

import ExperienceGroup.Ludora.features.client.IClientRepository;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.game.IGameRepository;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.review.IReviewRepository;
import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Order(5)
public class CargaReviews implements CargaDatos {

    private final IReviewRepository reviewRepository;
    private final IGameRepository gameRepository;
    private final IClientRepository clientRepository;

    public CargaReviews(IReviewRepository reviewRepository,
                        IGameRepository gameRepository,
                        IClientRepository clientRepository) {
        this.reviewRepository = reviewRepository;
        this.gameRepository = gameRepository;
        this.clientRepository = clientRepository;
    }

    // antiguedad = hace cuánto se publicó la reseña (Duration.ofHours(5), Duration.ofDays(2)...)
    private record Resena(String juego, String usuario, int estrellas, String comentario, Duration antiguedad) {}

    private static final List<Resena> RESENAS = List.of(
            // --- SANTA MONICA STUDIOS & PLAYSTATION STUFF ---
        new Resena("God of War Ragnarok", "cosmejuega", 5, "Cierre perfecto para la saga nórdica. Gráficamente es impactante y el combate con el hacha se siente increíble.", Duration.ofDays(40)),
        new Resena("God of War Ragnarok", "estebanquito", 5, "Visualmente alucinante. Las batallas contra jefes principales te dejan sin aliento.", Duration.ofDays(35)),
        new Resena("God of War Ragnarok", "martingomez", 4, "Grandioso juego, aunque el ritmo en algunas zonas secundarias baja un poco.", Duration.ofDays(15)),
        new Resena("God of War", "joaquinmolina", 5, "El reinicio perfecto. La relación entre Kratos y Atreus evoluciona de forma muy madura.", Duration.ofDays(10)),
        new Resena("God of War", "alanbrito", 5, "Camara al hombro, combate visceral y un mapa hermoso. Una joya total.", Duration.ofDays(20)),
        new Resena("The Last of Us Part I", "joaquinmolina", 5, "La narrativa más conmovedora y cruda de los videojuegos. El trabajo de sonido es de 10.", Duration.ofDays(11)),
        new Resena("The Last of Us Part I", "luciafernandez", 5, "Increíble cómo te conecta con los personajes desde el prólogo hasta el final.", Duration.ofDays(30)),
        new Resena("The Last of Us Part I", "cosmejuega", 4, "Muy buen remake, el sigilo y la tensión con los infectados están al máximo.", Duration.ofDays(25)),
        new Resena("Uncharted: Legacy of Thieves", "joaquinmolina", 4, "Acción cinematográfica pura. Muy divertido y con tiroteos vertiginosos.", Duration.ofDays(8)),
        new Resena("God of War III Remastered", "joaquinmolina", 5, "Escala colosal. Destruir al Olimpo con la furia pura de Kratos nunca pasa de moda.", Duration.ofDays(5)),
        new Resena("Spider-Man 2", "sofiabenitez", 5, "Balancearse por Nueva York con las alas de telaraña es súper fluido y entretenido.", Duration.ofDays(16)),
        new Resena("Helldivers 2", "facundorios", 5, "Caos democrático puro en multijugador. Risas y explosiones aseguradas.", Duration.ofHours(4)),

        // --- ROCKSTAR GAMES ---
        new Resena("GTA V", "estebanquito", 4, "Un clásico indiscutido. No me canso de recorrer Los Santos.", Duration.ofDays(50)),
        new Resena("GTA V", "cosmejuega", 3, "El juego base es histórico, pero la comunidad del modo online a veces es frustrante.", Duration.ofDays(45)),
        new Resena("GTA V", "gonzalosilva", 5, "Tres protagonistas inolvidables y misiones de asalto espectaculares.", Duration.ofDays(25)),
        new Resena("Red Dead Redemption 2", "estebanquito", 5, "La mejor historia y nivel de detalle jamás creado en un mundo abierto.", Duration.ofDays(45)),
        new Resena("Red Dead Redemption 2", "luciafernandez", 5, "Arthur Morgan es quizás el personaje mejor escrito de la industria.", Duration.ofDays(36)),
        new Resena("Red Dead Redemption", "joaquinmolina", 5, "La historia del viejo oeste de John Marston sigue siendo legendaria.", Duration.ofDays(12)),
        new Resena("Max Payne 3", "luciafernandez", 5, "La física de los disparos y el tiempo bala son insuperables incluso hoy en día.", Duration.ofHours(8)),
        new Resena("L.A. Noire", "luciafernandez", 4, "Interrogar sospechosos y leer sus gestos faciales es una mecánica genial.", Duration.ofDays(14)),

        // --- NINTENDO ---
        new Resena("Super Mario Odyssey", "alanbrito", 5, "Mágico de principio a fin. La mecánica de Cappy aporta una variedad increíble.", Duration.ofDays(42)),
        new Resena("Super Mario Odyssey", "estebanquito", 5, "Pura diversión y creatividad en cada mundo que visitas.", Duration.ofDays(38)),
        new Resena("Animal Crossing: New Horizons", "valentinasosa", 5, "Super relajante. El juego ideal para desconectar un rato todos los días.", Duration.ofDays(20)),
        new Resena("Animal Crossing: New Horizons", "julietalopez", 4, "Decorar la isla y compartir ropa personalizada con amigos es genial.", Duration.ofDays(18)),
        new Resena("Super Smash Bros. Ultimate", "valentinasosa", 4, "Cantidad absurda de contenido y personajes. Infaltable en reuniones.", Duration.ofDays(18)),
        new Resena("The Legend of Zelda: BotW", "valentinasosa", 5, "La libertad de exploración que ofrece cambió el género de mundo abierto.", Duration.ofDays(22)),
        new Resena("The Legend of Zelda: TotK", "valentinasosa", 5, "Construir vehículos y resolver acertijos con la Ultramano eleva la fórmula a otro nivel.", Duration.ofDays(10)),
        new Resena("Super Mario Bros. Wonder", "valentinasosa", 5, "Las flores maravilla transforman los niveles de formas súper locas e inesperadas.", Duration.ofDays(7)),
        new Resena("Mario Kart 8 Deluxe", "julietalopez", 5, "Las mejores pistas de la franquicia. Competir online es muy adictivo.", Duration.ofDays(15)),

        // --- CD PROJEKT RED ---
        new Resena("The Witcher 3: Wild Hunt", "luciafernandez", 5, "Las misiones secundarias tienen mejor guion que muchos juegos enteros. Obra maestra.", Duration.ofDays(35)),
        new Resena("The Witcher 3: Wild Hunt", "estebanquito", 5, "El Continente es gigante y Geralt es un protagonista carismático inolvidable.", Duration.ofDays(28)),
        new Resena("The Witcher 3: Wild Hunt", "marianodiaz", 5, "Excelente diseño de monstruos y contrato tras contrato te enganchas por horas.", Duration.ofDays(22)),
        new Resena("Phantom Liberty", "luciafernandez", 5, "El DLC supera al juego base en ritmo y tensión. Idris Elba hace un papelazo.", Duration.ofDays(32)),
        new Resena("Cyberpunk 2077", "estebanquito", 4, "Después de los parches quedó increíble. Night City es una maravilla técnica.", Duration.ofDays(48)),
        new Resena("Cyberpunk 2077", "cosmejuega", 4, "La ambientación cyberpunk e iluminación con Ray Tracing son insuperables.", Duration.ofDays(34)),
        new Resena("Cyberpunk 2077", "marianodiaz", 4, "Buena historia principal y combates acelerados con implantes cibernéticos.", Duration.ofDays(19)),
        new Resena("Cyberpunk 2077", "agustinaramirez", 5, "Night City se siente viva, oscura y decadente. La música acompaña genial.", Duration.ofDays(9)),

        // --- FROMSOFTWARE / SOULSLIKE ---
        new Resena("Elden Ring", "cosmejuega", 5, "Obra maestra absoluta. Morí 200 veces contra Malenia pero valió la pena.", Duration.ofDays(55)),
        new Resena("Elden Ring", "estebanquito", 5, "Descubrir fortalezas y calabozos subterráneos es una experiencia mágica.", Duration.ofDays(40)),
        new Resena("Elden Ring", "alanbrito", 2, "La dirección artística es brutal, pero no me convence la dificultad tan exigente.", Duration.ofDays(12)),
        new Resena("Elden Ring", "nicolasacosta", 5, "Flexibilidad total de builds. Magia, espadones o destreza, todo funciona bien.", Duration.ofDays(18)),
        new Resena("Sekiro: Shadows Die Twice", "nicolasacosta", 5, "El sistema de combate basado en parries es el más preciso y satisfactorio.", Duration.ofDays(17)),
        new Resena("Sekiro: Shadows Die Twice", "martingomez", 5, "Te exige concentración absoluta. La sensación de vencer a un jefe es única.", Duration.ofDays(14)),
        new Resena("Dark Souls III", "nicolasacosta", 5, "Grandioso cierre para la trilogía. Jefes inolvidables y excelente banda sonora.", Duration.ofDays(18)),
        new Resena("Dark Souls Remastered", "nicolasacosta", 4, "El diseño interconectado del mapa de Lordran es una lección de diseño.", Duration.ofDays(15)),
        new Resena("Dark Souls Remastered", "alanbrito", 1, "Me caí en Blighttown varias veces seguidas y no pude seguir. Frustrante.", Duration.ofDays(28)),
        new Resena("Dark Souls II: Scholar of the First Sin", "nicolasacosta", 3, "Tiene buenas ideas y muchos jefes, aunque la agilidad y las trampas pesan.", Duration.ofDays(6)),
        new Resena("Bloodborne", "nicolasacosta", 5, "La atmósfera victoriana-lovecraftiana y el combate rápido lo hacen perfecto.", Duration.ofDays(11)),
        new Resena("Bloodborne", "luciafernandez", 5, "Terror, sangre y lore fascinante en la ciudad de Yharnam.", Duration.ofDays(9)),
        new Resena("Armored Core VI", "agustinaramirez", 5, "Personalizar tu propio mecha y sentir la velocidad del combate es adictivo.", Duration.ofDays(9)),
        new Resena("Armored Core VI", "nicolasacosta", 4, "Tiroteos frenéticos en el aire y jefes mecánicos gigantes desafiantes.", Duration.ofDays(5)),

        // --- CAPCOM ---
        new Resena("Resident Evil 4 Remake", "martingomez", 5, "El mejor remake de la historia. Respetaron el clásico y modernizaron el combate.", Duration.ofDays(28)),
        new Resena("Resident Evil 4 Remake", "cosmejuega", 5, "Leon S. Kennedy con movimientos fluidos y parries con cuchillo. Impecable.", Duration.ofDays(24)),
        new Resena("Resident Evil 4 Remake", "alanbrito", 4, "Tenso, divertido y con gran valor de rejugabilidad.", Duration.ofDays(16)),
        new Resena("Devil May Cry 5", "martingomez", 5, "Hack and slash estilizado con combos interminables y música genial.", Duration.ofDays(25)),
        new Resena("Resident Evil 2 Remake", "estebanquito", 5, "Gestión de recursos impecable y una atmósfera de terror claustrofóbica.", Duration.ofDays(3)),
        new Resena("Resident Evil 2 Remake", "martingomez", 5, "El Mr. X persiguiéndote por la comisaría genera una tensión bárbara.", Duration.ofDays(11)),
        new Resena("Resident Evil Village", "estebanquito", 4, "Mezcla la acción al estilo RE4 con momentos de puro terror gótico.", Duration.ofDays(2)),
        new Resena("Monster Hunter: World", "agustinaramirez", 4, "Cazar, mejorar equipo y volver a cazar bestias más colosales. Excelente bucle.", Duration.ofDays(8)),
        new Resena("Monster Hunter: World", "martingomez", 4, "Multijugador genial para cazar dragones ancianos en equipo.", Duration.ofDays(6)),
        new Resena("Monster Hunter Wilds", "agustinaramirez", 5, "Los ecosistemas cambiantes en tiempo real se ven espectaculares.", Duration.ofHours(3)),
        new Resena("Street Fighter 6", "martingomez", 5, "El modo World Tour y los controles modernos acercan la franquicia a todos.", Duration.ofDays(13)),
        new Resena("Street Fighter 6", "cosmejuega", 4, "Netcode rollback excelente, partidas estables online.", Duration.ofDays(7)),

        // --- BETHESDA ---
        new Resena("The Elder Scrolls V: Skyrim", "facundorios", 5, "A pesar de los años, la libertad para explorar jamás pasa de moda.", Duration.ofDays(45)),
        new Resena("Fallout 4", "facundorios", 3, "La construcción de bases y el tiroteo están muy bien, pero perdió algo de rol.", Duration.ofDays(43)),
        new Resena("Fallout: New Vegas", "facundorios", 5, "El mejor sistema de facciones y diálogo de toda la saga Fallout.", Duration.ofDays(21)),
        new Resena("DOOM Eternal", "facundorios", 5, "Combate rápido como un ajedrez a 300 km/h. La música de Mick Gordon rompe todo.", Duration.ofDays(17)),
        new Resena("Starfield", "marianodiaz", 3, "Buen juego espacial, pero demasiadas pantallas de carga rompen la inmersión.", Duration.ofDays(6)),
        new Resena("Starfield", "facundorios", 4, "Personalizar tu nave espacial y explorar astros tiene su encanto.", Duration.ofDays(10)),

        // --- VALVE & PC CLASSICS ---
        new Resena("Half-Life 2", "florenciarossi", 5, "Envejeció increíblemente bien. La pistola de gravedad es pura física brillante.", Duration.ofDays(23)),
        new Resena("Half-Life 2", "cosmejuega", 5, "Una obra maestra que definió los shooters narrativos en primera persona.", Duration.ofDays(31)),
        new Resena("Half-Life 2", "alanbrito", 4, "Excelente ambientación distópica en Ciudad 17.", Duration.ofDays(18)),
        new Resena("Portal 2", "florenciarossi", 5, "Guion divertidísimo y puzles brillantes. GlaDOS y Wheatley brillan.", Duration.ofDays(22)),
        new Resena("Portal 2", "alanbrito", 5, "Uno de los mejores juegos cooperativos de puzles jamás creados.", Duration.ofDays(14)),
        new Resena("Counter-Strike 2", "florenciarossi", 3, "Gameplay competitivo sólido en Source 2, aunque aún faltan varios modos.", Duration.ofDays(21)),
        new Resena("Left 4 Dead 2", "florenciarossi", 5, "Jugar campañas de infectados con 3 amigos sigue siendo diversión pura.", Duration.ofDays(15)),
        new Resena("Dishonored 2", "florenciarossi", 4, "Diseño de niveles soberbio con múltiples caminos e infiltración sigilosa.", Duration.ofDays(8)),

        // --- SQUARE ENIX ---
        new Resena("Final Fantasy VII Remake", "marianodiaz", 4, "El combate en tiempo real mezclado con pausas tácticas es dinámico y vistoso.", Duration.ofDays(39)),
        new Resena("Final Fantasy XVI", "marianodiaz", 4, "Batallas de Eikons cinematográficas increíbles al estilo Hack and Slash.", Duration.ofDays(13)),
        new Resena("NieR: Automata", "marianodiaz", 5, "Filosófico, profundo y con una banda sonora inolvidable. Hay que jugarlo todo.", Duration.ofDays(38)),
        new Resena("NieR: Automata", "agustinaramirez", 5, "Combina shooters cenitales, hack and slash y narrativa única.", Duration.ofDays(12)),
        new Resena("Kingdom Hearts III", "marianodiaz", 3, "Visualmente parece una película de Pixar, aunque la historia se vuelve enredada.", Duration.ofDays(4)),

        // --- EA & SPORTS ---
        new Resena("EA SPORTS FC 24", "gonzalosilva", 2, "Pocos cambios respecto a entregas anteriores y foco excesivo en sobres.", Duration.ofDays(27)),
        new Resena("Need for Speed Unbound", "gonzalosilva", 4, "El estilo visual tipo grafiti le da mucha personalidad y el manejo es fluido.", Duration.ofDays(26)),
        new Resena("eFootball 2024", "gonzalosilva", 3, "Jugabilidad táctica interesante para ser gratis, aunque le faltan licencias.", Duration.ofDays(20)),
        new Resena("Battlefield 1", "gonzalosilva", 5, "La ambientación de la Primera Guerra Mundial y el sonido de las armas impacta.", Duration.ofDays(9)),
        new Resena("Dead Space Remake", "estebanquito", 5, "Sonido ambiente espeluznante a bordo de la USG Ishimura. Gran survival horror.", Duration.ofDays(1)),
        new Resena("Mass Effect Legendary Edition", "marianodiaz", 5, "Una de las mayores epopeyas de ciencia ficción de la historia de los videojuegos.", Duration.ofDays(26)),
        new Resena("Apex Legends", "facundorios", 4, "Movimiento rapísimo y tiroteos por escuadrones muy competitivos.", Duration.ofDays(15)),

        // --- FREE TO PLAY & EXTRA MULTIPLAYER ---
        new Resena("Fortnite", "julietalopez", 5, "Súper dinámico con las actualizaciones constantes y los eventos masivos.", Duration.ofDays(30)),
        new Resena("Fall Guys", "julietalopez", 4, "Risas aseguradas para jugar partidas rápidas con amigos.", Duration.ofDays(29)),
        new Resena("Fall Guys", "sofiabenitez", 4, "Ideal para entretenerse un rato esquivando obstáculos alocados.", Duration.ofDays(12)),
        new Resena("Brawlhalla", "julietalopez", 3, "Plataforma de peleas accesible y liviana, ideal para partidas casuales.", Duration.ofDays(1)),
        new Resena("Rocket League", "sofiabenitez", 5, "Concepto simple pero con una curva de aprendizaje y techo de habilidad alto.", Duration.ofDays(14)),
        new Resena("Rocket League", "gonzalosilva", 5, "Fútbol con autos propulsados. Partidas rápidas y ultra adictivas.", Duration.ofDays(18)),
        new Resena("The Sims 4", "florenciarossi", 4, "Infinitas posibilidades para construir casas y crear personajes.", Duration.ofDays(4)),
        new Resena("The Sims 4", "julietalopez", 5, "Pasarse horas diseñando casas y decorando interiores es lo mejor del juego.", Duration.ofDays(16)),
        new Resena("Overwatch 2", "florenciarossi", 2, "El gameplay 5v5 es rápido, pero la monetización dejó mucho que desear.", Duration.ofDays(3)),
        new Resena("Overwatch 2", "sofiabenitez", 3, "Los diseños de héroes son geniales, pero el emparejamiento a veces falla.", Duration.ofDays(8)),
        new Resena("Valorant", "facundorios", 4, "Shooter ultra competitivo. Requiere mucha comunicación y puntería fina.", Duration.ofDays(11)),
        new Resena("League of Legends", "facundorios", 3, "Mecánicamente brillante pero la toxicidad de la comunidad arruina partidas.", Duration.ofDays(24)),
        new Resena("Hearthstone", "sofiabenitez", 4, "Estrategia por cartas ambientada en Warcraft, ideal para jugar partidas cortas.", Duration.ofDays(5))
    );

    @Override
    public void cargar() {
        Map<String, GameEntity> juegos = gameRepository.findAll().stream()
                .collect(Collectors.toMap(GameEntity::getName, Function.identity(), (a, b) -> a));
        Map<String, ClientEntity> clientes = clientRepository.findAll().stream()
                .collect(Collectors.toMap(ClientEntity::getUserName, Function.identity(), (a, b) -> a));

        // Una reseña por cliente y juego: "idJuego:idCliente"
        Set<String> existentes = reviewRepository.findAll().stream()
                .map(r -> r.getGame().getId() + ":" + r.getClient().getId())
                .collect(Collectors.toSet());

        LocalDateTime ahora = LocalDateTime.now();
        int nuevas = 0;

        for (Resena r : RESENAS) {
            GameEntity juego = juegos.get(r.juego());
            ClientEntity cliente = clientes.get(r.usuario());
            if (juego == null || cliente == null) continue;

            if (existentes.contains(juego.getId() + ":" + cliente.getId())) continue;

            ReviewEntity review = new ReviewEntity();
            review.setRating(r.estrellas());
            review.setComment(r.comentario());
            review.setDate(ahora.minus(r.antiguedad()));
            review.setGame(juego);
            review.setClient(cliente);
            reviewRepository.save(review);
            nuevas++;
        }

        System.out.println("    Reseñas nuevas: " + nuevas);
    }
}