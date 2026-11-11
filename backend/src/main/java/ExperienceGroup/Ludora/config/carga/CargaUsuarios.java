package ExperienceGroup.Ludora.config.carga;

import ExperienceGroup.Ludora.auth.credentials.CredentialsEntity;
import ExperienceGroup.Ludora.auth.credentials.CredentialsRepository;
import ExperienceGroup.Ludora.auth.permissions.RoleEntity;
import ExperienceGroup.Ludora.auth.permissions.RoleRepository;
import ExperienceGroup.Ludora.auth.permissions.RolesEnum;
import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.features.client.IClientRepository;
import ExperienceGroup.Ludora.features.client.domain.ClientEntity;
import ExperienceGroup.Ludora.features.developer.IDeveloperRepository;
import ExperienceGroup.Ludora.features.developer.domain.DeveloperEntity;
import ExperienceGroup.Ludora.features.user.domain.UserEntity;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Order(2)
public class CargaUsuarios implements CargaDatos {

    private final IDeveloperRepository developerRepository;
    private final IClientRepository clientRepository;
    private final CredentialsRepository credentialsRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public CargaUsuarios(IDeveloperRepository developerRepository,
                         IClientRepository clientRepository,
                         CredentialsRepository credentialsRepository,
                         RoleRepository roleRepository,
                         PasswordEncoder passwordEncoder) {
        this.developerRepository = developerRepository;
        this.clientRepository = clientRepository;
        this.credentialsRepository = credentialsRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private record DatosDeveloper(String userName, String nombre, String apellido,
                                  String email, String empresa, String password) {}

    private record DatosCliente(String userName, String nombre, String apellido, String email,
                                long telefono, String calle, int numero, String nacimiento,
                                int puntos, boolean bloqueado, String password) {}

    // DEVELOPERS  (para agregar uno: una línea nueva)
    private static final List<DatosDeveloper> DEVELOPERS = List.of(
            new DatosDeveloper("manuelDev", "Manuel", "Gonzales", "manu@santamonica.com", "Santa Monica Studios", "Manu123"),
            new DatosDeveloper("francoDev", "Franco", "Rossi", "franco@rockstargames.com", "Rockstar Games", "Franco123"),
            new DatosDeveloper("shigeruDev", "Shigeru", "Miyamoto", "miyamoto@nintendo.com", "Nintendo", "Shigeru123"),
            new DatosDeveloper("cdpDev", "Marcin", "Iwinski", "contact@cdprojektred.com", "CD Projekt Red", "Cdp123"),
            new DatosDeveloper("hidetakaDev", "Hidetaka", "Miyazaki", "miyazaki@fromsoftware.com", "FromSoftware", "Miyazaki123"),
            new DatosDeveloper("capcomDev", "Kenzo", "Tsujimoto", "kenzo@capcom.com", "Capcom", "Capcom123")
    );

    // CLIENTES  (para agregar uno: una línea nueva)
    private static final List<DatosCliente> CLIENTES = List.of(
            new DatosCliente("cosmejuega", "Cosme", "Fulanito", "cosme@gmail.com", 223123456L, "Av. Colón", 1234, "1995-04-12", 12000, false, "Cosme123"),
            new DatosCliente("estebanquito", "Esteban", "Quito", "esteban@gmail.com", 223654321L, "San Martín", 2580, "1999-01-15", 99000, false, "Esteban123"),
            new DatosCliente("alanbrito", "Alan", "Brito", "alan@gmail.com", 223987654L, "Av. Independencia", 4321, "2002-06-20", 5, false, "Alan123"),
            new DatosCliente("luciafernandez", "Lucía", "Fernández", "lucia.fernandez@gmail.com", 2235123456L, "Colón", 1450, "1995-03-22", 1500, false, "Lucia123"),
            new DatosCliente("martingomez", "Martín", "Gómez", "martin.gomez@gmail.com", 2236234567L, "Independencia", 3120, "1990-07-08", 4200, false, "Martin123"),
            new DatosCliente("camilarodriguez", "Camila", "Rodríguez", "camila.rodriguez@gmail.com", 2234345678L, "Güemes", 980, "2000-11-30", 250, false, "Camila123"),
            new DatosCliente("tomasperez", "Tomás", "Pérez", "tomas.perez@gmail.com", 2235456789L, "Rivadavia", 2210, "1988-05-17", 0, true, "Tomas123"),
            new DatosCliente("valentinasosa", "Valentina", "Sosa", "valentina.sosa@gmail.com", 2236567890L, "Alberti", 1735, "1997-09-04", 8700, false, "Valentina123"),
            new DatosCliente("nicolasacosta", "Nicolás", "Acosta", "nicolas.acosta@gmail.com", 2234678901L, "Luro", 4050, "1993-02-12", 12300, false, "Nicolas123"),
            new DatosCliente("sofiabenitez", "Sofía", "Benítez", "sofia.benitez@gmail.com", 2235789012L, "Av. Constitución", 5600, "2001-06-25", 560, false, "Sofia123"),
            new DatosCliente("joaquinmolina", "Joaquín", "Molina", "joaquin.molina@gmail.com", 2236890123L, "Belgrano", 3345, "1985-12-01", 31000, false, "Joaquin123"),
            new DatosCliente("agustinaramirez", "Agustina", "Ramírez", "agustina.ramirez@gmail.com", 2234901234L, "Falucho", 1120, "1998-08-19", 77500, false, "Agustina123")
    );

    @Override
    public void cargar() {
        RoleEntity rolDeveloper = roleRepository.findByRole(RolesEnum.ROLE_DEVELOPER)
                .orElseThrow(() -> new RuntimeException("Error: No se encontró el Rol DEVELOPER en las tablas de permisos."));
        RoleEntity rolCliente = roleRepository.findByRole(RolesEnum.ROLE_CLIENT)
                .orElseThrow(() -> new RuntimeException("Error: No se encontró el Rol CLIENT en las tablas de permisos."));

        Set<String> developersExistentes = developerRepository.findAll().stream()
                .map(DeveloperEntity::getUserName)
                .collect(Collectors.toSet());

        int nuevosDevelopers = 0;
        for (DatosDeveloper d : DEVELOPERS) {
            if (developersExistentes.contains(d.userName())) continue;
            crearDeveloper(d, rolDeveloper);
            nuevosDevelopers++;
        }

        Set<String> clientesExistentes = clientRepository.findAll().stream()
                .map(ClientEntity::getUserName)
                .collect(Collectors.toSet());

        int nuevosClientes = 0;
        for (DatosCliente c : CLIENTES) {
            if (clientesExistentes.contains(c.userName())) continue;
            crearCliente(c, rolCliente);
            nuevosClientes++;
        }

        System.out.println("    Developers nuevos: " + nuevosDevelopers + " | Clientes nuevos: " + nuevosClientes);
        mostrarCredenciales();
    }

    private void crearDeveloper(DatosDeveloper d, RoleEntity rol) {
        DeveloperEntity dev = new DeveloperEntity();
        dev.setExternalId(UUID.randomUUID());
        dev.setName(d.nombre());
        dev.setLastName(d.apellido());
        dev.setUserName(d.userName());
        dev.setEmail(new Email(d.email()));
        dev.setStatusBlocked(false);
        dev.setCompany(d.empresa());
        dev = developerRepository.save(dev);

        guardarCredenciales(dev, d.password(), rol);
    }

    private void crearCliente(DatosCliente c, RoleEntity rol) {
        ClientEntity cliente = new ClientEntity();
        cliente.setExternalId(UUID.randomUUID());
        cliente.setName(c.nombre());
        cliente.setLastName(c.apellido());
        cliente.setUserName(c.userName());
        cliente.setEmail(new Email(c.email()));
        cliente.setStatusBlocked(c.bloqueado());
        cliente.setPhone(c.telefono());
        cliente.setStreet(c.calle());
        cliente.setNumberStreet(c.numero());
        cliente.setBirthDate(LocalDate.parse(c.nacimiento()));
        cliente.setPoints(c.puntos());
        cliente = clientRepository.save(cliente);

        guardarCredenciales(cliente, c.password(), rol);
    }

    private void guardarCredenciales(UserEntity usuario, String password, RoleEntity rol) {
        CredentialsEntity credenciales = CredentialsEntity.builder()
                .externalId(usuario.getExternalId())
                .username(usuario.getUserName())
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .accountNonLocked(true)
                .user(usuario)
                .roles(Set.of(rol))
                .build();
        credentialsRepository.save(credenciales);
    }

    private void mostrarCredenciales() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("CREDENCIALES PARA PRUEBAS");
        System.out.println("=".repeat(70));
        System.out.println("   [ADMIN]");
        System.out.println("   Username: admin123         | Password: password123");
        System.out.println("   " + "-".repeat(60));
        System.out.println("   [DEVELOPERS]");
        DEVELOPERS.forEach(d -> System.out.printf("   Username: %-16s | Password: %-13s (%s)%n",
                d.userName(), d.password(), d.empresa()));
        System.out.println("   " + "-".repeat(60));
        System.out.println("   [CLIENTES]");
        CLIENTES.forEach(c -> System.out.printf("   Username: %-16s | Password: %-13s (%d pts)%s%n",
                c.userName(), c.password(), c.puntos(), c.bloqueado() ? " [BLOQUEADO]" : ""));
        System.out.println("=".repeat(70) + "\n");
    }
}