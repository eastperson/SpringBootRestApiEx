package com.ep.restapi.jwt;

import com.ep.restapi.config.security.CustomUserDetailService;
import com.ep.restapi.config.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class JwtTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    void test(){
        String token = jwtTokenProvider.createToken("eastperson", List.of("USER"));
        System.out.println("token : "+token);
        System.out.println(jwtTokenProvider.getUserPk(token));
        System.out.println(jwtTokenProvider.validateToken(token));
    }

}
