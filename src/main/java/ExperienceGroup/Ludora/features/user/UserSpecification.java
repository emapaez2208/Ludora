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

    public static PredicateSpecification<UserEntity> userNameEquals(String userName){
        return (root, cb) -> userName == null || userName.isBlank()
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("userName")), userName.toLowerCase());
    }

    public static PredicateSpecification<UserEntity> statusBlockedEquals(Boolean status){
        return (root, cb) -> status == null
                ? cb.conjunction()
                : cb.equal(root.get("statusBlocked"), status);
    }

    public static PredicateSpecification<UserEntity> emailEquals(String email){
        return (root, cb) -> email == null || email.isBlank()
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("email").get("value")), email.toLowerCase());
    }

    public static PredicateSpecification<UserEntity> roleEquals(String role){
        return (root, cb) -> role == null || role.isBlank()
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("role").get("name")), role.toLowerCase());
    }
}
