package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IReviewRepository extends JpaRepository <ReviewEntity, Long>, JpaSpecificationExecutor<ReviewEntity> {

    Optional<ReviewEntity> findByExternalId(UUID externalId);

    Page<ReviewEntity> findByClient(ClientEntity client, Pageable pageable);

    Page<ReviewEntity> findByGame(GameEntity game, Pageable pageable);

    Page<ReviewEntity> findByGameAndClient(GameEntity game, ClientEntity client, Pageable pageable);

}
