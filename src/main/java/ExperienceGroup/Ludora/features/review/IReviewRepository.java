package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IReviewRepository extends JpaRepository <ReviewEntity, Long>, JpaSpecificationExecutor<ReviewEntity> {

    Optional<ReviewEntity> findByExternalId(UUID externalId);

    List<ReviewEntity> findByClient(ClientEntity client);

    List<ReviewEntity> findByGame(GameEntity game);

    List<ReviewEntity> findByGameAndClient(GameEntity game, ClientEntity client);

}
