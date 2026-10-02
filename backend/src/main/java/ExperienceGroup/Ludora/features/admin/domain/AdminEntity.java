package ExperienceGroup.Ludora.features.admin.domain;

import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminEntity extends UserEntity {


    @Column(nullable = false, unique = true)
    private Long employeeId;
}
