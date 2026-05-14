package ExperienceGroup.Ludora.features.admin;

import ExperienceGroup.Ludora.features.admin.domain.AdminEntity;
import org.springframework.data.jpa.domain.PredicateSpecification;

public class AdminSpecification {
    public static PredicateSpecification<AdminEntity> nameContains(String name){
        return (root, cb) -> name == null || name.isBlank()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static PredicateSpecification<AdminEntity> lastNameContains(String lastName){
        return (root, cb) -> lastName == null || lastName.isBlank()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static PredicateSpecification<AdminEntity> userNameEquals(String userName){
        return (root, cb) -> userName == null || userName.isBlank()
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("userName")), userName.toLowerCase());
    }

    public static PredicateSpecification<AdminEntity> statusBlockedEquals(Boolean status){
        return (root, cb) -> status == null
                ? cb.conjunction()
                : cb.equal(root.get("statusBlocked"), status);
    }

    public static PredicateSpecification<AdminEntity> emailEquals(String email){
        return (root, cb) -> email == null || email.isBlank()
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("email").get("value")), email.toLowerCase());
    }

    public static PredicateSpecification<AdminEntity> employeeIdEquals(Long employeeId){
        return (root, cb) -> employeeId == null
                ? cb.conjunction()
                : cb.equal(root.get("employeeId"), employeeId);
    }

}
