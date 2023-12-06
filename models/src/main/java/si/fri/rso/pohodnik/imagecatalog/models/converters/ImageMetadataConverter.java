package si.fri.rso.pohodnik.version.models.converters;

import si.fri.rso.pohodnik.version.lib.ImageMetadata;
import si.fri.rso.pohodnik.version.models.entities.ImageMetadataEntity;

public class ImageMetadataConverter {

    public static ImageMetadata toDto(ImageMetadataEntity entity) {

        ImageMetadata dto = new ImageMetadata();
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

    public static ImageMetadataEntity toEntity(ImageMetadata dto) {

        ImageMetadataEntity entity = new ImageMetadataEntity();
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
