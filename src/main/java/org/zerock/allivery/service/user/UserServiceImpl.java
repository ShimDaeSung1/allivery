package org.zerock.allivery.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.allivery.config.JwtTokenProvider;
import org.zerock.allivery.dto.user.LoginDTO;
import org.zerock.allivery.dto.user.SignupUserDTO;
import org.zerock.allivery.dto.user.TokenUserDTO;
import org.zerock.allivery.entity.user.User;
import org.zerock.allivery.entity.user.UserRepository;
import org.zerock.allivery.exception.CEmailSigninFailedException;
import org.zerock.allivery.exception.CEmailSignupFailedException;
import org.zerock.allivery.exception.CPasswordSigninFailedException;
import org.zerock.allivery.exception.CUserNameSignupFailedException;
import org.zerock.allivery.model.SingleResult;
import org.zerock.allivery.service.response.ResponseService;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder; //비밀번호 암호화
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @Override
    public SignupUserDTO signup(SignupUserDTO signupUserDTO) {

        log.info(signupUserDTO.toString());
        String email = signupUserDTO.getEmail();
        String userName = signupUserDTO.getUserName();

        User user1 = userRepository.findByEmail(email).orElse(null);
        User user2 = userRepository.findByUserName(userName);

        if(user1 != null){
            log.error(user1.toString());
            throw new CEmailSignupFailedException();
        }
        if(user2 != null){
            throw new CUserNameSignupFailedException();
        }

        User user = User.builder()
                .userName(signupUserDTO.getUserName())
                .email(signupUserDTO.getEmail())
                .password(encoder.encode(signupUserDTO.getPassword()))
                .build();

        userRepository.save(user);

        return signupUserDTO;
    }

    public SingleResult<TokenUserDTO> login(LoginDTO loginDTO){
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(CEmailSigninFailedException::new);
        if(!encoder.matches(password, user.getPassword())){
            //matches : 평문, 암호문 패스워드 비교 후 t/f리턴
            throw new CPasswordSigninFailedException();
        }

        return responseService.getSingleResult(
                TokenUserDTO.builder()
                        .token(jwtTokenProvider.createToken(String.valueOf(user.getEmail()), user.getRoles()))
                        .userId(user.getId())
                        .userName(user.getUsername())
                        .build()
        );
    }
}
