package ExperienceGroup.Ludora.features.ageRange;

import ExperienceGroup.Ludora.features.ageRange.domain.dto.AgeRangeDTORequest;
import ExperienceGroup.Ludora.features.ageRange.domain.dto.AgeRangeDTOResponse;

import java.util.List;
import java.util.UUID;

public interface IAgeRangeService {
    List<AgeRangeDTOResponse> getAllAgeRange(String rangeName, Integer minAge, String description);
    AgeRangeDTOResponse getByExternalId (UUID externalId);
    AgeRangeDTOResponse save (AgeRangeDTORequest ageRangeDTORequest);
    AgeRangeDTOResponse update(UUID externalId, AgeRangeDTORequest ageRangeDTORequest);
    void delete (UUID externalId);
}
