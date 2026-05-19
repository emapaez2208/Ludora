package ExperienceGroup.Ludora.features.ageRange;

import ExperienceGroup.Ludora.common.exception.InvalidAgeRangeException;
import ExperienceGroup.Ludora.common.exception.AgeRangeNotFoundException;
import ExperienceGroup.Ludora.common.utils.IMapper;
import ExperienceGroup.Ludora.features.ageRange.domain.AgeRangeEntity;
import ExperienceGroup.Ludora.features.ageRange.domain.dto.AgeRangeDTORequest;
import ExperienceGroup.Ludora.features.ageRange.domain.dto.AgeRangeDTOResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@AllArgsConstructor
public class AgeRangeService implements IAgeRangeService{

    private final IAgeRangeRepository ageRangeRepository;
    private final IMapper<AgeRangeEntity, AgeRangeDTOResponse> responseMapper;
    private final IMapper<AgeRangeEntity, AgeRangeDTORequest> requestMapper;

    @Override
    public List<AgeRangeDTOResponse> getAllAgeRange() {

        List<AgeRangeEntity> ageRanges = ageRangeRepository.findAll();

        return ageRanges.stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    @Override
    public AgeRangeDTOResponse getByExternalId(UUID externalId) {
        return ageRangeRepository.findByExternalId(externalId)
                .map(responseMapper::toDTO)
                .orElseThrow(() -> new AgeRangeNotFoundException("Age range not found"));
    }

    @Override
    @Transactional
    public AgeRangeDTOResponse save(AgeRangeDTORequest ageRangeDTORequest) {

        if (ageRangeRepository.existsByRangeName(ageRangeDTORequest.rangeName())){
            throw new InvalidAgeRangeException("Age range name '" + ageRangeDTORequest.rangeName() + "' already exists.");
        }

        AgeRangeEntity entity = requestMapper.toEntity(ageRangeDTORequest);
        AgeRangeEntity savedEntity = ageRangeRepository.save(entity);

        return responseMapper.toDTO(savedEntity);
    }

    @Override
    @Transactional
    public AgeRangeDTOResponse update(UUID externalId, AgeRangeDTORequest ageRangeDTORequest) {
        AgeRangeEntity existingAgeRange = ageRangeRepository.findByExternalId(externalId)
                .orElseThrow(() -> new AgeRangeNotFoundException("Age range not found"));

        if (ageRangeRepository.existsByRangeName(ageRangeDTORequest.rangeName()) &&
        !existingAgeRange.getRangeName().equals(ageRangeDTORequest.rangeName())){
            throw new InvalidAgeRangeException("Age range name '" + ageRangeDTORequest.rangeName() + "' already exists.");
        }

        existingAgeRange.setRangeName(ageRangeDTORequest.rangeName());
        existingAgeRange.setMinAge(ageRangeDTORequest.minAge());
        existingAgeRange.setDescription(ageRangeDTORequest.description());

        AgeRangeEntity savedAgeRange = ageRangeRepository.save(existingAgeRange);

        return responseMapper.toDTO(savedAgeRange);
    }

    @Override
    @Transactional
    public void delete(UUID externalId) {
        AgeRangeEntity toBeDeleted = ageRangeRepository.findByExternalId(externalId)
                .orElseThrow(() -> new AgeRangeNotFoundException("Age range not found"));

        ageRangeRepository.delete(toBeDeleted);
    }
}
