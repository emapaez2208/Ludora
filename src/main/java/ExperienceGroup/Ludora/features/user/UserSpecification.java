package ExperienceGroup.Ludora.features.user;

import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import org.springframework.data.jpa.domain.PredicateSpecification;

public class UserSpecification {

    public static PredicateSpecification<UserEntity> nameContains(String name){
        return (root, cb) -> name == null || name.isBlank()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static PredicateSpecification<UserEntity> lastNameContains(String lastName){
        return (root, cb) -> lastName == null || lastName.isBlank()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }


}
