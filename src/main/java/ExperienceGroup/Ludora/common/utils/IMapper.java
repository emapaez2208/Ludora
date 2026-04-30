package ExperienceGroup.Ludora.common.utils;

public interface IMapper <T, U>{
    T toEntity(U u);
    U toDTO(T t);
}
