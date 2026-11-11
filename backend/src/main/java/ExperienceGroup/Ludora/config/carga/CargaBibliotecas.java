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
                    "God of War Ragnarok", "GTA V", "Elden Ring", "Resident Evil 4 Remake")),
            new Biblioteca("estebanquito", List.of(
                    "GTA V", "Super Mario Odyssey", "God of War Ragnarok", "Red Dead Redemption 2", "Cyberpunk 2077")),
            new Biblioteca("alanbrito", List.of(
                    "God of War Ragnarok", "Super Mario Odyssey", "Elden Ring", "Dark Souls Remastered"))
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