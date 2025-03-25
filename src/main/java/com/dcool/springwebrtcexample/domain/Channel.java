package com.dcool.springwebrtcexample.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "channels")
@Data
public class Channel {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID channelId;

    private String channelOwner;

    private String[] inviteUsers;

    private String channelName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String[] joinUsers;

    public String toString(){
        return "{channelId="+channelId+", inviteUsers="+ StringUtils.arrayToCommaDelimitedString(inviteUsers) +"}";
    }

}
