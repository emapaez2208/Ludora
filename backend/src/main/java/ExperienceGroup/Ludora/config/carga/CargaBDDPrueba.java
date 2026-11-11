package ExperienceGroup.Ludora.config;

import ExperienceGroup.Ludora.config.carga.CargaDatos;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Configuration
public class CargaBDDPrueba {

    // INTERRUPTOR (application.yaml -> app.carga.activa)
    // true  -> cada carga escanea la BD e inserta SOLO lo que falta
    // false -> carga desactivada. Si se borra algo, queda borrado hasta una nueva carga en TRUE.
    // 1. Con la base vacía, se puede arrancar con el interruptor en false (levanta las tablas)
    // 2. Se vuelve a lanzar el programa con el interruptor en true
    @Bean
    @Order(2)
    CommandLineRunner initCargaPrueba(List<CargaDatos> cargas,
                                      TransactionTemplate tx,
                                      @Value("${app.carga.activa:true}") boolean activa) {
        return args -> {
            if (!activa) {
                System.out.println("====== CARGA DESACTIVADA (app.carga.activa=false) ======");
                return;
            }

            System.out.println("====== INICIANDO CARGA SELECTIVA DE DATOS DE PRUEBA ======");

            for (CargaDatos carga : cargas) {
                System.out.println("--> " + carga.getClass().getSimpleName());
                tx.executeWithoutResult(status -> carga.cargar());
            }

            System.out.println("====== CARGA DE DATOS DE PRUEBA FINALIZADA ======");
        };
    }
}