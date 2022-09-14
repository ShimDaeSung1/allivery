package org.zerock.allivery.service.user;

import org.zerock.allivery.dto.user.LoginDTO;
import org.zerock.allivery.dto.user.SignupUserDTO;
import org.zerock.allivery.dto.user.TokenUserDTO;
import org.zerock.allivery.model.SingleResult;


public interface UserService {

    SingleResult<TokenUserDTO> login(LoginDTO loginDTO);
    SignupUserDTO signup(SignupUserDTO signupUserDTO);
}
