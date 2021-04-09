package com.ep.restapi.controller.common;

import com.ep.restapi.service.KakaoService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Controller
@RequestMapping("/social/login")
public class SocialController {

    private final Environment env;
    private final RestTemplate restTemplate;
    private final Gson gson;
    private final KakaoService kakaoService;

    @Value("http://localhost:8080")
    private String baseUrl;

    @Value("ee7c2971235680f1f09fb9247f4c78da")
    private String kakaoClientId;

    @Value("/social/login/kakao")
    private String kakaoRedirect;

    @GetMapping
    public ModelAndView socialLogin(ModelAndView mav){

        StringBuilder loginUrl = new StringBuilder()
                .append(env.getProperty("spring.social.kakao.url.login"))
                .append("?client_id=").append(kakaoClientId)
                .append("&response_type=code")
                .append("&redirect_uri=").append(baseUrl).append(kakaoRedirect);
        mav.addObject("loginUrl",loginUrl);
        mav.setViewName("social/login");
        return mav;
    }

    @GetMapping(value = "/kakao")
    public ModelAndView redirectKakao(ModelAndView mav, @RequestParam String code){
        mav.addObject("authInfo",kakaoService.getKakaoTokenInfo(code));
        mav.setViewName("social/redirectKakao");
        return mav;
    }

}
