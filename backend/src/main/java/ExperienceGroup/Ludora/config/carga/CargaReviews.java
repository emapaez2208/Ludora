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
            new Resena("GTA V", "cosmejuega", 3, "El juego base está bien, pero el online está lleno de hackers.", Duration.ofHours(5)),
            new Resena("GTA V", "estebanquito", 5, "Un clásico. No me canso de recorrer Los Santos y hacer misiones.", Duration.ofDays(1)),
            new Resena("Super Mario Odyssey", "estebanquito", 5, "Mágico de principio a fin, puro estilo clásico de Nintendo.", Duration.ofHours(3)),
            new Resena("Super Mario Odyssey", "alanbrito", 4, "Muy divertido para jugar en familia, los controles son una delicia.", Duration.ofHours(12)),
            new Resena("Red Dead Redemption 2", "estebanquito", 5, "La mejor historia y nivel de detalle del mundo abierto jamás creado.", Duration.ofDays(3)),
            new Resena("Elden Ring", "cosmejuega", 5, "Obra maestra absoluta. Morí 200 veces contra Malenia pero valió cada segundo.", Duration.ofDays(1)),
            new Resena("Elden Ring", "alanbrito", 2, "Demasiado difícil y no te explica nada. La ambientación es genial pero frustra.", Duration.ofDays(4)),
            new Resena("Cyberpunk 2077", "estebanquito", 4, "Después de los parches quedó tremendo juego. Night City no tiene comparación.", Duration.ofHours(8)),
            new Resena("Resident Evil 4 Remake", "cosmejuega", 5, "El mejor remake de la historia. Respetaron el clásico y modernizaron el combate a la perfección.", Duration.ofDays(6)),
            new Resena("Dark Souls Remastered", "alanbrito", 1, "Me caí en Blighttown cinco veces seguidas y desinstalé el juego. No es para mí.", Duration.ofHours(18))
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