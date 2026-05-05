package ExperienceGroup.Ludora.features.role.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RoleDTO  (@Schema (description = "nombre de un Role" , example = "Admin , Develop , Client" , required = true)
                        @Pattern(regexp = "^(Admin|Develop|Client)$", message = "Elige una opción válida")
                        @NotBlank String name ,
                        @Schema(description ="Description del Role", required = true )
                        String description){
}
