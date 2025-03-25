package com.dcool.springwebrtcexample.api.rooms;

import com.dcool.springwebrtcexample.domain.Room;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoomRepository
    extends
    JpaRepository<Room, String>,
    JpaSpecificationExecutor<Room> {

    List<Room> findByChannel_ChannelId(UUID channelID);

}
