package com.dcool.springwebrtcexample.api.friends;

import com.dcool.common.rest.core.annotation.HandleBeforeRead;
import com.dcool.common.rest.jpa.repository.query.JpaSpecificationBuilder;
import com.dcool.springwebrtcexample.domain.Friend;
import com.dcool.springwebrtcexample.domain.auditing.AuditedAuditor;
import com.dcool.springwebrtcexample.domain.response.BadRequestException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class FriendRepositoryHandler {

    protected Log logger = LogFactory.getLog(getClass());

    private @Autowired AuditedAuditor auditedAuditor;


    @HandleBeforeCreate
    @HandleBeforeSave
    @HandleBeforeDelete
    public void handleBefore(Friend entity){
        throw new BadRequestException();
    }

    @HandleBeforeRead
    public void handleBeforeRead(Friend entity, Specification<Friend> specification) throws Exception{

        JpaSpecificationBuilder.of(Friend.class)
            .where()
            .and().eq("userId", entity.getUserId())
            .orderBy().asc("friendName")
            .build(specification);
    }
}
