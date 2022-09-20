package org.zerock.allivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.allivery.config.GoogleOAuth;
import org.zerock.allivery.config.KakaoOAuth;
import org.zerock.allivery.dto.OAuth.GoogleOAuthTokenDto;
import org.zerock.allivery.dto.OAuth.GoogleUserInfoDto;
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
    private final GoogleOAuth googleOAuth;

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
            userRepository.save(
                    User.builder()
                            .email(email)
                            .nickName(name)
                            .password("kakao")
                            .serialNum(name + LocalDateTime.now())
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

    public ResponseEntity<String> googlelogin(String code) throws IOException {
        // 구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
        ResponseEntity<String> accessTokenResponse = googleOAuth.requestAccessToken(code);
        // 응답 객체가 JSON 형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
        GoogleOAuthTokenDto oAuthToken = googleOAuth.getAccessToken(accessTokenResponse);
        // accessToken을 담은 후 accessToken 통신
        ResponseEntity<String> userInfoResponse = googleOAuth.requestUserInfo(oAuthToken);
        GoogleUserInfoDto googleUser = googleOAuth.getUserInfo(userInfoResponse);

        String email = googleUser.getEmail();
        String name = googleUser.getName();

        if (!userRepository.existsByEmail(email)) {
            /*
             * 회원가입일때와 로그인일때를 구분해서 JWT 발급이 가능해야함
             */
            userRepository.save(
                    User.builder()
                            .email(email)
                            .nickName(name)
                            .password("google")
                            .serialNum(name + LocalDateTime.now())
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
        return new ResponseEntity<>("LOGIN_TRUE",HttpStatus.OK);
    }
    /*
    회원가입 이후 추가정보 요청하는 Service 필요
    성공시 회원가입 또는 로그인 시의 Validation 필요
     */
}
