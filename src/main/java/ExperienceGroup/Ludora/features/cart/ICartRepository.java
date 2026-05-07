package ExperienceGroup.Ludora.features.cart;

import ExperienceGroup.Ludora.features.cart.domain.CartEntity;
import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import ExperienceGroup.Ludora.features.user.domain.dto.UserDTOResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICartRepository extends JpaRepository<CartEntity,Long> {

    Optional<CartEntity> findByExternalId(UUID externalId);

    Optional<CartEntity> findByUser(UserDTOResponse userDTOResponse);


}
