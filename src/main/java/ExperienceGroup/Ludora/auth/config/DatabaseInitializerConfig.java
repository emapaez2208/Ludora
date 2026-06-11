package ExperienceGroup.Ludora.auth.config;

import ExperienceGroup.Ludora.auth.credentials.CredentialsEntity;
import ExperienceGroup.Ludora.auth.credentials.CredentialsRepository;
import ExperienceGroup.Ludora.auth.permissions.*;
import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.features.admin.IAdminRepository;
import ExperienceGroup.Ludora.features.admin.domain.AdminEntity;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
public class DatabaseInitializerConfig {

    @Bean
    @Transactional
    public CommandLineRunner initDatabase(
            PermitRepository permitRepository,     // Asumiendo que tenés estos repositorios
            RoleRepository roleRepository,
            CredentialsRepository credentialsRepository,
            IAdminRepository adminRepository,
            PasswordEncoder passwordEncoder          // Inyectamos tu PasswordEncoder real
    ) {
        return args -> {
            // 1. Evitar duplicados si el ddl-auto no está en create-drop
            if (permitRepository.count() > 0) return;

            System.out.println(">> Cargando datos de prueba en la base de datos...");

            // 2. Crear y guardar Permisos
            PermitEntity seeUsers = permitRepository.save(PermitEntity.builder().permits(PermitsEnum.SEE_USERS).build());
            PermitEntity createUsers = permitRepository.save(PermitEntity.builder().permits(PermitsEnum.CREATE_USERS).build());
            PermitEntity updateUsers = permitRepository.save(PermitEntity.builder().permits(PermitsEnum.UPDATE_USERS).build());
            PermitEntity deleteUsers = permitRepository.save(PermitEntity.builder().permits(PermitsEnum.DELETE_USERS).build());
            PermitEntity buyGames = permitRepository.save(PermitEntity.builder().permits(PermitsEnum.BUY_GAMES).build());
            PermitEntity gameAgreeCart = permitRepository.save(PermitEntity.builder().permits(PermitsEnum.GAME_AGREE_CART).build());
            PermitEntity createGames = permitRepository.save(PermitEntity.builder().permits(PermitsEnum.CREATE_GAMES).build());
            PermitEntity updateGames = permitRepository.save(PermitEntity.builder().permits(PermitsEnum.UPDATE_GAMES).build());
            PermitEntity deleteGames = permitRepository.save(PermitEntity.builder().permits(PermitsEnum.DELETE_GAMES).build());
            PermitEntity createReview = permitRepository.save(PermitEntity.builder().permits(PermitsEnum.CREATE_REVIEW).build());
            PermitEntity updateReview = permitRepository.save(PermitEntity.builder().permits(PermitsEnum.UPDATE_REVIEW).build());
            PermitEntity deleteReview = permitRepository.save(PermitEntity.builder().permits(PermitsEnum.DELETE_REVIEW).build());
            PermitEntity crudAgeRange = permitRepository.save(PermitEntity.builder().permits(PermitsEnum.CRUD_AGERANGE).build());


            // 3. Crear y guardar Roles asignando los permisos correspondientes
            RoleEntity roleClient = new RoleEntity(RolesEnum.ROLE_CLIENT);
            roleClient.getPermits().add(buyGames);
            roleClient.getPermits().add(gameAgreeCart);
            roleClient.getPermits().add(createReview);
            roleClient.getPermits().add(updateReview);
            roleClient.getPermits().add(deleteReview);
            roleRepository.save(roleClient);

            RoleEntity roleAdmin = new RoleEntity(RolesEnum.ROLE_ADMIN);
            roleAdmin.getPermits().add(seeUsers);
            roleAdmin.getPermits().add(createUsers);
            roleAdmin.getPermits().add(updateUsers);
            roleAdmin.getPermits().add(deleteUsers);
            roleAdmin.getPermits().add(deleteGames);
            roleAdmin.getPermits().add(deleteReview);
            roleAdmin.getPermits().add(crudAgeRange);
            roleRepository.save(roleAdmin);

            RoleEntity roleDeveloper = new RoleEntity(RolesEnum.ROLE_DEVELOPER);
            roleDeveloper.getPermits().add(createGames);
            roleDeveloper.getPermits().add(updateGames);
            roleRepository.save(roleDeveloper);

            String passwordPlano = "password123";
            String passwordEncriptada = passwordEncoder.encode(passwordPlano); // <-- ACÁ SUCEDE LA MAGIA

            // Ejemplo: Crear Admin
            AdminEntity adminUser = new AdminEntity();
            adminUser.setExternalId(UUID.randomUUID());
            adminUser.setEmail(new Email("admin@example.com"));
            adminUser.setName("admin");
            adminUser.setLastName("Admin");
            adminUser.setUserName("admin123");
            adminUser.setEmployeeId(123456L);
            adminUser.setStatusBlocked(false);
            adminRepository.save(adminUser);

            CredentialsEntity adminCreds = new CredentialsEntity();
            adminCreds.setUsername("admin123");
            adminCreds.setExternalId(adminUser.getExternalId());
            adminCreds.setPassword(passwordEncriptada); // Guardamos el hash generado en vivo
            adminCreds.setEnabled(true);
            adminCreds.getRoles().add(roleAdmin);
            adminCreds.setUser(adminUser);
            credentialsRepository.save(adminCreds);

            System.out.println(">> ¡Datos de prueba cargados exitosamente usando el PasswordEncoder");
        };
    }
}