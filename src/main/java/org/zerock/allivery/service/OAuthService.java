package org.zerock.allivery.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.allivery.config.GoogleOAuth;
import org.zerock.allivery.config.KakaoOAuth;
import org.zerock.allivery.config.security.JwtTokenProvider;
import org.zerock.allivery.dto.OAuth.GoogleOAuthTokenDto;
import org.zerock.allivery.dto.OAuth.GoogleUserInfoDto;
import org.zerock.allivery.dto.OAuth.KakaoOAuthTokenDto;
import org.zerock.allivery.dto.OAuth.KakaoUserInfoDto;
import org.zerock.allivery.dto.user.TokenUserDTO;
import org.zerock.allivery.entity.user.User;
import org.zerock.allivery.entity.user.UserRepository;
import org.zerock.allivery.exception.CEmailSigninFailedException;
import org.zerock.allivery.model.SingleResult;
import org.zerock.allivery.service.response.ResponseService;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {
    private final UserRepository userRepository;
    private final KakaoOAuth kakaoOAuth;
    private final GoogleOAuth googleOAuth;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;

    private SingleResult<TokenUserDTO> getSingleResult(User user) {
        return responseService.getSingleResult(
                TokenUserDTO.builder()
                        .token(jwtTokenProvider.createToken(String.valueOf(user.getEmail()), user.getRoles()))
                        .userId(user.getId())
                        .nickName(user.getNickName())
                        .build()
        );
    }

    private KakaoUserInfoDto getKakaoUserInfoDto(String code) throws JsonProcessingException {
        ResponseEntity<String> accessTokenResponse = kakaoOAuth.requestAccessToken(code);
        KakaoOAuthTokenDto oAuthToken = kakaoOAuth.getAccessToken(accessTokenResponse);
        ResponseEntity<String> userInfoResponse = kakaoOAuth.requestUserInfo(oAuthToken);
        KakaoUserInfoDto kakaoUser = kakaoOAuth.getUserInfo(userInfoResponse);
        return kakaoUser;
    }
    private GoogleUserInfoDto getGoogleUserInfoDto(String code) throws JsonProcessingException {
        ResponseEntity<String> accessTokenResponse = googleOAuth.requestAccessToken(code);
        GoogleOAuthTokenDto oAuthToken = googleOAuth.getAccessToken(accessTokenResponse);
        ResponseEntity<String> userInfoResponse = googleOAuth.requestUserInfo(oAuthToken);
        GoogleUserInfoDto googleUser = googleOAuth.getUserInfo(userInfoResponse);
        return googleUser;
    }

    // Service
    // 카카오 로그인 서비스
    public SingleResult<TokenUserDTO> kakaoLogin(String code) throws IOException {
        KakaoUserInfoDto kakaoUser = getKakaoUserInfoDto(code);
        if (!userRepository.existsByEmail(kakaoUser.getKakao_account().getEmail())) {
            userRepository.save(
                    User.builder()
                            .email(kakaoUser.getKakao_account().getEmail())
                            .nickName(kakaoUser.getProperties().getNickname())
                            .password("kakao")
                            .serialNum(kakaoUser.getProperties().getNickname() + LocalDateTime.now())
                            .build()
            );
            return getSingleResult(userRepository.findByEmail(
                    kakaoUser.getKakao_account().getEmail()
            ).orElseThrow(CEmailSigninFailedException::new));
        }
        return getSingleResult(userRepository.findByEmail(
                kakaoUser.getKakao_account().getEmail()
        ).orElseThrow(CEmailSigninFailedException::new));
    }

    public SingleResult<TokenUserDTO> googlelogin(String code) throws IOException {
        GoogleUserInfoDto googleUser = getGoogleUserInfoDto(code);
        if (!userRepository.existsByEmail(googleUser.getEmail())) {
            userRepository.save(
                    User.builder()
                            .email(googleUser.getEmail())
                            .nickName(googleUser.getName())
                            .password("google")
                            .serialNum(googleUser.getName() + LocalDateTime.now())
                            .build()
            );
            return getSingleResult(userRepository.findByEmail(
                    googleUser.getEmail()
            ).orElseThrow(CEmailSigninFailedException::new));
        }
        return getSingleResult(userRepository.findByEmail(
                googleUser.getEmail()
        ).orElseThrow(CEmailSigninFailedException::new));
    }
}
