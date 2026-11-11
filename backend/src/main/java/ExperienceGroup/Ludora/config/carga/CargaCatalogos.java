package ExperienceGroup.Ludora.config.carga;

import ExperienceGroup.Ludora.features.ageRange.IAgeRangeRepository;
import ExperienceGroup.Ludora.features.ageRange.domain.AgeRangeEntity;
import ExperienceGroup.Ludora.features.genre.IGenreRepository;
import ExperienceGroup.Ludora.features.genre.domain.GenreEntity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Order(1)
public class CargaCatalogos implements CargaDatos {

    private final IAgeRangeRepository ageRangeRepository;
    private final IGenreRepository genreRepository;

    public CargaCatalogos(IAgeRangeRepository ageRangeRepository, IGenreRepository genreRepository) {
        this.ageRangeRepository = ageRangeRepository;
        this.genreRepository = genreRepository;
    }

    private record RangoEdad(String nombre, int edadMinima, String descripcion) {}

    private record Genero(String nombre, String descripcion) {}

    private static final List<RangoEdad> RANGOS = List.of(
            new RangoEdad("ATP", 0, "Apta para todo público."),
            new RangoEdad("+13", 13, "Violencia moderada o fantasía."),
            new RangoEdad("+16", 16, "Contenido fuerte o lenguaje explícito."),
            new RangoEdad("+18", 18, "Violencia extrema o madura.")
    );

    private static final List<Genero> GENEROS = List.of(
            new Genero("Action", "Juegos de combate y reflejos."),
            new Genero("RPG", "Juegos de rol y progresión."),
            new Genero("Aventura", "Exploración e historia."),
            new Genero("Shooter", "Acción armada en primera/tercera persona."),
            new Genero("Deporte", "Simulación deportiva."),
            new Genero("Survival Horror", "Supervivencia y terror."),
            new Genero("Estrategia", "Táctica, gestión y toma de decisiones por turnos o en tiempo real."),
            new Genero("Indie", "Títulos independientes con propuestas artísticas y mecánicas únicas."),
            new Genero("Simulación", "Experiencias de recreación de la vida real, vehículos o profesiones."),
            new Genero("Carreras", "Competencias de automovilismo, motos y velocidad arcade o simulador."),
            new Genero("Plataformas", "Saltos, precisión y obstáculos en escenarios 2D y 3D."),
            new Genero("Puzzles", "Rompecabezas, acertijos y resolución lógica."),
            new Genero("Lucha", "Enfrentamientos cuerpo a cuerpo, combos y artes marciales."),
            new Genero("Mundo Abierto", "Mapas masivos y libertad total de desplazamiento y objetivos."),
            new Genero("Multijugador", "Partidas online competitivas y cooperativas con otros jugadores."),
            new Genero("Roguelike", "Generación procedural, muerte permanente y progresión por partidas.")
    );

    @Override
    public void cargar() {
        Set<String> rangosExistentes = ageRangeRepository.findAll().stream()
                .map(AgeRangeEntity::getRangeName)
                .collect(Collectors.toSet());

        int nuevosRangos = 0;
        for (RangoEdad r : RANGOS) {
            if (!rangosExistentes.contains(r.nombre())) {
                ageRangeRepository.save(new AgeRangeEntity(null, null, r.nombre(), r.edadMinima(), r.descripcion(), null));
                nuevosRangos++;
            }
        }

        Set<String> generosExistentes = genreRepository.findAll().stream()
                .map(GenreEntity::getName)
                .collect(Collectors.toSet());

        int nuevosGeneros = 0;
        for (Genero g : GENEROS) {
            if (!generosExistentes.contains(g.nombre())) {
                genreRepository.save(new GenreEntity(null, g.nombre(), g.descripcion()));
                nuevosGeneros++;
            }
        }

        System.out.println("    Rangos de edad nuevos: " + nuevosRangos + " | Géneros nuevos: " + nuevosGeneros);
    }
}