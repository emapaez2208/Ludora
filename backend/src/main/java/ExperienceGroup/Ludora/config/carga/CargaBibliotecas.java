package ExperienceGroup.Ludora.config.carga;

import ExperienceGroup.Ludora.features.client.IClientRepository;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.game.IGameRepository;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Order(4)
public class CargaBibliotecas implements CargaDatos {

    private final IClientRepository clientRepository;
    private final IGameRepository gameRepository;

    public CargaBibliotecas(IClientRepository clientRepository, IGameRepository gameRepository) {
        this.clientRepository = clientRepository;
        this.gameRepository = gameRepository;
    }

    private record Biblioteca(String usuario, List<String> juegos) {}
    // Juegos "comprados" por cada cliente. Incluye los juegos que ya tienen reseña.
    //   cosmejuega   -> A: en GTA V ya reseñó
    //   estebanquito -> B: tiene God of War Ragnarok y NO lo reseñó (puede escribir)
    //   alanbrito    -> C: NO tiene GTA V (debe comprarlo para opinar)
    private static final List<Biblioteca> BIBLIOTECAS = List.of(
new Biblioteca("cosmejuega", List.of(
                "God of War Ragnarok", "Elden Ring", "GTA V", "Resident Evil 4 Remake", "Cyberpunk 2077", "The Last of Us Part I", "Half-Life 2", "Street Fighter 6")),
        new Biblioteca("estebanquito", List.of(
                "GTA V", "Red Dead Redemption 2", "Cyberpunk 2077", "Resident Evil Village", "Resident Evil 2 Remake", "God of War Ragnarok", "Super Mario Odyssey", "The Witcher 3: Wild Hunt", "Elden Ring", "Dead Space Remake")),
        new Biblioteca("facundorios", List.of(
                "The Elder Scrolls V: Skyrim", "Fallout 4", "League of Legends", "Valorant", "Helldivers 2", "Apex Legends", "DOOM Eternal", "Fallout: New Vegas", "Starfield")),
        new Biblioteca("alanbrito", List.of(
                "Super Mario Odyssey", "God of War", "Portal 2", "Half-Life 2", "Resident Evil 4 Remake", "Dark Souls Remastered", "Elden Ring")),
        new Biblioteca("marianodiaz", List.of(
                "Final Fantasy VII Remake", "NieR: Automata", "Mass Effect Legendary Edition", "Starfield", "Final Fantasy XVI", "The Witcher 3: Wild Hunt", "Cyberpunk 2077", "Kingdom Hearts III")),
        new Biblioteca("luciafernandez", List.of(
                "The Witcher 3: Wild Hunt", "Phantom Liberty", "Max Payne 3", "L.A. Noire", "Red Dead Redemption 2", "The Last of Us Part I", "Bloodborne")),
        new Biblioteca("julietalopez", List.of(
                "Fortnite", "Fall Guys", "Brawlhalla", "The Sims 4", "Animal Crossing: New Horizons", "Mario Kart 8 Deluxe")),
        new Biblioteca("martingomez", List.of(
                "Resident Evil 4 Remake", "Devil May Cry 5", "Resident Evil 2 Remake", "Monster Hunter: World", "Street Fighter 6", "God of War Ragnarok", "Sekiro: Shadows Die Twice")),
        new Biblioteca("gonzalosilva", List.of(
                "EA SPORTS FC 26", "Need for Speed Unbound", "eFootball 2024", "Rocket League", "GTA V", "Battlefield 1")),
        new Biblioteca("florenciarossi", List.of(
                "Half-Life 2", "Portal 2", "Counter-Strike 2", "Overwatch 2", "The Sims 4", "Left 4 Dead 2", "Dishonored 2")),
        new Biblioteca("valentinasosa", List.of(
                "Animal Crossing: New Horizons", "Super Smash Bros. Ultimate", "The Legend of Zelda: BotW", "The Legend of Zelda: TotK", "Super Mario Bros. Wonder")),
        new Biblioteca("nicolasacosta", List.of(
                "Dark Souls III", "Dark Souls Remastered", "Sekiro: Shadows Die Twice", "Elden Ring", "Bloodborne", "Armored Core VI", "Dark Souls II: Scholar of the First Sin")),
        new Biblioteca("sofiabenitez", List.of(
                "Spider-Man 2", "Rocket League", "Overwatch 2", "Fall Guys", "Hearthstone")),
        new Biblioteca("joaquinmolina", List.of(
                "The Last of Us Part I", "God of War", "Uncharted: Legacy of Thieves", "God of War III Remastered", "Red Dead Redemption")),
        new Biblioteca("agustinaramirez", List.of(
                "Armored Core VI", "Monster Hunter: World", "Monster Hunter Wilds", "Cyberpunk 2077", "NieR: Automata"))
    );

    @Override
    public void cargar() {
        Map<String, GameEntity> juegos = gameRepository.findAll().stream()
                .collect(Collectors.toMap(GameEntity::getName, Function.identity(), (a, b) -> a));
        Map<String, ClientEntity> clientes = clientRepository.findAll().stream()
                .collect(Collectors.toMap(ClientEntity::getUserName, Function.identity(), (a, b) -> a));

        int agregados = 0;
        for (Biblioteca b : BIBLIOTECAS) {
            ClientEntity cliente = clientes.get(b.usuario());
            if (cliente == null) continue;

            List<GameEntity> biblioteca = new ArrayList<>(cliente.getGames());

            for (String nombre : b.juegos()) {
                GameEntity juego = juegos.get(nombre);
                if (juego == null) continue;

                boolean yaLoTiene = biblioteca.stream().anyMatch(g -> g.getId().equals(juego.getId()));
                if (!yaLoTiene) {
                    biblioteca.add(juego);
                    agregados++;
                }
            }

            cliente.setGames(biblioteca);
            clientRepository.save(cliente);
        }

        System.out.println("    Juegos agregados a bibliotecas: " + agregados);
    }
}