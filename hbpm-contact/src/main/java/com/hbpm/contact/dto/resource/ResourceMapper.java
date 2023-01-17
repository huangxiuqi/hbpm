package com.hbpm.contact.dto.resource;

import com.hbpm.model.contact.ContactResourceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author huangxiuqi
 */
@Mapper
public interface ResourceMapper {

    ResourceMapper INSTANCE = Mappers.getMapper(ResourceMapper.class);

    /**
     * 实体转dto
     * @param entity
     * @return
     */
    ResourceDTO entity2dto(ContactResourceEntity entity);

    /**
     * dto转实体
     * @param dto
     * @return
     */
    ContactResourceEntity dto2entity(ResourceDTO dto);
}
