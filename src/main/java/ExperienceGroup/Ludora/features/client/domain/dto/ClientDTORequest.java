package ExperienceGroup.Ludora.features.client.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ClientDTORequest (@Schema(description = "Numero de telefono",example = "123" , required = true)
                                @Size(min = 0 , max =15 , message = "El numero tiene una longuitud de 15")
                                @Positive (message = "No puede ser un numero negativo")
                                @NotNull int phone ,
                                @Schema(description = "Nombre de la calle", example = "Luro" , required = true)
                                @Size(max = 25 , message = "Tiene que tener como maximo 25 caracteres")
                                @NotNull String street,
                                @Schema (description = "numero de la calle",example = "123",required = true)
                                @Positive(message = "No puede ser numero negativo")
                                @NotBlank int numberStreet,
                                @Schema(description = "Fecha de nacimiento", example = "10/10/10", required = true)
                                @NotNull LocalDate birhdate
                                ){

}
