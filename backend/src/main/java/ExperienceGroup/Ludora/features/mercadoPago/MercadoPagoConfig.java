package ExperienceGroup.Ludora.features.mercadoPago;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MercadoPagoConfig {

    @Value("${mercadopago.access}")
    private String accessToken;


    @PostConstruct
    public void init(){
        com.mercadopago.MercadoPagoConfig.setAccessToken(accessToken);
    }
}
