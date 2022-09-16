package org.zerock.allivery.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.zerock.allivery.config.JwtTokenProvider;
import org.zerock.allivery.dto.user.LoginDTO;
import org.zerock.allivery.dto.user.SignupUserDTO;
import org.zerock.allivery.dto.user.TokenUserDTO;
import org.zerock.allivery.entity.user.User;
import org.zerock.allivery.entity.user.UserRepository;
import org.zerock.allivery.model.SingleResult;
import org.zerock.allivery.service.response.ResponseService;
import org.zerock.allivery.service.user.UserService;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider; //jwt토큰 생성
    private final UserService userService; //API요청 결과에 대한 code, message
    private final PasswordEncoder passwordEncoder; //비밀번호 암호화
    private final ResponseService responseService; //API요청 결과에 대한 code, message

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인")
    @PostMapping("/")
    public SingleResult<TokenUserDTO> singin(@RequestBody LoginDTO loginDTO){
        return userService.login(loginDTO);
    }

    @ApiOperation(value = "가입", notes = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<SignupUserDTO> signup(@RequestBody SignupUserDTO signupUserDTO){

        return ResponseEntity.ok(userService.signup(signupUserDTO));
    }

}
