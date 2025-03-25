package com.dcool.springwebrtcexample.api.rooms;

import com.dcool.common.rest.core.annotation.HandleBeforeRead;
import com.dcool.common.rest.jpa.repository.query.JpaSpecificationBuilder;
import com.dcool.springwebrtcexample.domain.Room;
import com.dcool.springwebrtcexample.domain.response.BadRequestException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class RoomRepositoryHandler{

    protected Log logger = LogFactory.getLog(getClass());

    @HandleBeforeCreate
    @HandleBeforeDelete
    public void handleBefore(Room entity) throws Exception{
        throw new BadRequestException();
    }


    @HandleBeforeRead
    public void handleBeforeRead(Room entity, Specification<Room> specification){

        JpaSpecificationBuilder.of(Room.class)
            .where()
            .and().eq("userId", entity.getUserId())
            .build(specification);
    }
}
