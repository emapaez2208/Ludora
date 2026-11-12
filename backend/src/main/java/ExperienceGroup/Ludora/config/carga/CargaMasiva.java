package ExperienceGroup.Ludora.config.carga;

import ExperienceGroup.Ludora.auth.credentials.CredentialsEntity;
import ExperienceGroup.Ludora.auth.credentials.CredentialsRepository;
import ExperienceGroup.Ludora.auth.permissions.RoleEntity;
import ExperienceGroup.Ludora.auth.permissions.RoleRepository;
import ExperienceGroup.Ludora.auth.permissions.RolesEnum;
import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.features.client.IClientRepository;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.game.IGameRepository;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.review.IReviewRepository;
import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**Genera volumen: clientes, bibliotecas y reseñas al azar pero COHERENTES(un cliente solo reseña juegos que tiene en su biblioteca). Los clientes que ya existen se saltean. */
@Component
@Order(8)
public class CargaMasiva implements CargaDatos {

    // ---- cambia estos números para generar más o menos datos ----
    private static final int CANTIDAD_CLIENTES = 60;
    private static final int MIN_JUEGOS_POR_CLIENTE = 3;
    private static final int MAX_JUEGOS_POR_CLIENTE = 10;
    private static final double PROBABILIDAD_RESENA = 0.65; // de los juegos que tiene, cuántos reseña
    private static final int DIAS_HACIA_ATRAS = 90;          // las reseñas se reparten en este período
    private static final String PASSWORD = "Test1234";       // la misma para todos los generados

    private static final String[] NOMBRES = {
            "Lucas", "Mateo", "Sofía", "Valentina", "Joaquín", "Martina", "Santiago", "Camila", "Facundo", "Julieta",
            "Bruno", "Agustín", "Florencia", "Nahuel", "Milagros", "Franco", "Rocío", "Ignacio", "Carolina", "Gonzalo",
            "Paula", "Emiliano", "Daniela", "Federico", "Micaela", "Ramiro", "Lucía", "Tomás", "Abril", "Leandro", "Nicolás"
    };

    private static final String[] APELLIDOS = {
            "Gutiérrez", "López", "Díaz", "Torres", "Romero", "Álvarez", "Ruiz", "Suárez", "Herrera", "Medina",
            "Castro", "Ortiz", "Silva", "Navarro", "Peralta", "Ríos", "Vega", "Domínguez", "Aguirre", "Cabrera",
            "Moreno", "Giménez", "Luna", "Ledesma", "Paz", "Bustos", "Correa", "Villalba", "Funes", "Acuña"
    };

    // La columna street admite hasta 20 caracteres
    private static final String[] CALLES = {
            "Av. Colón", "San Martín", "Rivadavia", "Belgrano", "Güemes", "Alberti", "Luro", "Falucho",
            "Av. Independencia", "Av. Constitución", "Mitre", "Sarmiento", "Moreno", "Jujuy", "Córdoba",
            "Entre Ríos", "Av. Libertad", "Italia"
    };

    // Incluye valores alrededor del umbral de descuento (10000 puntos)
    private static final int[] PUNTOS = {0, 50, 300, 1200, 5000, 9800, 10000, 15000, 60000};

    private static final String[] COMENTARIOS_POSITIVOS = {
            "Excelente de principio a fin, lo recomiendo sin dudar.",
            "Gráficos, historia y jugabilidad al máximo nivel.",
            "Me enganchó desde la primera hora y no pude soltarlo.",
            "Vale cada centavo. Una de las mejores experiencias que jugué.",
            "Gran trabajo del estudio, se nota el cuidado en cada detalle.",
            "Ideal para jugar con amigos, horas de diversión asegurada.",
            "La banda sonora y la ambientación son increíbles.",
            "Rejugable y con mucho contenido. Totalmente recomendable.",
            "Obra maestra absoluta, superó todas mis expectativas.",
            "Un vicio total, se te pasan las horas volando sin darte cuenta.",
            "De lo mejor que salió en los últimos años, una joya imperdible.",
            "Controles súper fluidos y una curva de aprendizaje perfecta.",
            "Lo volvería a jugar mil veces más, tiene una rejugabilidad tremenda."
    };

