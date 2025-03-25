package com.dcool.springwebrtcexample.api.friends;

import com.dcool.springwebrtcexample.domain.Friend;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FriendRepository
    extends
    JpaRepository<Friend, UUID>,
    JpaSpecificationExecutor<Friend> {

    Optional<Friend> findOneByUserIdAndFriend_UserId(String userId, String friendUserId); // 관계 맺기...

}
