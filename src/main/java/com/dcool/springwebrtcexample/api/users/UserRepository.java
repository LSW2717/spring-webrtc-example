package com.dcool.springwebrtcexample.api.users;

import com.dcool.springwebrtcexample.domain.User;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository
    extends
    JpaRepository<User, String>,
    JpaSpecificationExecutor<User> {



    Optional<User> findByUserIdAndUserNameIsNotNull(String userId);

}