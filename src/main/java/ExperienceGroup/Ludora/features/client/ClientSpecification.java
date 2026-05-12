package ExperienceGroup.Ludora.features.client;

import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import org.springframework.data.jpa.domain.PredicateSpecification;

import java.time.LocalDate;

public class ClientSpecification {
    public static PredicateSpecification<ClientEntity> nameContains(String name){
        return (root, cb) -> name == null || name.isBlank()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static PredicateSpecification<ClientEntity> lastNameContains(String lastName){
        return (root, cb) -> lastName == null || lastName.isBlank()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static PredicateSpecification<ClientEntity> userNameEquals(String userName){
        return (root, cb) -> userName == null || userName.isBlank()
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("userName")), userName.toLowerCase());
    }

    public static PredicateSpecification<ClientEntity> statusBlockedEquals(Boolean status){
        return (root, cb) -> status == null
                ? cb.conjunction()
                : cb.equal(root.get("statusBlocked"), status);
    }

    public static PredicateSpecification<ClientEntity> emailEquals(String email){
        return (root, cb) -> email == null || email.isBlank()
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("email").get("value")), email.toLowerCase());
    }

  public static PredicateSpecification<ClientEntity> phoneEquals (Integer phone){
        return (root , cb)->phone == null
                ? cb.conjunction()
                :cb.equal(root.get("phone"), phone);
  }

  public static  PredicateSpecification<ClientEntity> streetEquals (String street){
        return (root, cb) -> street==null || street.isBlank()
                ?cb.conjunction()
                :cb.equal(cb.lower(root.get("street")),"%"+street.toLowerCase()+"%");
  }

  public static PredicateSpecification<ClientEntity> numberStreetEquals (Integer numberSteet){
        return (root, cb)-> numberSteet== null
                ?cb.conjunction()
                :cb.equal(root.get("numberStreet"), numberSteet);
  }

  public static PredicateSpecification<ClientEntity> birthDateEquals (LocalDate birthDate){
        return (root,cb )->  birthDate==null
                ?cb.conjunction()
                :cb.equal(root.get("birhDate"),birthDate);
  }
}
