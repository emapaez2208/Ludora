package ExperienceGroup.Ludora.features.genre;

import ExperienceGroup.Ludora.features.genre.domain.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IGenreRepository extends JpaRepository<GenreEntity,Long> {
    Optional<GenreEntity> findByName(String name);

}
