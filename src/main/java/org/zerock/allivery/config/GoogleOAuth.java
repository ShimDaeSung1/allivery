package org.zerock.allivery.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.zerock.allivery.dto.OAuth.GoogleOAuthTokenDto;
import org.zerock.allivery.dto.OAuth.GoogleUserInfoDto;
import org.zerock.allivery.dto.OAuth.KakaoUserInfoDto;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleOAuth {

    final String googleLoginUrl = "https://accounts.google.com";
    final String googleClientId = "931078693609-qar8a0o35q2ngmuhgugqu260r1mjj5n3.apps.googleusercontent.com";
    final String googleRedirecUrl = "http://localhost:8080/oauth/google/login";
    final String googleClientSecret = "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";
    final String GOOGLE_TOKEN_REQUEST_URL = "https://oauth2.googleapis.com/token";
    final String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v1/userinfo";

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public String getOauthRedirectURL() {
        String reqUrl = googleLoginUrl + "/o/oauth2/v2/auth?client_id=" + googleClientId + "&redirect_uri=" + googleRedirecUrl
                + "&response_type=code&scope=email%20profile%20openid&access_type=offline";
        return reqUrl;
    }

    // 먼저 일회용 코드를 다시 구글로 보내 액세스 토큰을 포함한 JSON String이 담긴 ResponseEntity를 받아온다.
    public ResponseEntity<String> requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", googleClientId);
        params.put("client_secret", googleClientSecret);
        params.put("redirect_uri", googleRedirecUrl);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL,
                params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        return null;
    }

    // responseEntity에 담긴 JSON String을 역직렬화해 자바 객체에 담는다.
    public GoogleOAuthTokenDto getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        System.out.println("response.getBody() = " + response.getBody());
        GoogleOAuthTokenDto googleOAuthTokenDto = objectMapper.readValue(response.getBody(), GoogleOAuthTokenDto.class);
        return googleOAuthTokenDto;
    }

    public ResponseEntity<String> requestUserInfo(GoogleOAuthTokenDto oAuthToken) {

        //header에 accessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());

        //HttpEntity를 하나 생성해 헤더를 담아서 후
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
        System.out.println("response.getBody() = " + response.getBody());
        return response;
    }

    public GoogleUserInfoDto getUserInfo(ResponseEntity<String> response) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();
            GoogleUserInfoDto googleUserInfoDto = objectMapper.readValue(response.getBody(), GoogleUserInfoDto.class);
            return googleUserInfoDto;
    }
}
