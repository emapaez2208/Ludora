package ExperienceGroup.Ludora.config.carga;

import ExperienceGroup.Ludora.common.utils.BusinessRules;
import ExperienceGroup.Ludora.features.client.IClientRepository;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.game.IGameRepository;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.sale.ESaleStatus;
import ExperienceGroup.Ludora.features.sale.ISaleRepository;
import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import ExperienceGroup.Ludora.features.sale.domain.SaleItemEntity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(7)
public class CargaVentas implements CargaDatos {

    private final ISaleRepository saleRepository;
    private final IClientRepository clientRepository;
    private final IGameRepository gameRepository;

    public CargaVentas(ISaleRepository saleRepository,
                       IClientRepository clientRepository,
                       IGameRepository gameRepository) {
        this.saleRepository = saleRepository;
        this.clientRepository = clientRepository;
        this.gameRepository = gameRepository;
    }

    // antiguedad = hace cuánto se hizo la venta
    private record VentaPrueba(String usuario, ESaleStatus status, Duration antiguedad, List<String> juegos) {}

    // Fechas repartidas en ~45 días, con estados mezclados
    private static final List<VentaPrueba> VENTAS = List.of(
            new VentaPrueba("cosmejuega",      ESaleStatus.APPROVED, Duration.ofDays(45), List.of("God of War Ragnarok", "Elden Ring")),
            new VentaPrueba("estebanquito",    ESaleStatus.APPROVED, Duration.ofDays(38), List.of("GTA V", "Red Dead Redemption 2", "Cyberpunk 2077")),
            new VentaPrueba("alanbrito",       ESaleStatus.APPROVED, Duration.ofDays(30), List.of("Super Mario Odyssey")),
            new VentaPrueba("luciafernandez",  ESaleStatus.APPROVED, Duration.ofDays(27), List.of("The Witcher 3: Wild Hunt", "Phantom Liberty")),
            new VentaPrueba("martingomez",     ESaleStatus.APPROVED, Duration.ofDays(21), List.of("Resident Evil 4 Remake", "Devil May Cry 5")),
            new VentaPrueba("camilarodriguez", ESaleStatus.REJECTED, Duration.ofDays(18), List.of("Mario Kart 8 Deluxe")),
            new VentaPrueba("valentinasosa",   ESaleStatus.APPROVED, Duration.ofDays(15), List.of("Animal Crossing: New Horizons", "Super Smash Bros. Ultimate")),
            new VentaPrueba("nicolasacosta",   ESaleStatus.APPROVED, Duration.ofDays(12), List.of("Dark Souls III", "Dark Souls Remastered", "Sekiro: Shadows Die Twice")),
            new VentaPrueba("sofiabenitez",    ESaleStatus.PENDING,  Duration.ofDays(9),  List.of("Spider-Man 2")),
            new VentaPrueba("joaquinmolina",   ESaleStatus.APPROVED, Duration.ofDays(7),  List.of("The Last of Us Part I", "God of War", "Uncharted: Legacy of Thieves")),
            new VentaPrueba("agustinaramirez", ESaleStatus.APPROVED, Duration.ofDays(5),  List.of("Armored Core VI", "Monster Hunter: World")),
            new VentaPrueba("cosmejuega",      ESaleStatus.REJECTED, Duration.ofDays(3),  List.of("GTA IV")),
            new VentaPrueba("estebanquito",    ESaleStatus.APPROVED, Duration.ofDays(2),  List.of("Resident Evil Village", "Resident Evil 2 Remake")),
            new VentaPrueba("alanbrito",       ESaleStatus.PENDING,  Duration.ofDays(1),  List.of("The Witcher 2", "The Witcher 1", "L.A. Noire")),
            new VentaPrueba("luciafernandez",  ESaleStatus.APPROVED, Duration.ofHours(2), List.of("Max Payne 3"))
    );

    @Override
    public void cargar() {
        // Solo se cargan si la tabla de ventas está vacía
        if (saleRepository.count() > 0) {
            System.out.println("    Ya hay ventas cargadas, se omite.");
            return;
        }

        LocalDateTime ahora = LocalDateTime.now();
        List<ClientEntity> clientes = clientRepository.findAll();
        List<GameEntity> juegosVenta = gameRepository.findAll();
        int cargadas = 0;

        for (VentaPrueba v : VENTAS) {
            ClientEntity cliente = clientes.stream()
                    .filter(c -> c.getUserName().equals(v.usuario()))
                    .findFirst().orElse(null);

            List<GameEntity> juegos = juegosVenta.stream()
                    .filter(g -> v.juegos().contains(g.getName()))
                    .toList();

            if (cliente == null || juegos.isEmpty()) continue;

            saleRepository.save(armarVenta(cliente, v, ahora.minus(v.antiguedad()), juegos));
            cargadas++;
        }

        System.out.println("    Ventas nuevas: " + cargadas);
    }

    // Arma la venta igual que SaleService.create: descuento por puntos, precio por ítem, total y puntos ganados
    private static SaleEntity armarVenta(ClientEntity cliente, VentaPrueba v, LocalDateTime fecha, List<GameEntity> juegos) {
        boolean conDescuento = cliente.getPoints() >= BusinessRules.POINTS_THRESHOLD;

        SaleEntity venta = new SaleEntity();
        venta.setClient(cliente);
        venta.setStatus(v.status());
        venta.setDate(fecha);
        venta.setHasDiscount(conDescuento);

        List<SaleItemEntity> items = new ArrayList<>();
        for (GameEntity juego : juegos) {
            BigDecimal precio = conDescuento
                    ? juego.getPrice().multiply(BigDecimal.ONE.subtract(BusinessRules.DISCOUNT_PERCENTAGE))
                            .setScale(2, RoundingMode.HALF_UP)
                    : juego.getPrice().setScale(2, RoundingMode.HALF_UP);

            SaleItemEntity item = new SaleItemEntity();
            item.setSale(venta);
            item.setGame(juego);
            item.setPriceAtSale(precio);
            items.add(item);
        }

        BigDecimal total = items.stream()
                .map(SaleItemEntity::getPriceAtSale)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        venta.setItems(items);
        venta.setTotalPrice(total);
        venta.setEarnedPoints(total.multiply(BusinessRules.REWARD_POINTS_PERCENTAGE)
                .setScale(0, RoundingMode.FLOOR)
                .intValue());

        return venta;
    }
}