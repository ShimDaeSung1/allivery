package org.zerock.allivery.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.allivery.entity.user.User;
import org.zerock.allivery.entity.user.UserAccount;
import org.zerock.allivery.entity.user.UserRepository;
import org.zerock.allivery.exception.CUserNotFoundException;
import org.zerock.allivery.exception.handler.ErrorCode;

@RequiredArgsConstructor
@Service
@Log4j2
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new CUserNotFoundException(
                "유저를 찾을 수 없습니다." , ErrorCode.FORBIDDEN
        ));
        return user;
    }
}