    private static final String[] COMENTARIOS_MIXTOS = {
            "Está bien, pero esperaba un poco más por lo que cuesta.",
            "Tiene cosas geniales y otras que podrían mejorar.",
            "Divertido por momentos, repetitivo en otros.",
            "Buen juego, aunque algunos controles no son muy cómodos.",
            "Cumple, pero no me terminó de convencer.",
            "Entretenido para pasar el rato, sin llegar a ser increíble.",
            "Arranca muy fuerte, pero hacia el final se vuelve un poco pesado.",
            "Visualmente es hermoso, aunque en lo jugable se siente algo genérico.",
            "Si lo agarrás en oferta vale la pena, a precio completo lo dudaría.",
            "Tiene muy buenas ideas, pero le falta un poco de pulido general.",
            "Me gustó la propuesta, aunque la historia deja varios cabos sueltos."
    };

    private static final String[] COMENTARIOS_NEGATIVOS = {
            "No me gustó, se me hizo aburrido y repetitivo.",
            "Tiene muchos errores y la optimización es pésima.",
            "Esperaba mucho más, una decepción para mí.",
            "Demasiado difícil y poco amigable con los nuevos jugadores.",
            "No lo recomiendo, el precio no se justifica.",
            "La historia no me atrapó y los controles son torpes.",
            "Lo terminé desinstalando a las pocas horas, no me enganchó para nada.",
            "Pésimo rendimiento y caídas de FPS constantes incluso en buena PC.",
            "Se siente incompleto, parece que lo sacaron apurados.",
            "La inteligencia artificial es muy básica y arruina la inmersión.",
            "Una pérdida de tiempo y plata, hay opciones mucho mejores del mismo género."
    };

