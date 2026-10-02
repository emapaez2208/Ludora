package ExperienceGroup.Ludora.features.mercadoPago.exception;

public class MercadoPagoFailedException extends RuntimeException {
    public MercadoPagoFailedException(String message) {
        super(message);
    }

    public MercadoPagoFailedException(){
        super("Failed in MercadoPago context");
    }
}
