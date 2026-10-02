package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import org.springframework.data.jpa.domain.PredicateSpecification;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewSpecification {

    public static PredicateSpecification<ReviewEntity> externalIdEquals (UUID id){
        return (root, cb) -> id == null
                ? cb.conjunction()
                : cb.equal(root.get("externalId"), id);
    }

    public static PredicateSpecification<ReviewEntity> ratingGreaterThan(Integer min){
        return (root, cb) -> min == null
                ? cb.conjunction()
                : cb.greaterThanOrEqualTo(root.get("rating"), min);
    }

    public static PredicateSpecification<ReviewEntity> ratingLessThan(Integer max){
        return (root, cb) -> max == null
                ? cb.conjunction()
                : cb.lessThanOrEqualTo(root.get("rating"), max);
    }

    public static PredicateSpecification<ReviewEntity> ratingBetween(Integer min, Integer max){
        return (root, cb) -> {
            if(min == null && max == null) return cb.conjunction();
            if(min == null) return cb.lessThanOrEqualTo(root.get("rating"), max);
            if(max == null) return cb.greaterThanOrEqualTo(root.get("rating"), min);
            return cb.between(root.get("rating"), min, max);
        };
    }

    public static PredicateSpecification<ReviewEntity> dateGreaterThan(LocalDateTime min) {
        return (root, cb) -> min == null
                ? cb.conjunction()
                : cb.greaterThanOrEqualTo(root.get("date"), min);
    }

    public static PredicateSpecification<ReviewEntity> dateLessThan(LocalDateTime max) {
        return (root, cb) -> max == null
                ? cb.conjunction()
                : cb.lessThanOrEqualTo(root.get("date"), max);
    }

    public static PredicateSpecification<ReviewEntity> dateBetween(LocalDateTime min, LocalDateTime max) {
        return (root, cb) -> {
            if (min == null && max == null) return cb.conjunction();
            if (min == null) return cb.lessThanOrEqualTo(root.get("date"), max);
            if (max == null) return cb.greaterThanOrEqualTo(root.get("date"), min);
            return cb.between(root.get("date"), min, max);
        };
    }

    public static PredicateSpecification<ReviewEntity> gameEquals(UUID game) {
        return (root, cb) -> game == null
                ? cb.conjunction()
                : cb.equal(root.join("game").get("externalId"), game);
    }

    public static PredicateSpecification<ReviewEntity> clientEquals(UUID client) {
        return (root, cb) -> client == null
                ? cb.conjunction()
                : cb.equal(root.join("client").get("externalId"), client);
    }
}

