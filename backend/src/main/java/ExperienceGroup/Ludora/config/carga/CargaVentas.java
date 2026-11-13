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
            // Ventas históricas (30 a 60 días atrás)
        new VentaPrueba("cosmejuega",      ESaleStatus.APPROVED, Duration.ofDays(58), List.of("God of War Ragnarok", "Elden Ring")),
        new VentaPrueba("estebanquito",    ESaleStatus.APPROVED, Duration.ofDays(52), List.of("GTA V", "Red Dead Redemption 2", "Cyberpunk 2077")),
        new VentaPrueba("facundorios",     ESaleStatus.APPROVED, Duration.ofDays(48), List.of("The Elder Scrolls V: Skyrim", "Fallout 4")),
        new VentaPrueba("alanbrito",       ESaleStatus.APPROVED, Duration.ofDays(45), List.of("Super Mario Odyssey")),
        new VentaPrueba("marianodiaz",     ESaleStatus.APPROVED, Duration.ofDays(41), List.of("Final Fantasy VII Remake", "NieR: Automata")),
        new VentaPrueba("luciafernandez",  ESaleStatus.APPROVED, Duration.ofDays(37), List.of("The Witcher 3: Wild Hunt", "Phantom Liberty")),
        new VentaPrueba("julietalopez",    ESaleStatus.APPROVED, Duration.ofDays(33), List.of("Fortnite", "Fall Guys")),
        new VentaPrueba("martingomez",     ESaleStatus.APPROVED, Duration.ofDays(30), List.of("Resident Evil 4 Remake", "Devil May Cry 5")),

        // Ventas intermedias (15 a 30 días atrás)
        new VentaPrueba("gonzalosilva",    ESaleStatus.APPROVED, Duration.ofDays(28), List.of("EA SPORTS FC 26", "Need for Speed Unbound")),
        new VentaPrueba("camilarodriguez", ESaleStatus.REJECTED, Duration.ofDays(26), List.of("Mario Kart 8 Deluxe")),
        new VentaPrueba("florenciarossi",  ESaleStatus.APPROVED, Duration.ofDays(24), List.of("Half-Life 2", "Portal 2", "Counter-Strike 2")),
        new VentaPrueba("valentinasosa",   ESaleStatus.APPROVED, Duration.ofDays(22), List.of("Animal Crossing: New Horizons", "Super Smash Bros. Ultimate")),
        new VentaPrueba("nicolasacosta",   ESaleStatus.APPROVED, Duration.ofDays(19), List.of("Dark Souls III", "Dark Souls Remastered", "Sekiro: Shadows Die Twice")),
        new VentaPrueba("lucasmendoza",    ESaleStatus.REJECTED, Duration.ofDays(17), List.of("DOOM Eternal")),
        new VentaPrueba("sofiabenitez",    ESaleStatus.APPROVED, Duration.ofDays(16), List.of("Spider-Man 2", "Rocket League")),

        // Ventas recientes (1 a 14 días atrás)
        new VentaPrueba("joaquinmolina",   ESaleStatus.APPROVED, Duration.ofDays(14), List.of("The Last of Us Part I", "God of War", "Uncharted: Legacy of Thieves")),
        new VentaPrueba("facundorios",     ESaleStatus.APPROVED, Duration.ofDays(12), List.of("League of Legends", "Valorant")),
        new VentaPrueba("agustinaramirez", ESaleStatus.APPROVED, Duration.ofDays(10), List.of("Armored Core VI", "Monster Hunter: World")),
        new VentaPrueba("marianodiaz",     ESaleStatus.APPROVED, Duration.ofDays(8),  List.of("Mass Effect Legendary Edition", "Starfield")),
        new VentaPrueba("cosmejuega",      ESaleStatus.REJECTED, Duration.ofDays(7),  List.of("GTA IV")),
        new VentaPrueba("florenciarossi",  ESaleStatus.APPROVED, Duration.ofDays(5),  List.of("Overwatch 2", "The Sims 4")),
        new VentaPrueba("estebanquito",    ESaleStatus.APPROVED, Duration.ofDays(4),  List.of("Resident Evil Village", "Resident Evil 2 Remake")),
        new VentaPrueba("gonzalosilva",    ESaleStatus.PENDING,  Duration.ofDays(3),  List.of("Street Fighter 6", "eFootball 2024")),
        new VentaPrueba("julietalopez",    ESaleStatus.APPROVED, Duration.ofDays(2),  List.of("Brawlhalla")),

        // Ventas del día (Últimas horas)
        new VentaPrueba("alanbrito",       ESaleStatus.PENDING,  Duration.ofHours(18), List.of("The Witcher 2", "The Witcher 1", "L.A. Noire")),
        new VentaPrueba("luciafernandez",  ESaleStatus.APPROVED, Duration.ofHours(12), List.of("Max Payne 3")),
        new VentaPrueba("facundorios",     ESaleStatus.APPROVED, Duration.ofHours(5),  List.of("Helldivers 2", "Apex Legends")),
        new VentaPrueba("marianodiaz",     ESaleStatus.PENDING,  Duration.ofHours(1),  List.of("Final Fantasy XVI", "Kingdom Hearts III"))
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