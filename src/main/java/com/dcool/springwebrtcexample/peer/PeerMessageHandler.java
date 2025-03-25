package com.dcool.springwebrtcexample.peer;


import com.dcool.common.stomp.support.StompHeaderAccessorSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@RestController
public class PeerMessageHandler {

    protected Log logger = LogFactory.getLog(getClass());

    protected @Autowired SimpMessageSendingOperations sender;
    protected String registerKey = getClass().getName();


    @MessageMapping("peer.offer.{roomId}.{userId}")
    @SendTo("/topic/peer.offer.{roomId}.{userId}")
    public String handleOffer(@Payload String offer,
                                  @DestinationVariable(value = "roomId") String roomId,
                                  @DestinationVariable(value = "userId") String userId) {
        logger.info("OFFER [peer.offer."+roomId+"."+userId+"]");
        return offer;
    }

    @MessageMapping("peer.iceCandidate.{roomId}.{userId}")
    @SendTo("/topic/peer.iceCandidate.{roomId}.{userId}")
    public String handleIceCandidate(@Payload String candidate,
                                         @DestinationVariable(value = "roomId") String roomId,
                                         @DestinationVariable(value = "userId") String userId) {
        logger.info("ICE [peer.iceCandidate."+roomId+"."+userId+"]");
        return candidate;
    }

    @MessageMapping("peer.answer.{roomId}.{userId}")
    @SendTo("/topic/peer.answer.{roomId}.{userId}")
    public String handleAnswer(@Payload String answer,
                                   @DestinationVariable(value = "roomId") String roomId,
                                   @DestinationVariable(value = "userId") String userId) {
        logger.info("ANSWER [peer.iceCandidate."+roomId+"."+userId+"]");
        return answer;
    }


    @MessageMapping("peer.keyResponse.{roomId}")
    @SendTo("/topic/peer.keyResponse.{roomId}")
    public PeerMessage handleKeyResponse(StompHeaderAccessor headers, @Payload PeerMessage payload, @DestinationVariable(value = "roomId") String roomId) {
        logger.info("KEY RESPONSE [peer.keyResponse."+roomId+"#"+payload.getKey()+"]");
        payload.setRoomId(roomId);
        payload.setKey(StompHeaderAccessorSupport.user(headers));
        return payload;
    }

    @MessageMapping("peer.keyRequest.{roomId}")
    @SendTo("/topic/peer.keyRequest.{roomId}")
    public PeerMessage handleKeyRequest(StompHeaderAccessor headers, @Payload PeerMessage payload, @DestinationVariable(value = "roomId") String roomId) {
        logger.info("KEY REQUEST [peer.keyRequest."+roomId+"#"+payload.getKey()+"]");
        payload.setRoomId(roomId);
        payload.setKey(StompHeaderAccessorSupport.user(headers));
        handleKeyRegister(headers, payload);
        return payload;
    }

    private void handleKeyDestroy(StompHeaderAccessor headers) {

        PeerMessage payload = (PeerMessage)headers.getSessionAttributes().get(registerKey);

        payload.setKey(StompHeaderAccessorSupport.user(headers));
        logger.info("KEY DESTROY [peer.keyDestroy."+payload.getRoomId()+"#"+payload.getKey()+"]");
        sender.convertAndSend("/topic/peer.keyDestroy."+payload.getRoomId(), payload);
    }


    private void handleKeyRegister(StompHeaderAccessor headers, PeerMessage payload) {

        if(ObjectUtils.isEmpty(payload.getRoomId()))
            payload.setRoomId(StompHeaderAccessorSupport.variable(headers, "{roomId}"));
        payload.setKey(StompHeaderAccessorSupport.user(headers));

        headers.getSessionAttributes().put(registerKey, payload);
    }



    @EventListener
    public void SessionSubscribeEvent(SessionSubscribeEvent e){
        StompHeaderAccessor headers = StompHeaderAccessorSupport.wrap(e, "/topic/peer.keyDestroy.{roomId}");
        if(ObjectUtils.isEmpty(headers)) return;

        this.handleKeyRegister(headers, new PeerMessage());
    }



    @EventListener
    public void SessionDisconnectEvent(SessionDisconnectEvent e){
        StompHeaderAccessor headers = StompHeaderAccessorSupport.wrap(e, registerKey);
        if(ObjectUtils.isEmpty(headers)) return;

        this.handleKeyDestroy(headers);
    }

    @EventListener
    public void SessionUnsubscribeEvent(SessionUnsubscribeEvent e){
        StompHeaderAccessor headers = StompHeaderAccessorSupport.wrap(e, registerKey);
        if(ObjectUtils.isEmpty(headers)) return;

        this.handleKeyDestroy(headers);
    }
}

