package com.ep.restapi.controller.v1;

import com.ep.restapi.domain.User;
import com.ep.restapi.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping(value = "/user")
    public List<User> findAllUser(){
        return userRepository.findAll();
    }

    @PostMapping(value = "/user")
    public User save(){
        User user = User.builder()
                .uid("ep@email.com")
                .name("동인")
                .build();
        return userRepository.save(user);
    }

}
