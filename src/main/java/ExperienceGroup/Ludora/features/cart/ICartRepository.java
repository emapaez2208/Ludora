package ExperienceGroup.Ludora.features.cart;

import ExperienceGroup.Ludora.features.cart.domain.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartRepository extends JpaRepository<CartEntity,Long> {



}
