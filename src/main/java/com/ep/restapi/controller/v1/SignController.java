package com.ep.restapi.controller.v1;

import com.ep.restapi.advice.exception.CEmailSigninFailedException;
import com.ep.restapi.config.security.JwtTokenProvider;
import com.ep.restapi.domain.User;
import com.ep.restapi.model.response.CommonResult;
import com.ep.restapi.model.response.SingleResult;
import com.ep.restapi.repository.UserRepository;
import com.ep.restapi.service.ResponseService;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인",notes = "이메일 회원 로그인을 한다.")
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(
            @ApiParam(value = "회원 ID : 이메일",required = true) @RequestParam String uid,
            @ApiParam(value = "비밀번호 : 이메일",required = true) @RequestParam String password
    ) {
        System.out.println("uid : "+uid);
        System.out.println("password : "+password);
        User user = userRepository.findByUid(uid).orElseThrow(CEmailSigninFailedException::new);
        System.out.println(user);
        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new CEmailSigninFailedException();
        }
        System.out.println("통과");

        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()),user.getRoles()));
    }

    @ApiOperation(value = "가입",notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResult signin(
            @ApiParam(value = "회원ID : 이메일",required = true) @RequestParam String uid,
            @ApiParam(value = "비밀번호",required = true) @RequestParam String password,
            @ApiParam(value = "이름",required = true) @RequestParam String name
    ){
        System.out.println("===========================회원가입");
        System.out.println(uid);
        System.out.println(password);
        System.out.println(name);
        User user = userRepository.save(User.builder()
                .uid(uid)
                .password(passwordEncoder.encode(password))
                .name(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        System.out.println("user : " + user);
        return responseService.getSuccessResult();
    }

}
