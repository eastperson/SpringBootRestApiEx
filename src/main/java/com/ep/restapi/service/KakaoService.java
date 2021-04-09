package com.ep.restapi.service;

import com.ep.restapi.advice.exception.CCommunicationException;
import com.ep.restapi.model.social.KakaoProfile;
import com.ep.restapi.model.social.RetKakaoAuth;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;



@RequiredArgsConstructor
@Service
public class KakaoService {

    private final RestTemplate restTemplate;
    private final Environment env;
    private final Gson gson;

    @Value("http://localhost:8080")
    private String baseUrl;

    @Value("ee7c2971235680f1f09fb9247f4c78da")
    private String kakaoClientId;

    @Value("/social/login/kakao")
    private String kakaoRedirect;

    public KakaoProfile getKakaoProfile(String accessToken) {
        // Set Header : Content-type : application/x-www-form-irlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization","Beadrer " + accessToken);

        // Set http entity
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(null,headers);
        try{
            // Request profile
            ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("spring.social.kakao.url.profile"),request,String.class);
            if(response.getStatusCode() == HttpStatus.OK){
                return gson.fromJson(response.getBody(),KakaoProfile.class);
            }
        } catch (Exception e) {
            throw new CCommunicationException();
        }
        throw new CCommunicationException();
    }

    public RetKakaoAuth getKakaoTokenInfo(String code) {
        // Set Header : Content-type : application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Set parameter
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri",baseUrl+kakaoRedirect);
        params.add("code",code);
        // Set http entity
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(params,headers);
        ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("spring.social.kakao.url.token"),request,String.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            return gson.fromJson(response.getBody(),RetKakaoAuth.class);
        }
        return null;
    }



}
