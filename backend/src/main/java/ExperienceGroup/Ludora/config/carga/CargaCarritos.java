package ExperienceGroup.Ludora.config.carga;

import ExperienceGroup.Ludora.features.cart.ICartRepository;
import ExperienceGroup.Ludora.features.cart.domain.CartEntity;
import ExperienceGroup.Ludora.features.client.IClientRepository;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.game.IGameRepository;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(6)
public class CargaCarritos implements CargaDatos {

    private final ICartRepository cartRepository;
    private final IClientRepository clientRepository;
    private final IGameRepository gameRepository;

    public CargaCarritos(ICartRepository cartRepository,
                         IClientRepository clientRepository,
                         IGameRepository gameRepository) {
        this.cartRepository = cartRepository;
        this.clientRepository = clientRepository;
        this.gameRepository = gameRepository;
    }

    private record Carrito(String usuario, List<String> juegos) {}

    // Solo se crea si el cliente todavía no tiene carrito
    private static final List<Carrito> CARRITOS = List.of(
            new Carrito("alanbrito", List.of("God of War Ragnarok", "Red Dead Redemption 2", "GTA V")),
            new Carrito("cosmejuega", List.of("Spider-Man 2", "The Last of Us Part I", "God of War")),
            new Carrito("estebanquito", List.of("GTA IV", "L.A. Noire", "Uncharted: Legacy of Thieves"))
    );

    @Override
    public void cargar() {
        List<GameEntity> todosLosJuegos = gameRepository.findAll();
        List<ClientEntity> clientes = clientRepository.findAll();
        int creados = 0;

        for (Carrito c : CARRITOS) {
            ClientEntity cliente = clientes.stream()
                    .filter(cl -> cl.getUserName().equals(c.usuario()))
                    .findFirst().orElse(null);

            if (cliente == null || cartRepository.findByClient_ExternalId(cliente.getExternalId()).isPresent()) {
                continue;
            }

            List<GameEntity> juegos = todosLosJuegos.stream()
                    .filter(g -> c.juegos().contains(g.getName()))
                    .toList();

            CartEntity cart = new CartEntity();
            cart.setClient(cliente);
            cart.setGames(new ArrayList<>(juegos));
            cart.setTotalPrice(juegos.stream()
                    .map(GameEntity::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            cartRepository.save(cart);
            creados++;
        }

        System.out.println("    Carritos nuevos: " + creados);
    }
}