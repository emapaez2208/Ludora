package ExperienceGroup.Ludora.features.cart.domain.mapper;

import ExperienceGroup.Ludora.features.cart.domain.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICartRepository extends JpaRepository<CartEntity, Long> {


    Optional<CartEntity> findById(Long cartId);


}
