package ExperienceGroup.Ludora.features.game;

import ExperienceGroup.Ludora.features.game.domain.GameEntity;
import org.springframework.data.jpa.domain.PredicateSpecification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GameSpecification {

    public static PredicateSpecification<GameEntity> nameContains(String name){
        return (root, cb) -> name == null || name.isBlank()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static PredicateSpecification<GameEntity> priceLesserThan(BigDecimal max) {
        return (root, cb) ->
                max == null ? null : cb.lessThanOrEqualTo(root.get("price"), max);
    }

    public static PredicateSpecification<GameEntity> priceGreaterThan(BigDecimal min) {
        return (root, cb) ->
                min == null ? null : cb.greaterThanOrEqualTo(root.get("price"), min);
    }

    public static PredicateSpecification<GameEntity> priceBetween(BigDecimal min, BigDecimal max) {
        return (root, cb) -> {
            if (min == null && max == null)
                return cb.conjunction();

            if (max == null)
                return cb.greaterThanOrEqualTo(root.get("price"), min);

            if (min == null)
                return cb.lessThanOrEqualTo(root.get("price"), max);

            return cb.between(root.get("price"), min, max);
        };
    }

    public static PredicateSpecification<GameEntity> releaseDateAfter(LocalDate minReleaseDate) {
        return (root, cb) ->
                minReleaseDate == null ? null : cb.greaterThanOrEqualTo(root.get("releaseDate"), minReleaseDate);
    }

    public static PredicateSpecification<GameEntity> releaseDateBefore(LocalDate maxReleaseDate) {
        return (root, cb) ->
                maxReleaseDate == null ? null : cb.lessThanOrEqualTo(root.get("releaseDate"), maxReleaseDate);
    }

    public static PredicateSpecification<GameEntity> releaseDateBetween(LocalDate min, LocalDate max) {
        return (root, cb) -> {
            if (min == null && max == null)
                return cb.conjunction();

            if (max == null)
                return cb.greaterThanOrEqualTo(root.get("releaseDate"), min);

            if (min == null)
                return cb.lessThanOrEqualTo(root.get("releaseDate"), max);

            return cb.between(root.get("releaseDate"), min, max);
        };
    }

    public static PredicateSpecification<GameEntity> statusBlockedEquals(Boolean status){
        return (root, cb) -> status == null
                ? cb.conjunction()
                : cb.equal(root.get("statusBlocked"), status);
    }

    public static PredicateSpecification<GameEntity> hasGenreName(String genreName) {
        return (root, cb) -> {
            if (genreName == null || genreName.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(cb.lower(root.join("genres").get("name")), genreName.toLowerCase());
        };
    }

    public static PredicateSpecification<GameEntity> hasAgeRangeName(String rangeName){
        return (root, cb) -> rangeName == null || rangeName.isBlank()
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("ageRange").get("rangeName")), rangeName.toLowerCase());
    }

    public static PredicateSpecification<GameEntity> hasDeveloperName(String developerCompany) {
        return (root, cb) -> (developerCompany == null || developerCompany.isBlank())
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("developer").get("company")), developerCompany.toLowerCase());
    }

}
