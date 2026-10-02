package ExperienceGroup.Ludora.features.sale;

import ExperienceGroup.Ludora.features.sale.domain.SaleEntity;
import org.springframework.data.jpa.domain.PredicateSpecification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class SaleSpecification {

    public static PredicateSpecification<SaleEntity> externalIdEquals(UUID id){
        return (root, cb) -> id == null
                ? cb.conjunction()
                : cb.equal(root.get("externalId"), id);
    }

    public static PredicateSpecification<SaleEntity> dateGreaterThan(LocalDateTime minDate){
        return (root, cb) -> minDate == null
                ? cb.conjunction()
                : cb.greaterThanOrEqualTo(root.get("date"), minDate);
    }

    public static PredicateSpecification<SaleEntity> dateLessThan (LocalDateTime maxDate){
        return (root, cb) -> maxDate == null
                ? cb.conjunction()
                : cb.lessThanOrEqualTo(root.get("date"), maxDate);
    }

    public static PredicateSpecification<SaleEntity> statusEquals(ESaleStatus status){
        return (root, cb) -> status == null
                ? cb.conjunction()
                : cb.equal(root.get("status"), status);
    }

    public static PredicateSpecification<SaleEntity> totalPriceGreaterThan(BigDecimal minPrice){
        return (root, cb) -> minPrice == null
                ? cb.conjunction()
                : cb.greaterThanOrEqualTo(root.get("totalPrice"), minPrice);
    }

    public static PredicateSpecification<SaleEntity> totalPriceLessThan(BigDecimal maxPrice){
        return (root, cb) -> maxPrice == null
                ? cb.conjunction()
                : cb.lessThanOrEqualTo(root.get("totalPrice"), maxPrice);
    }

    public static PredicateSpecification<SaleEntity> clientEquals(List<UUID> clientId){
        return (root, cb) -> clientId == null
                ? cb.conjunction()
                : root.join("client").get("externalId").in(clientId);
    }

    public static PredicateSpecification<SaleEntity> gamesEquals(List<UUID> gamesId){
        return (root, cb) -> gamesId == null
                ? cb.conjunction()
                : root.join("games").get("externalId").in(gamesId);
    }
}
