package ExperienceGroup.Ludora.features.genre;

import ExperienceGroup.Ludora.features.genre.domain.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface IGenreRepository extends JpaRepository<GenreEntity,Long> {
    Optional<GenreEntity> findByName(String name);

    Optional<GenreEntity> findByDescription(String description);

    Optional<List<GenreEntity>> findAllByName(List<String> name);

}
