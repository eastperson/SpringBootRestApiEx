package com.ep.restapi.model.social;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
public class KakaoProfile {
    private Long id;
    private Properties properties;

    @Getter @Setter @ToString
    private static class Properties{
        private String nickname;
        private String thumnail_image;
        private String profile_image;
    }
}
