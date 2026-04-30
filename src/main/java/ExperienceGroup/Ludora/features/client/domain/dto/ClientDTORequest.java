package ExperienceGroup.Ludora.features.client.domain.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ClientDTORequest (@Positive int phone ,
                                @NotBlank


                                ){

}
