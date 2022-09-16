package org.zerock.allivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.allivery.config.KakaoOAuth;
import org.zerock.allivery.dto.OAuth.KakaoOAuthTokenDto;
import org.zerock.allivery.dto.OAuth.KakaoUserInfoDto;
import org.zerock.allivery.entity.user.User;
import org.zerock.allivery.entity.user.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {
    private final UserRepository userRepository;
    private final KakaoOAuth kakaoOAuth;

    // Service
    // 카카오 로그인 서비스
    public ResponseEntity<String> kakaoLogin(String code) throws IOException {
        ResponseEntity<String> accessTokenResponse = kakaoOAuth.requestAccessToken(code);
        KakaoOAuthTokenDto oAuthToken = kakaoOAuth.getAccessToken(accessTokenResponse);
        ResponseEntity<String> userInfoResponse = kakaoOAuth.requestUserInfo(oAuthToken);
        KakaoUserInfoDto kakaoUser = kakaoOAuth.getUserInfo(userInfoResponse);
        String email = kakaoUser.getKakao_account().getEmail();
        String name = kakaoUser.getProperties().getNickname();
        if (!userRepository.existsByEmail(email)) {
            /*
             * 회원가입일때와 로그인일때를 구분해서 JWT 발급이 가능해야함
             */

            LocalDateTime dateTime = LocalDateTime.now();

            userRepository.save(
                    User.builder()
                            .email(email)
                            .nickName(name)
                            .password("kakao")
                            .serialNum(name + dateTime)
                            .build()
            );
            return new ResponseEntity<>("REGISTER_TRUE", HttpStatus.OK);
            /*
             * body에 클라이언트로 보낼 JSON, 현재 임의로 String
             */
        }
        /*
         * body에 클라이언트로 보낼 JSON, 현재 임의로 String
         */
        return new ResponseEntity<>("LOGIN_TRUE", HttpStatus.OK);
    }
}
