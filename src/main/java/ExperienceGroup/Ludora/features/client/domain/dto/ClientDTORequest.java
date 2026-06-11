package ExperienceGroup.Ludora.features.client.domain.dto;


import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.common.utils.Password;
import com.fasterxml.jackson.dataformat.yaml.util.StringQuotingChecker;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ClientDTORequest (@Schema(description = "El nombre del usuario", example = "John", required = true)
                                @Size(min = 3, max = 32, message = "El nombre tiene una longitud minima de 3 y maxima de 32 caracteres")
                                @NotEmpty
                                String name,

                                @Schema(description = "El apellido del usuario", example = "Doe", required = true)
                                @Size(min = 3, max = 32, message = "El nombre tiene una longitud minima de 3 y maxima de 32 caracteres")
                                @NotEmpty
                                String lastName,

                                @Schema(description = "El usuario para poder registrarse o iniciar sesion", example = "JohnDoe", required = true)
                                @Size(min = 5, max = 20, message = "El usuario tiene una longitud minima de 5 y maxima de 20 caracteres")
                                @NotBlank
                                String userName,

                                @Schema(description = "El email debe tener un formato email valido", example = "JohnDoe@email.com", required = true)
                                @NotNull
                                Email email,

                                @Schema(description = "La contraseña para poder ingresar a la cuenta, debe tener un formato valido, " +
                                        "usar minimo una minuscula, una mayuscula, un numero y un caracter especial." +
                                        "La longitud debe ser entre 8 y 16 caracteres")
                                @NotNull
                                Password password ,

                                @Schema(description = "Numero de telefono",example = "123" , required = true)
                                @Positive (message = "No puede ser un numero negativo")
                                @NotNull
                                @Min(value = 1000000L, message = "El teléfono debe tener al menos 7 dígitos")
                                @Max(value = 99999999999L, message = "El teléfono no puede superar los 11 dígitos")
                                Long phone ,

                                @Schema(description = "Nombre de la calle", example = "Luro" , required = true)
                                @Size(max = 25 , message = "Tiene que tener como maximo 25 caracteres")
                                @NotNull
                                String street,

                                @Schema (description = "numero de la calle",example = "123",required = true)
                                @Positive(message = "No puede ser numero negativo")
                                @NotNull
                                @Digits(integer = 5, fraction = 0, message = "El maximo de caracteres permitidos es 5")
                                Integer numberStreet,

                                @Schema(description = "Fecha de nacimiento, formato = YYYY-MM-DD", example = "2010-05-27", required = true)
                                @NotNull LocalDate birthDate

                                ){

}
