package com.dcool.springwebrtcexample.api.channels;

import com.dcool.springwebrtcexample.domain.Channel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChannelRepository
    extends
    JpaRepository<Channel, UUID>,
    JpaSpecificationExecutor<Channel> {

    Channel findOneByInviteUsers(String[] inviteUsers);
}
