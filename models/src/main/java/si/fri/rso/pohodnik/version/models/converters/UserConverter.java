package si.fri.rso.pohodnik.version.models.converters;

import si.fri.rso.pohodnik.version.lib.User;
import si.fri.rso.pohodnik.version.models.entities.UserEntity;

public class UserConverter {

    public static User toDto(UserEntity entity) {

        User dto = new User();
        dto.setId(entity.getId());
        dto.setfirstName(entity.getfirstName());
        dto.setlastName(entity.getlastName());
        dto.setAge(entity.getAge());
        dto.setsex(entity.getsex());
        dto.setskill(entity.getskill());
        dto.setLongtitude(entity.getLongtitude());
        dto.setLatitude(entity.getLatitude());

        return dto;

    }

    public static UserEntity toEntity(User dto) {

        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setfirstName(dto.getfirstName());
        entity.setlastName(dto.getlastName());
        entity.setAge(dto.getAge());
        entity.setsex(dto.getsex());
        entity.setskill(dto.getskill());
        entity.setLongtitude(dto.getLongtitude());
        entity.setLatitude(dto.getLatitude());

        return entity;

    }

}
