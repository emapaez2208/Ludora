package ExperienceGroup.Ludora.features.game.domain.dto;

import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTOResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record GameDTOResponse(
        UUID externalId,
        String name,
        BigDecimal price,
        String description,
        LocalDate releaseDate,
        String ageRange,
        List<String> genres,
        List<ReviewDTOResponse> reviews
) {}
