package com.ep.restapi.controller.v1;

import com.ep.restapi.domain.User;
import com.ep.restapi.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SignControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    @Before("signin")
    void setUp() throws Exception{
        User user = userRepository.save(User.builder()
                .uid("ep@email.com")
                .name("동인")
                .password(passwordEncoder.encode("123123"))
                .roles(List.of("ROLE_USER")).build());
        System.out.println("user : "+user);
    }

    @DisplayName("로그인 테스트")
    @Test
    void signin() throws Exception{
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("uid","ep@email.com");
        params.add("password","123123");
        mockMvc.perform(post("/v1/signin")
                .accept(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                .params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void signup() throws Exception{
        long epochTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("uid","ep_" + epochTime + "email.com");
        params.add("password","123123");
        params.add("name","ep_"+epochTime);
        mockMvc.perform(post("/v1/signup")
                .accept(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                .params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists());
    }

    @Test
    @WithMockUser(username = "mockUser",roles = {"USER"})
    public void accessdenied() throws Exception{
        //mockMvc.perform(MockMvcRequestBuilders.get("/v1/users").accept(MediaType.APPLICATION_JSON + ";charset=UTF-8"))
                //.andDo(print())
//                .andExpect(forwardedUrl("/exception/accessdenied"));
    }

}