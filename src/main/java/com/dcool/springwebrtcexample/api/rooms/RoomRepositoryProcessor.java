package com.dcool.springwebrtcexample.api.rooms;

import com.dcool.springwebrtcexample.api.friends.FriendRepository;
import com.dcool.springwebrtcexample.api.users.UserRepository;
import com.dcool.springwebrtcexample.domain.Channel;
import com.dcool.springwebrtcexample.domain.Room;
import com.dcool.springwebrtcexample.domain.Room.Member;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class RoomRepositoryProcessor  implements RepresentationModelProcessor<EntityModel<Room>> {

    protected Log logger = LogFactory.getLog(getClass());

    private @Autowired UserRepository userRepository;
    private @Autowired FriendRepository friendRepository;

    @Override
    public EntityModel<Room> process(EntityModel<Room> model) {

        List<EntityModel<Member>> roomMembers = new ArrayList<>();

        try{
            Room room = model.getContent();
            String userId = room.getUserId();
            Channel channel = room.getChannel();
            String[] inviteUsers = channel.getInviteUsers();

            for(String inviteUser : inviteUsers){

                Member member = new Member();

                friendRepository.findOneByUserIdAndFriend_UserId(userId, inviteUser).ifPresentOrElse((friend->{
                    //Exists Friend With Group
                    member.setType(userId.equals(inviteUser) ? Member.Type.ME : Member.Type.FRIEND);
                    member.setUserId(friend.getFriend().getUserId());
                    member.setUserName(
                        StringUtils.hasText(friend.getFriendName()) ? friend.getFriendName() : friend.getFriend().getUserName());

                }), ()->{

                    userRepository.findById(inviteUser).ifPresentOrElse(user->{
                        member.setType(userId.equals(user.getUserId()) ? Member.Type.ME : Member.Type.NEIGHBOR );
                        member.setUserId(user.getUserId());
                        member.setUserName(user.getUserName());

                    }, ()->{
                        //NoExists User
                        member.setType(Member.Type.STRANGER);
                    });
                });

                roomMembers.add(EntityModel.of(member));
            }

        }catch(Exception e){
            logger.info("", e);
        }
        model.getContent().setRoomMembers(roomMembers);
        return model;
    }
}
