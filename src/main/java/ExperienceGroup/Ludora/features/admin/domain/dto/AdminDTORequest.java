package ExperienceGroup.Ludora.features.admin.domain.dto;

import ExperienceGroup.Ludora.common.utils.Email;
import ExperienceGroup.Ludora.common.utils.Password;
import ExperienceGroup.Ludora.features.user.domain.dto.UserDTORequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record AdminDTORequest(@Schema(description = "El nombre del usuario", example = "John", required = true)
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
                              Password password,
                              @Schema(description = "El legajo del administrador", example = "611305", required = true)
                              @Positive
                              @NotNull
                              Long employeeId) {
}
