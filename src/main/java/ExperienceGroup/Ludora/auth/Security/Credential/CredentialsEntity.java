package ExperienceGroup.Ludora.auth.Security.Credential;

import ExperienceGroup.Ludora.auth.Security.Entity.Role.RoleEntity;
import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter@Getter@NoArgsConstructor@AllArgsConstructor@Builder
public class CredentialsEntity implements UserDetails {

    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false,columnDefinition = "boolean default true")
    private Boolean enabled;

    @OneToOne
    @JoinColumn(name="usuario_id", referencedColumnName = "id", unique = true)
    private UserEntity userEntity;

    @ManyToMany(cascade = CascadeType.MERGE , fetch = FetchType.EAGER)
    @JoinTable
            (name="credentials_roles",
            joinColumns = @JoinColumn(name="credential_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
            )
    private Set<RoleEntity> roles= new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      Set<GrantedAuthority> authorities = new HashSet<>();

      roles.forEach(rol->
              authorities.add(new SimpleGrantedAuthority(rol.getRole().name())));
      return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    public boolean isEnabled (){
        return Boolean.TRUE.equals(this.enabled);
    }
}
