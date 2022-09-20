package org.zerock.allivery.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.allivery.config.GoogleOAuth;
import org.zerock.allivery.service.OAuthService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("oauth")
public class OAuthController {

    private final OAuthService oAuthService;
    private final GoogleOAuth googleOAuth;


    @GetMapping("/kakao")
    public void getKakakoAuthUrl(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://kauth.kakao.com/oauth/authorize?client_id=b6f8914d44b9618e47be1c31c0867db2&redirect_uri=http://localhost:8080/oauth/kakao/login&response_type=code");
    }

    @GetMapping("kakao/login")
    public ResponseEntity<String> kakaoLogin(
            @RequestParam(name = "code") String code) throws IOException {
        log.info("카카오 API 서버 code : " + code);
        return oAuthService.kakaoLogin(code);
    }

    @GetMapping("/google")
    public void getGoogleAuthUrl(HttpServletResponse response) throws IOException {
        response.sendRedirect(googleOAuth.getOauthRedirectURL());
    }

    @GetMapping("/google/login")
    public ResponseEntity<String> callback(
            @RequestParam(name = "code") String code) throws IOException {
        log.info("구글 API 서버 code : " + code);
        return oAuthService.googlelogin(code);
    }
}
