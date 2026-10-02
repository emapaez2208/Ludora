package ExperienceGroup.Ludora.features.developer;

import ExperienceGroup.Ludora.features.developer.domain.DeveloperEntity;
import org.springframework.data.jpa.domain.PredicateSpecification;

public class DeveloperSpecification {

    public static PredicateSpecification<DeveloperEntity> nameContains(String name) {
        return (root, cb) -> name == null || name.isBlank()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static PredicateSpecification<DeveloperEntity> lastNameContains(String lastName) {
        return (root, cb) -> lastName == null || lastName.isBlank()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static PredicateSpecification<DeveloperEntity> userNameEquals(String userName) {
        return (root, cb) -> userName == null || userName.isBlank()
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("userName")), userName.toLowerCase());
    }

    public static PredicateSpecification<DeveloperEntity> emailEquals(String email) {
        return (root, cb) -> email == null || email.isBlank()
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("email").get("value")), email.toLowerCase());
    }

    public static PredicateSpecification<DeveloperEntity> statusBlockedEquals(Boolean statusBlocked) {
        return (root, cb) -> statusBlocked == null
                ? cb.conjunction()
                : cb.equal(root.get("statusBlocked"), statusBlocked);
    }

    public static PredicateSpecification<DeveloperEntity> companyContains(String company) {
        return (root, cb) -> company == null || company.isBlank()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("company")), "%" + company.toLowerCase() + "%");
    }
}