    private final IClientRepository clientRepository;
    private final IGameRepository gameRepository;
    private final IReviewRepository reviewRepository;
    private final CredentialsRepository credentialsRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public CargaMasiva(IClientRepository clientRepository,
                       IGameRepository gameRepository,
                       IReviewRepository reviewRepository,
                       CredentialsRepository credentialsRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.gameRepository = gameRepository;
        this.reviewRepository = reviewRepository;
        this.credentialsRepository = credentialsRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    private record ResenaGenerada(GameEntity juego, int estrellas, String comentario, LocalDateTime fecha) {}
    @Override
    public void cargar() {
        RoleEntity rolCliente = roleRepository.findByRole(RolesEnum.ROLE_CLIENT)
                .orElseThrow(() -> new RuntimeException("Error: No se encontró el Rol CLIENT en las tablas de permisos."));

        // Solo juegos visibles: ni bloqueados ni en revisión (ordenados por id, los primeros son los "más populares")
        List<GameEntity> juegos = gameRepository.findAll().stream()
                .filter(g -> Boolean.FALSE.equals(g.getStatusBlocked()) && Boolean.FALSE.equals(g.getNeedRevision()))
                .sorted(Comparator.comparing(GameEntity::getId))
                .toList();
        if (juegos.isEmpty()) {
            System.out.println("    No hay juegos visibles, se omite la carga masiva.");
            return;
        }
        Set<String> existentes = clientRepository.findAll().stream()
                .map(ClientEntity::getUserName)
                .collect(Collectors.toSet());

        Random azar = new Random();
        String hash = passwordEncoder.encode(PASSWORD); // se encripta una sola vez para todos (BCrypt es lento)
        LocalDateTime ahora = LocalDateTime.now();

        int clientesNuevos = 0;
        int resenasNuevas = 0;
        String ejemplo = null;

            for (int i = 1; i <= CANTIDAD_CLIENTES; i++) {
            String nombre = NOMBRES[i % NOMBRES.length];
            String apellido = APELLIDOS[(i * 7) % APELLIDOS.length];
            String userName = sinTildes(nombre + apellido).toLowerCase() + i;

            if (existentes.contains(userName)) continue;

            String calle = elegir(CALLES, azar);
            int numeroCalle = 100 + azar.nextInt(5900);
            LocalDate nacimiento = LocalDate.of(1980 + azar.nextInt(26), 1 + azar.nextInt(12), 1 + azar.nextInt(28));
            int puntos = PUNTOS[azar.nextInt(PUNTOS.length)];
            boolean bloqueado = azar.nextInt(15) == 0;

            List<GameEntity> biblioteca = sortearJuegos(juegos, azar);

            List<ResenaGenerada> resenas = new ArrayList<>();
            for (GameEntity juego : biblioteca) {
                if (azar.nextDouble() < PROBABILIDAD_RESENA) {
                    int estrellas = estrellasAlAzar(azar);
                    resenas.add(new ResenaGenerada(
                            juego,
                            estrellas,
                            comentarioPara(estrellas, azar),
                            ahora.minusMinutes(azar.nextInt(DIAS_HACIA_ATRAS * 24 * 60))));
                }
            }
            ClientEntity cliente = new ClientEntity();
            cliente.setExternalId(UUID.randomUUID());
            cliente.setName(nombre);
            cliente.setLastName(apellido);
            cliente.setUserName(userName);
            cliente.setEmail(new Email(userName + "@test.com"));
            cliente.setStatusBlocked(bloqueado);
            cliente.setPhone(2235000000L + i);
            cliente.setStreet(calle);
            cliente.setNumberStreet(numeroCalle);
            cliente.setBirthDate(nacimiento);
            cliente.setPoints(puntos);
            cliente.setGames(new ArrayList<>(biblioteca));
            cliente = clientRepository.save(cliente);

            credentialsRepository.save(CredentialsEntity.builder()
                    .externalId(cliente.getExternalId())
                    .username(cliente.getUserName())
                    .password(hash)
                    .enabled(true)
                    .accountNonLocked(true)
                    .user(cliente)
                    .roles(Set.of(rolCliente))
                    .build());
            for (ResenaGenerada r : resenas) {
                ReviewEntity review = new ReviewEntity();
                review.setRating(r.estrellas());
                review.setComment(r.comentario());
                review.setDate(r.fecha());
                review.setGame(r.juego());
                review.setClient(cliente);
                reviewRepository.save(review);
                resenasNuevas++;
            }
            if (ejemplo == null) ejemplo = userName;
            clientesNuevos++;
        }
        System.out.println("    Clientes generados: " + clientesNuevos + " | Reseñas generadas: " + resenasNuevas);
        if (ejemplo != null) {
            System.out.println("    Ejemplo de usuario generado: " + ejemplo + " | Password: " + PASSWORD
                    + " (todos los generados usan la misma password)");
        }
    }
    // Elige entre MIN y MAX juegos distintos. Los primeros de la lista salen más seguido, así algunos juegos juntan muchas reseñas (sirve para probar paginación) y otros pocas o ninguna.
    private List<GameEntity> sortearJuegos(List<GameEntity> juegos, Random azar) {
        int cantidad = Math.min(juegos.size(),
                MIN_JUEGOS_POR_CLIENTE + azar.nextInt(MAX_JUEGOS_POR_CLIENTE - MIN_JUEGOS_POR_CLIENTE + 1));
        Set<Integer> indices = new LinkedHashSet<>();
        while (indices.size() < cantidad) {
            indices.add((int) (juegos.size() * Math.pow(azar.nextDouble(), 1.8)));
        }
        List<GameEntity> elegidos = new ArrayList<>();
        for (int indice : indices) {
            elegidos.add(juegos.get(indice));
        }
        return elegidos;
    }
    // Más 4 y 5 estrellas que 1 y 2, como en la realidad
    private int estrellasAlAzar(Random azar) {
        double x = azar.nextDouble();
        if (x < 0.07) return 1;
        if (x < 0.17) return 2;
        if (x < 0.37) return 3;
        if (x < 0.68) return 4;
        return 5;
    }
    private String comentarioPara(int estrellas, Random azar) {
        if (estrellas >= 4) return elegir(COMENTARIOS_POSITIVOS, azar);
        if (estrellas == 3) return elegir(COMENTARIOS_MIXTOS, azar);
        return elegir(COMENTARIOS_NEGATIVOS, azar);
    }
    private static String elegir(String[] opciones, Random azar) {
        return opciones[azar.nextInt(opciones.length)];
    }
    private static String sinTildes(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }
}