package rocks.zipcode.service.mapper;

import org.mapstruct.*;
import rocks.zipcode.domain.Presets;
import rocks.zipcode.domain.User;
import rocks.zipcode.service.dto.PresetsDTO;
import rocks.zipcode.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Presets} and its DTO {@link PresetsDTO}.
 */
@Mapper(componentModel = "spring")
public interface PresetsMapper extends EntityMapper<PresetsDTO, Presets> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    PresetsDTO toDto(Presets s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
